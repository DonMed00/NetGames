package com.donmedapp.netgames.ui.game


import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.PixelFormat
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.MediaController
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.donmedapp.netgames.R
import com.donmedapp.netgames.Result
import com.donmedapp.netgames.Result2
import com.donmedapp.netgames.StoreObj
import com.donmedapp.netgames.base.observeEvent
import com.donmedapp.netgames.data.pojo.Game
import com.donmedapp.netgames.data.pojo.UserGame
import com.donmedapp.netgames.extensions.invisibleUnless
import com.donmedapp.netgames.utils.isNetDisponible
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.DocumentReference
import com.like.LikeButton
import com.like.OnLikeListener
import kotlinx.android.synthetic.main.game_fragment.*


/**
 * A simple [Fragment] subclass.
 */
class GameFragment : Fragment(R.layout.game_fragment) {


    private lateinit var gameAdapter: GameFragmentAdapter


    private val viewmodel: GameViewmodel by viewModels {
        GameViewmodelFactory(requireActivity(), requireActivity().application)
    }
    private val gameId: Long by lazy {
        requireArguments().getLong(getString(R.string.ARG_GAME_ID))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViews()
    }
    private fun setupViews() {
        setupAppBar()
        setHasOptionsMenu(true)
        setLblVisibility()
        observeMessage()
        if (isNetDisponible(requireContext())) {
            viewmodel.getGame(gameId)
            viewmodel.getScreenGame(gameId)
            setupAdapter()
            setupRecyclerView()
            observeLiveData()
            setupLikeBtn()
            btnBuy.setOnClickListener { navegateToStore() }
        } else {
            Snackbar.make(
                lblDescription,
                getString(R.string.no_conection_detected),
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    private fun observeMessage() {
        viewmodel.message.observeEvent(this) {
            Snackbar.make(
                lblDescription,
                it,
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    private fun setLblVisibility() {
        imgGameG.invisibleUnless(isNetDisponible(requireContext()))
        lblName.invisibleUnless(isNetDisponible(requireContext()))
        lblDescription.invisibleUnless(isNetDisponible(requireContext()))
        lblPlatforms.invisibleUnless(isNetDisponible(requireContext()))
        spinner1.invisibleUnless(isNetDisponible(requireContext()))
        btnBuy.invisibleUnless(isNetDisponible(requireContext()))
        lblScreenshots.invisibleUnless(isNetDisponible(requireContext()))
        lstScreenshots.invisibleUnless(isNetDisponible(requireContext()))
        lblScreenshots2.invisibleUnless(isNetDisponible(requireContext()))
        lblVideo.invisibleUnless(isNetDisponible(requireContext()))
        lblVideo2.invisibleUnless(isNetDisponible(requireContext()))
        emptyView.invisibleUnless(!isNetDisponible(requireContext()))


    }


    private fun setupLikeBtn() {
        setupBtn(viewmodel.gameNew)

        likeButton.setOnLikeListener(object : OnLikeListener {
            override fun liked(likeButton: LikeButton?) {
                viewmodel.addGame()
            }

            override fun unLiked(likeButton: LikeButton?) {
                viewmodel.removeGame()

            }

        })

    }

    private fun setupBtn(gameNew: DocumentReference) {
        gameNew.get().addOnSuccessListener { documentSnapshot ->
            val userGames = documentSnapshot.toObject(UserGame::class.java)
            lateinit var game: Game
            viewmodel.game.observe(viewLifecycleOwner) {
                game = Game(
                    it.id.toString(),
                    it.name,
                    it.backgroundImage ?: "",
                    it.released
                )
                likeButton?.let {
                    likeButton.visibility = View.VISIBLE
                    likeButton.isLiked = userGames?.games!!.contains(game)
                }
            }
        }
    }

    private fun observeLiveData() {
        viewmodel.game.observe(viewLifecycleOwner) {
            lblName.text = it.name
            if (!it.backgroundImage.isNullOrBlank()) {
                imgGameG.load(it.backgroundImage)

            }
            if (it.toStringPlatforms() == "No platforms") {
                lblPlatformsNo.invisibleUnless(true)

            }

            lblVideo2.invisibleUnless(!it.hasVideoContent())
            video.invisibleUnless(it.hasVideoContent())
            if (!video.isVisible) {

                video.layoutParams = ConstraintLayout.LayoutParams(0, 0)
            }
            if (it.hasVideoContent()) {
                setupVideo(it.clip!!.clip)
            }
            lblMetacritic.invisibleUnless(false)
            if (it.hasMetacriticRating()) {
                lblMetacritic.text = (it.metacritic!!.toFloat() / 10).toString()
            }
            if (!it.description.isBlank()) {
                lblDescription2.text =
                    HtmlCompat.fromHtml(it.description, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
            } else {
                lblDescription2.text = getString(R.string.no_description)
            }
            if (it.platforms != null) {
                setupPlatforms(it)
            }
            if (it.stores != null) {
                setupSpinner(it.stores)
            }
        }
        viewmodel.screenGame.observe(viewLifecycleOwner) {
            showScreens(it.results)
        }

    }

    private fun setupPlatforms(it: Result) {
        imgPs4.invisibleUnless(it.hasPlatform("playstation") || it.hasPlatform("xbox")  ||it.hasPlatform("nintendo"))
        imgPlatformsH.invisibleUnless(it.hasPlatform("pc") || it.hasPlatform("linux") || it.hasPlatform("mac"))
        imgMobile.invisibleUnless(it.hasPlatform("ios") || it.hasPlatform("Android"))
        imgWii.invisibleUnless(it.hasPlatform("wii"))
    }

    private fun setupSpinner(stores: List<StoreObj>) {
        val list = ArrayList<String>()
        stores.forEach { list.add(it.store!!.name!!) }
        if (list.isEmpty()) {
            list.add(getString(R.string.no_stores))
        }
        val adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, list)
        spinner1.adapter = adapter
    }

    private fun navegateToStore() {
        if (viewmodel.navigateToStore(spinner1).isNotEmpty()) {
            val browserIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse(viewmodel.navigateToStore(spinner1)))
            startActivity(browserIntent)
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun setupVideo(videopath: String) {
        try {
            requireActivity().window.setFormat(PixelFormat.TRANSLUCENT)
            val mediaController = MediaController(this.context)
            mediaController.setAnchorView(video)
            mediaController.setBackgroundResource(R.color.white)
            val videoUri = Uri.parse(videopath)
            video.setMediaController(mediaController)
            video.setVideoURI(videoUri)
            video.setOnPreparedListener {

            }
        } catch (e: Exception) {
            viewmodel.setMessage("Video Play Error :" + e.message)
        }
    }

    private fun setupAppBar() {


        viewmodel.game.observe(viewLifecycleOwner) {
            (requireActivity() as AppCompatActivity).supportActionBar?.run {
                setDisplayShowTitleEnabled(true)
                title = it.name
                setDisplayHomeAsUpEnabled(true)
            }
        }

    }

    private fun setupRecyclerView() {
        lstScreenshots.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
            adapter = gameAdapter

        }
    }

    private fun setupAdapter() {

        gameAdapter = GameFragmentAdapter().also {
            it.onItemClickListener = {

            }
        }
    }

    private fun showScreens(screenshots: List<Result2>?) {
        lstScreenshots.post {
            gameAdapter.submitList(screenshots)
            lblScreenshots2.invisibleUnless(screenshots!!.isEmpty())
        }
    }
}

