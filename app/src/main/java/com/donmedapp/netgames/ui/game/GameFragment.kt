package com.donmedapp.netgames.ui.game


import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.PixelFormat
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.MediaController
import android.widget.Toast
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
import com.donmedapp.netgames.data.pojo.Game
import com.donmedapp.netgames.data.pojo.UserGame
import com.donmedapp.netgames.extensions.invisibleUnless
import com.donmedapp.netgames.ui.MainViewModel
import com.donmedapp.netgames.utils.isNetDisponible
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.like.LikeButton
import com.like.OnLikeListener
import kotlinx.android.synthetic.main.game_fragment.*


/**
 * A simple [Fragment] subclass.
 */
class GameFragment : Fragment(R.layout.game_fragment) {


    private lateinit var gameAdapter: GameFragmentAdapter


    val myDB = FirebaseFirestore.getInstance()

    private val viewModel: GameViewmodel by viewModels {
        GameViewmodelFactory(activity!!.application)
    }

    var viewmodelActivity: MainViewModel = MainViewModel()

    private val gameId: Long by lazy {
        arguments!!.getLong(getString(R.string.ARG_GAME_ID))
    }


    // The system "short" animation time duration, in milliseconds. This
    // duration is ideal for subtle animations or animations that occur
    // very frequently.
    private var shortAnimationDuration: Int = 0

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //scrollview.post { scrollview.fullScroll(View.FOCUS_UP) } //Posible solucion para el scroll
        setupViews()

    }


    private fun setupViews() {
        setupAppBar()
        setHasOptionsMenu(true)
        setLblVisibility()
        if(isNetDisponible(context!!)){
            viewModel.getGame(gameId)
            viewModel.getScreenGame(gameId)
            setupAdapter()
            setupRecyclerView()
            observeLiveData()
            setupLikeBtn()
            btnBuy.setOnClickListener { navegateToStore() }
        }else{
            Snackbar.make(
                lblDescription,
                getString(R.string.no_conection_detected),
                Snackbar.LENGTH_SHORT
            ).show()
        }


    }
    private fun setLblVisibility() {
        imgGameG.invisibleUnless(isNetDisponible(context!!))
        lblName.invisibleUnless(isNetDisponible(context!!))
        lblDescription.invisibleUnless(isNetDisponible(context!!))
        lblPlatforms.invisibleUnless(isNetDisponible(context!!))
        spinner1.invisibleUnless(isNetDisponible(context!!))
        btnBuy.invisibleUnless(isNetDisponible(context!!))
        lblScreenshots.invisibleUnless(isNetDisponible(context!!))
        lstScreenshots.invisibleUnless(isNetDisponible(context!!))
        lblScreenshots2.invisibleUnless(isNetDisponible(context!!))
        lblVideo.invisibleUnless(isNetDisponible(context!!))
        lblVideo2.invisibleUnless(isNetDisponible(context!!))
        emptyView.invisibleUnless(!isNetDisponible(context!!))



    }


    private fun setupLikeBtn() {

        val gameNew = myDB.collection("users").document(viewmodelActivity.mAuth.currentUser!!.uid)
        setupBtn(gameNew)

            likeButton.setOnLikeListener(object : OnLikeListener {
                override fun liked(likeButton: LikeButton?) {
                    gameNew.get().addOnSuccessListener { documentSnapshot ->
                        val userGames = documentSnapshot.toObject(UserGame::class.java)
                        viewModel.game.value.run {
                            userGames!!.games.add(
                                Game(
                                    this!!.id.toString(),
                                    name,
                                    backgroundImage ?: "",
                                    released
                                )
                            )

                        }
                        //val userG = UserGame(userGames!!.games,viewmodelActivity.mAuth.currentUser!!.uid)
                        myDB.collection("users").document(viewmodelActivity.mAuth.currentUser!!.uid)
                            .set(userGames!!)
                    }
                    if(isNetDisponible(context!!)) {
                        Snackbar.make(
                            lblDescription,
                            getString(R.string.game_gameadded),
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }else {
                        Snackbar.make(
                            lblDescription,
                            getString(R.string.no_conection_detected),
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }


                }

                override fun unLiked(likeButton: LikeButton?) {
                    gameNew.get().addOnSuccessListener { documentSnapshot ->
                        val userGames = documentSnapshot.toObject(UserGame::class.java)
                        viewModel.game.value.run {
                            userGames!!.games.remove(
                                Game(
                                    this!!.id.toString(),
                                    name,
                                    backgroundImage ?: "",
                                    released
                                )
                            )
                        }
                        //val userG = UserGame(userGames!!.games,viewmodelActivity.mAuth.currentUser!!.uid)
                        myDB.collection("users").document(viewmodelActivity.mAuth.currentUser!!.uid)
                            .set(userGames!!)
                    }
                    if(isNetDisponible(context!!)) {
                        Snackbar.make(
                            lblDescription,
                            getString(R.string.game_gameremoved),
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }else {
                        Snackbar.make(
                            lblDescription,
                            getString(R.string.no_conection_detected),
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }

                }

            })

    }

    private fun setupBtn(gameNew: DocumentReference) {
        gameNew.get().addOnSuccessListener { documentSnapshot ->
            val userGames = documentSnapshot.toObject(UserGame::class.java)
            lateinit var game: Game
            viewModel.game.observe(this) {
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


            //Thread.sleep(500)
            // likeButton.isLiked=false
            //arreglar lo del likebutton


        }
    }

    private fun observeLiveData() {
        viewModel.game.observe(this) {
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
                playVideo(it.clip!!.clip)
            }
            lblMetacritic.invisibleUnless(it.hasMetacriticRating())
            if (it.hasMetacriticRating()) {
                lblMetacritic.text = (it.metacritic!!.toFloat()/10).toString()
            }
            if (!it.description.isNullOrBlank()) {
                lblDescription2.text =
                    HtmlCompat.fromHtml(it.description, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
            } else {
                lblDescription2.text = "No description"
            }
            if (it.platforms != null) {
                setupPlatforms(it)
            }
            if (it.stores != null) {
                setupSpinner(it.stores)
            }
        }
        viewModel.screenGame.observe(this) {
            showScreens(it.results)
        }

    }

    private fun setupPlatforms(it: Result) {
        imgPs4.invisibleUnless(it.hasPlatform("playstation"))
        lblPlatformsH.invisibleUnless(it.hasPlatform("pc") || it.hasPlatform("linux"))
        imgXbox.invisibleUnless(it.hasPlatform("xbox"))
        imgNintendo.invisibleUnless(
            it.hasPlatform("nintendo")
        )
        imgMobile.invisibleUnless(it.hasPlatform("ios") || it.hasPlatform("Android"))
        imgWii.invisibleUnless(it.hasPlatform("wii"))
    }

    private fun setupSpinner(stores: List<StoreObj>) {
        var list = ArrayList<String>()
        stores.forEach { list.add(it.store!!.name!!) }
        if (list.isEmpty()) {
            list.add(getString(R.string.no_stores))
        }
        val adapter = ArrayAdapter(activity!!, android.R.layout.simple_spinner_item, list)
        spinner1.adapter = adapter
    }

    private fun navegateToStore() {
        if (spinner1.selectedItem.toString() != getString(R.string.no_stores)) {
            val store = viewModel.game.value!!.stores!![spinner1.selectedItemPosition]
            var url = store.url!!
            if (!url.startsWith("http://") && !url.startsWith("https://"))
                url = "http://$url"
            val browserIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(browserIntent)
            // btnBuy.setOnClickListener { startActivity(browserIntent) }
        } else {
            Snackbar.make(
                lblDescription,
                getString(R.string.game_noshops),
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun playVideo(videopath: String) {
        try {
            activity!!.window.setFormat(PixelFormat.TRANSLUCENT)
            var mediaController = MediaController(this.context)
            mediaController.setAnchorView(video)
            mediaController.setBackgroundResource(R.color.white)
            val videoUri = Uri.parse(videopath)
            video.setMediaController(mediaController)
            video.setVideoURI(videoUri)
            video.setOnPreparedListener {
                //it.start()
            }
        } catch (e: Exception) {
            Snackbar.make(
                lblDescription,
                "Video Play Error :" + e.message,
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    private fun setupAppBar() {

        viewModel.game.observe(this) {
            (requireActivity() as AppCompatActivity).supportActionBar?.run {
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

        gameAdapter = GameFragmentAdapter(this.requireActivity()).also {
            it.onItemClickListener = {

            }
        }
    }

    private fun showScreens(screenshots: List<Result2>?) {
        lstScreenshots.post {
            gameAdapter.submitList(screenshots)
            lblScreenshots2.invisibleUnless(screenshots!!.isEmpty())
        }
        // lstScreenshots.smoothScrollToPosition(0)
    }
}

