package com.donmedapp.netgames.ui.game


import android.content.Intent
import android.graphics.PixelFormat
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.MediaController
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.donmedapp.netgames.*
import com.donmedapp.netgames.data.pojo.Game
import com.donmedapp.netgames.data.pojo.UserGame
import com.donmedapp.netgames.extensions.invisibleUnless
import com.donmedapp.netgames.ui.MainViewModel
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //scrollview.post { scrollview.fullScroll(View.FOCUS_UP) } //Posible solucion para el scroll
        viewModel.getGame(gameId)
        viewModel.getScreenGame(gameId)
        setupViews()

    }


    private fun setupViews() {
        setupAppBar()
        setHasOptionsMenu(true)
        setupAdapter()
        setupRecyclerView()
        observeLiveData()
        setupLikeBtn()
        btnBuy.setOnClickListener { navegateToStore() }
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
                                backgroundImage,
                                released
                            )
                        )

                    }
                    //val userG = UserGame(userGames!!.games,viewmodelActivity.mAuth.currentUser!!.uid)
                    myDB.collection("users").document(viewmodelActivity.mAuth.currentUser!!.uid)
                        .set(userGames!!)
                }

                Toast.makeText(activity, "Like", Toast.LENGTH_SHORT).show()
            }

            override fun unLiked(likeButton: LikeButton?) {
                gameNew.get().addOnSuccessListener { documentSnapshot ->
                    val userGames = documentSnapshot.toObject(UserGame::class.java)
                    viewModel.game.value.run {
                        userGames!!.games.remove(
                            Game(
                                this!!.id.toString(),
                                name,
                                backgroundImage,
                                released
                            )
                        )
                    }
                    //val userG = UserGame(userGames!!.games,viewmodelActivity.mAuth.currentUser!!.uid)
                    myDB.collection("users").document(viewmodelActivity.mAuth.currentUser!!.uid)
                        .set(userGames!!)
                }
                Toast.makeText(activity, "DisLike", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun setupBtn(gameNew: DocumentReference) {
        gameNew.get().addOnSuccessListener { documentSnapshot ->
            val userGames = documentSnapshot.toObject(UserGame::class.java)
            lateinit var game : Game
            viewModel.game.observe(this){
                game = Game(
                    it.id.toString(),
                    it.name,
                    it.backgroundImage,
                    it.released)
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
            imgGameG.load(it.backgroundImage)
            lblVideo2.invisibleUnless(!it.hasVideoContent())
            video.invisibleUnless(it.hasVideoContent())
            if (it.hasVideoContent()) {
                playVideo(it.clip!!.clip)
            }
            lblMetacritic.invisibleUnless(it.hasMetacriticRating())
            if (it.hasMetacriticRating()) {
                lblMetacritic.text = it.metacritic
            }
            if (!it.description.isNullOrBlank()) {
                lblDescription2.text =
                    HtmlCompat.fromHtml(it.description, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
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
        imgPc.invisibleUnless(it.hasPlatform("pc") || it.hasPlatform("linux"))
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
            Toast.makeText(activity!!, "No existen tiendas", Toast.LENGTH_SHORT).show()
        }
    }

    private fun playVideo(videopath: String) {
        try {
            activity!!.window.setFormat(PixelFormat.TRANSLUCENT)
            var mediaController = MediaController(activity)
            mediaController.setAnchorView(video)
            val videoUri = Uri.parse(videopath)
            video.setMediaController(mediaController)
            video.setVideoURI(videoUri)
            video.setOnPreparedListener {
                it.start()
            }
        } catch (e: Exception) {
            println("Video Play Error :" + e.message)
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

        gameAdapter = GameFragmentAdapter().also {

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
