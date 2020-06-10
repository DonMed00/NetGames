package com.donmedapp.netgames.ui.home


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.donmedapp.netgames.R
import com.donmedapp.netgames.Result
import com.donmedapp.netgames.extensions.invisibleUnless
import com.donmedapp.netgames.ui.MainViewModel
import com.donmedapp.netgames.utils.isNetDisponible
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.home_fragment.*


/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment(R.layout.home_fragment) {


    private var actionAdapter = HomeFragmentAdapter()

    private var strategyAdapter = HomeFragmentAdapter()

    private var sportsAdapter = HomeFragmentAdapter()

    private var adventureAdapter = HomeFragmentAdapter()

    private var simulationAdapter = HomeFragmentAdapter()

    private var puzzleAdapter = HomeFragmentAdapter()

    private var arcadeAdapter = HomeFragmentAdapter()


    private var viewmodelActivity: MainViewModel = MainViewModel()


    private val viewModel: HomeViewmodel by viewModels {
        HomeViewmodelFactory()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        findNavController().graph.startDestination = R.id.homeDestination
        setupViews()

    }


    private fun setupViews() {
        setupAppBar()
        setHasOptionsMenu(true)
        setLblVisibility()
        if (isNetDisponible(requireContext())) {

            viewModel.setupFirebaseData()
            setupAdapters()
            setupRecyclerViews()
            observeLiveData()
            viewmodelActivity.setupData()
        } else {
            Snackbar.make(
                lstAction,
                getString(R.string.no_conection_detected),
                Snackbar.LENGTH_SHORT
            ).show()
        }

    }

    private fun setLblVisibility() {
        lblAction.invisibleUnless(isNetDisponible(requireContext()))
        lblStrategy.invisibleUnless(isNetDisponible(requireContext()))
        lblSports.invisibleUnless(isNetDisponible(requireContext()))
        lblAdventure.invisibleUnless(isNetDisponible(requireContext()))
        lblSimulation.invisibleUnless(isNetDisponible(requireContext()))
        lblPuzzle.invisibleUnless(isNetDisponible(requireContext()))
        lblArcade.invisibleUnless(isNetDisponible(requireContext()))


        emptyView.invisibleUnless(!isNetDisponible(requireContext()))
    }

    private fun setupRecyclerViews() {
        setupRecycler(lstAction, actionAdapter)
        setupRecycler(lstStrategy, strategyAdapter)
        setupRecycler(lstSports, sportsAdapter)
        setupRecycler(lstAdventure, adventureAdapter)
        setupRecycler(lstSimulation, simulationAdapter)
        setupRecycler(lstPuzzle, puzzleAdapter)
        setupRecycler(lstArcade, arcadeAdapter)


    }

    private fun setupRecycler(lst: RecyclerView, adapterH: HomeFragmentAdapter) {
        lst.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
            adapter = adapterH

        }
    }

    private fun setupAdapters() {

        setupAdapter(actionAdapter)
        setupAdapter(strategyAdapter)
        setupAdapter(sportsAdapter)
        setupAdapter(adventureAdapter)
        setupAdapter(simulationAdapter)
        setupAdapter(puzzleAdapter)
        setupAdapter(arcadeAdapter)


    }


    private fun setupAdapter(adapter: HomeFragmentAdapter) {
        adapter.also {
            it.onItemClickListener = { navegateToGame(it, adapter) }
        }
    }

    private fun navegateToGame(id: Int, adapter: HomeFragmentAdapter) {
        val gameId = adapter.currentList[id].id
        findNavController().navigate(
            R.id.navToGame2, bundleOf(
                getString(R.string.ARG_GAME_ID) to gameId
            )
        )
    }


    private fun observeLiveData() {
        viewModel.gamesAction.observe(viewLifecycleOwner) {
            showGames(lstAction, actionAdapter, it)
        }
        viewModel.gamesStrategy.observe(viewLifecycleOwner) {
            showGames(lstStrategy, strategyAdapter, it)
        }

        viewModel.gamesSports.observe(viewLifecycleOwner) {
            showGames(lstSports, sportsAdapter, it)
        }
        viewModel.gamesAdventure.observe(viewLifecycleOwner) {
            showGames(lstAdventure, adventureAdapter, it)
        }

        viewModel.gamesSimulation.observe(viewLifecycleOwner) {
            showGames(lstSimulation, simulationAdapter, it)
        }
        viewModel.gamesPuzzle.observe(viewLifecycleOwner) {
            showGames(lstPuzzle, puzzleAdapter, it)
        }
        viewModel.gamesArcade.observe(viewLifecycleOwner) {
            showGames(lstArcade, arcadeAdapter, it)
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
            setDisplayHomeAsUpEnabled(false)
        }

    }

}



