package com.donmedapp.netgames.ui.home


import android.content.SharedPreferences
import android.os.Bundle
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
import com.donmedapp.netgames.extensions.invisibleUnless
import com.donmedapp.netgames.ui.MainViewModel
import com.donmedapp.netgames.ui.favorites.FavoritesFragmentAdapter
import com.donmedapp.netgames.utils.isNetDisponible
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.home_fragment.*


/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment(R.layout.home_fragment) {


    private val settings: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(activity)
    }

    private var actionAdapter = HomeFragmentAdapter()

    private var strategyAdapter = HomeFragmentAdapter()

    private var sportsAdapter = HomeFragmentAdapter()

    private var adventureAdapter = HomeFragmentAdapter()

    private var simulationAdapter = HomeFragmentAdapter()

    private var myFavsAdapter = FavoritesFragmentAdapter()


    var viewmodelActivity: MainViewModel = MainViewModel()


    private val viewModel: HomeViewmodel by viewModels {
        HomeViewmodelFactory(activity!!.application)
    }
    // private lateinit var mAuth: FirebaseAuth

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //viewModel.setupData()
        findNavController().graph.startDestination = R.id.homeDestination
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
        setupAppBar()
        setHasOptionsMenu(true)
        setLblVisibility()
        if(isNetDisponible(context!!)){

            viewModel.setupFirebaseData()
            setupAdapters()
            setupRecyclerViews()
            observeLiveData()
            viewmodelActivity.setupData()
        }else {
            Snackbar.make(
                lstAction,
                getString(R.string.no_conection_detected),
                Snackbar.LENGTH_SHORT
            ).show()
        }

    }

    private fun setLblVisibility() {
        lblAction.invisibleUnless(isNetDisponible(context!!))
        lblStrategy.invisibleUnless(isNetDisponible(context!!))
        lblSports.invisibleUnless(isNetDisponible(context!!))
        lblAdventure.invisibleUnless(isNetDisponible(context!!))
        lblSimulation.invisibleUnless(isNetDisponible(context!!))
        lblFavs.invisibleUnless(isNetDisponible(context!!))
        lblHomeEmpty.invisibleUnless(isNetDisponible(context!!))
        emptyView.invisibleUnless(!isNetDisponible(context!!))
    }

    private fun setupRecyclerViews() {
        setupRecycler(lstAction, actionAdapter)
        setupRecycler(lstStrategy, strategyAdapter)
        setupRecycler(lstSports, sportsAdapter)
        setupRecycler(lstAdventure, adventureAdapter)
        setupRecycler(lstSimulation, simulationAdapter)
        lstFavs.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
            adapter = myFavsAdapter

        }
    }

    private fun setupRecycler(lst: RecyclerView, adapterH: HomeFragmentAdapter) {
        lst.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
            adapter = adapterH

        }
    }

    private fun setupAdapters() {

        myFavsAdapter = FavoritesFragmentAdapter().also {
            it.onItemClickListener = { navegateToGameFavs(it) }


            setupAdapter(actionAdapter)
            setupAdapter(strategyAdapter)
            setupAdapter(sportsAdapter)
            setupAdapter(adventureAdapter)
            setupAdapter(simulationAdapter)


        }


    }


    private fun setupAdapter(adapter: HomeFragmentAdapter) {
        adapter.also {
            it.onItemClickListener = { navegateToGame(it, adapter) }
        }
    }

    private fun navegateToGame(id: Int, adapter: HomeFragmentAdapter) {
        val gameId = adapter.currentList[id].id
        //Thread.sleep(500)
        findNavController().navigate(
            R.id.navToGame2, bundleOf(
                getString(R.string.ARG_GAME_ID) to gameId
            )
        )
    }

    private fun navegateToGameFavs(id: Int) {
        val gameId = myFavsAdapter.currentList[id].gameId
        //Thread.sleep(500)
        findNavController().navigate(
            R.id.navToGame2, bundleOf(
                getString(R.string.ARG_GAME_ID) to gameId.toLong()
            )
        )
    }

    private fun observeLiveData() {
        viewModel.gamesAction.observe(this) {
            showGames(lstAction, actionAdapter, it)
        }
        viewModel.gamesStrategy.observe(this) {
            showGames(lstStrategy, strategyAdapter, it)
        }

        viewModel.gamesSports.observe(this) {
            showGames(lstSports, sportsAdapter, it)
        }
        viewModel.gamesAdventure.observe(this) {
            showGames(lstAdventure, adventureAdapter, it)
        }

        viewModel.gamesSimulation.observe(this) {
            showGames(lstSimulation, simulationAdapter, it)
        }

        viewmodelActivity.gamesFav.observe(this) {
            lstFavs.post {
                myFavsAdapter.submitList(it)
            }
            lblHomeEmpty.invisibleUnless(it.isEmpty())
        }

    }

    private fun showGames(lst: RecyclerView, adapter: HomeFragmentAdapter, results: List<Result>) {
        lst.post {
            adapter.submitList(results)
        }
    }



    private fun setupAppBar() {
        (requireActivity() as AppCompatActivity).supportActionBar?.run {
            setDisplayShowTitleEnabled(false)
            //setTitle(R.string.app_name)
            setDisplayHomeAsUpEnabled(false)
        }

    }

}



