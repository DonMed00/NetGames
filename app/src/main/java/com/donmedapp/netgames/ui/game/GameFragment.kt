package com.donmedapp.netgames.ui.game


import android.graphics.PixelFormat
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.MediaController
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import coil.api.load
import com.donmedapp.netgames.R
import com.donmedapp.netgames.data.pojo.UserGame
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


    val myDB = FirebaseFirestore.getInstance()

    private val viewModel: GameViewmodel by viewModels {
        GameViewmodelFactory(activity!!.application)
    }

    var viewmodelActivity : MainViewModel = MainViewModel()

    private val gameId: Long by lazy {
        arguments!!.getLong(getString(R.string.ARG_GAME_ID))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        scrollview.post { scrollview.fullScroll(View.FOCUS_UP)  } //Posible solucion para el scroll
        viewModel.getGame(gameId)
        setupViews()

    }



    private fun setupViews() {
        setupAppBar()
        setHasOptionsMenu(true)
        observeLiveData()
        setupLikeBtn()
    }


    private fun setupLikeBtn() {
        val gameNew = myDB.collection("users").document(viewmodelActivity.mAuth.currentUser!!.uid)
        setupBtn(gameNew)

        likeButton.setOnLikeListener(object : OnLikeListener{
            override fun liked(likeButton: LikeButton?) {
                gameNew.get().addOnSuccessListener { documentSnapshot ->
                    val userGames = documentSnapshot.toObject(UserGame::class.java)
                    userGames!!.games.add(viewModel.game.value!!.id.toInt())
                    //val userG = UserGame(userGames!!.games,viewmodelActivity.mAuth.currentUser!!.uid)
                    myDB.collection("users").document(viewmodelActivity.mAuth.currentUser!!.uid).set(userGames)
                }

                Toast.makeText(activity,"Like",Toast.LENGTH_SHORT).show()
            }

            override fun unLiked(likeButton: LikeButton?) {
                gameNew.get().addOnSuccessListener { documentSnapshot ->
                    val userGames = documentSnapshot.toObject(UserGame::class.java)
                    userGames!!.games.remove(viewModel.game.value!!.id.toInt())
                    //val userG = UserGame(userGames!!.games,viewmodelActivity.mAuth.currentUser!!.uid)
                    myDB.collection("users").document(viewmodelActivity.mAuth.currentUser!!.uid).set(userGames)
                }
                Toast.makeText(activity,"DisLike",Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun setupBtn(gameNew: DocumentReference) {
        gameNew.get().addOnSuccessListener { documentSnapshot ->
            val userGames = documentSnapshot.toObject(UserGame::class.java)!!
            //Thread.sleep(500)
           // likeButton.isLiked=false
            likeButton.isLiked = userGames.games.contains(gameId.toInt())
        }
    }

    private fun observeLiveData() {
        viewModel.game.observe(this) {
            lblName.text = it.name
            imgGameG.load(it.backgroundImage)
            if(it.hasVideoContent()){
                playVideo(it.clip!!.clip)
            }else{
                video.visibility= View.INVISIBLE
            }
            if(it.hasMetacriticRating()){
                lblMetacritic.text=it.metacritic
            }
            if(!it.description.isNullOrBlank()){
                lblDescription2.text=HtmlCompat.fromHtml(it.description,HtmlCompat.FROM_HTML_MODE_LEGACY).toString()

            }
            if(it.hasPlatform("pc")){
                lblPlatforms.text="PC"
            }
        }
    }

    private fun playVideo(videopath: String) {
        try {
            activity!!.window.setFormat(PixelFormat.TRANSLUCENT)
            var mediaController = MediaController(activity)
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
}
