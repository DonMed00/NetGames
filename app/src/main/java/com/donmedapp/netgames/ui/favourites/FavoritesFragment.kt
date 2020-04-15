package com.donmedapp.netgames.ui.favourites


import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.observe

import com.donmedapp.netgames.R
import com.donmedapp.netgames.Result
import com.donmedapp.netgames.data.pojo.UserGame
import com.donmedapp.netgames.ui.MainViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.favorites_fragment.*

/**
 * A simple [Fragment] subclass.
 */
class FavoritesFragment : Fragment(R.layout.favorites_fragment) {

    private val settings: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(activity)
    }

    private lateinit var favAdapter: FavoritesFragmentAdapter

    var viewmodelActivity: MainViewModel = MainViewModel()


    private val viewModel: FavoritesViewmodel by viewModels {
        FavoritesViewmodelFactory(activity!!.application)
    }
    // private lateinit var mAuth: FirebaseAuth

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViews()

    }


    private fun setupViews() {
        setupAppBar()
        setHasOptionsMenu(true)
        setupAdapter()
        setupRecyclerView()
        setupData()
        observeLiveData()
    }

    private fun setupData() {
        val myDB = FirebaseFirestore.getInstance()
        val gameNew = myDB.collection("users").document(viewmodelActivity.mAuth.currentUser!!.uid)
        gameNew.get().addOnSuccessListener { documentSnapshot ->
            val userGames = documentSnapshot.toObject(UserGame::class.java)
            viewModel.setGameId(userGames!!.games)

        }
    }

    private fun setupRecyclerView() {
        lstFavorites.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            adapter = favAdapter

        }
    }

    private fun setupAdapter() {
        favAdapter = FavoritesFragmentAdapter().also {
            it.onItemClickListener = { navegateToGame(it) }

        }

    }

    private fun navegateToGame(id: Int) {
        var gameId = favAdapter.currentList[id].id
        //Thread.sleep(500)
        findNavController().navigate(
            R.id.navToGame3, bundleOf(
                getString(R.string.ARG_GAME_ID) to gameId
            )
        )
    }

    private fun observeLiveData() {
        viewModel.gamesId.observe(this){
            for(num in it){
                viewModel.getGame(num.toLong())

            }
        }

        viewModel.games.observe(this){
            showGames(it)
        }

    }

    private fun showGames(results: List<Result>) {
        lstFavorites.post {
            favAdapter.submitList(results)

        }

    }


    private fun setupAppBar() {
        (requireActivity() as AppCompatActivity).supportActionBar?.run {
            setTitle(R.string.favorites_title)
            setDisplayHomeAsUpEnabled(true)
        }

    }


}
