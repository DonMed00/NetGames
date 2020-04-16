package com.donmedapp.netgames.ui.home


import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.donmedapp.netgames.R
import com.donmedapp.netgames.Result
import com.donmedapp.netgames.data.pojo.UserGame
import com.donmedapp.netgames.ui.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.home_fragment.*


/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment(R.layout.home_fragment) {


    private val settings: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(activity)
    }

    private lateinit var homeAdapter: HomeFragmentAdapter

    var viewmodel: MainViewModel = MainViewModel()


    private val viewModel: HomeViewmodel by viewModels {
        HomeViewmodelFactory(activity!!.application)
    }
    // private lateinit var mAuth: FirebaseAuth

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        findNavController().graph.startDestination=R.id.homeDestination
       // viewModel.getGamesByGenre("strategy")
        setupViews()


       // if (settings.getString("currentUser", getString(R.string.no_user)) != getString(R.string.no_user)) {
          //  loginUser()
            //lblPrueba.text = mAuth.currentUser!!.email.toString()

            // Toast.makeText(activity, "No hay usuarios", Toast.LENGTH_SHORT).show()

       // } else {
          //  findNavController().navigate(R.id.loginDestination)
       // }
    }


    private fun setupViews() {
        setupFirebaseData()
        setupAppBar()
        setHasOptionsMenu(true)
        setupAdapter()
        setupRecyclerViews()
        observeLiveData()
    }

    private fun setupFirebaseData() {
        val myDB = FirebaseFirestore.getInstance()
        val gameNew = myDB.collection("users").document(viewmodel.mAuth.currentUser!!.uid)
        gameNew.get().addOnSuccessListener { documentSnapshot ->
            val userGames = documentSnapshot.toObject(UserGame::class.java)
            if(userGames==null){
                myDB.collection("users").document(viewmodel.mAuth.currentUser!!.uid)
                    .set(UserGame())
            }
        }

    }

    private fun setupRecyclerViews() {
        lstResults.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            adapter = homeAdapter

        }
    }

    private fun setupAdapter() {
        homeAdapter = HomeFragmentAdapter().also {
            it.onItemClickListener = { navegateToGame(it) }

        }

    }

    private fun navegateToGame(id: Int) {
        var gameId = homeAdapter.currentList[id].id
        //Thread.sleep(500)
        findNavController().navigate(
            R.id.navToGame2, bundleOf(
                getString(R.string.ARG_GAME_ID) to gameId
            )
        )
    }

    private fun observeLiveData() {
        viewModel.games.observe(this) {
            showGames(it)
        }

    }

    private fun showGames(results: List<Result>) {
        lstResults.post {
            homeAdapter.submitList(results)

        }

    }


    private fun setupAppBar() {
        (requireActivity() as AppCompatActivity).supportActionBar?.run {
            setTitle(R.string.home_title)
            setDisplayHomeAsUpEnabled(false)
        }

    }

    private fun loginUser() {
        //Obtenemos usuario y contraseña
        //Verificamos que los campos no este vacios
        viewmodel.mAuth.signInWithEmailAndPassword(
            settings.getString("currentUser", getString(R.string.no_user))!!,
            settings.getString("currentPassword", getString(R.string.no_password))!!
        )
            .addOnCompleteListener(activity!!) { task ->
                if (task.isSuccessful) {
                    //Toast.makeText(
                    //  activity, "Inicio de sesion correcto",
                    //  Toast.LENGTH_SHORT
                    // ).show()
                    // Si se inició correctamente la sesión vamos a la vista Home de la aplicación
                } else {
                    // sino le avisamos el usuairo que orcurrio un problema
                    //mProgressBar.hide()
                    Toast.makeText(
                        activity, "Authentication failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}



