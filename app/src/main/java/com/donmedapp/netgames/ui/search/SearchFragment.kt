package com.donmedapp.netgames.ui.search


import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
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
import com.donmedapp.netgames.extensions.hideSoftKeyboard
import com.donmedapp.netgames.extensions.invisibleUnless
import com.donmedapp.netgames.utils.isNetDisponible
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.search_fragment.*


/**
 * A simple [Fragment] subclass.
 */
class SearchFragment : Fragment(R.layout.search_fragment){

    private val viewModel: SearchViewmodel by viewModels {
        SearchViewmodelFactory(activity!!.application)
    }

    private var searchAdapter = SearchFragmentAdapter()


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViews()

    }

    private fun setupViews() {
        setupAppBar()
        setHasOptionsMenu(true)
        //search()
        if (isNetDisponible(context!!)) {
            setupAdapter()
            setupRecyclerView()
            observeLiveData()
            setupOnEditorAction()
        } else {
            Snackbar.make(
                lstSearch,
                getString(R.string.no_conection_detected),
                Snackbar.LENGTH_SHORT
            ).show()
        }

    }



    private fun setupOnEditorAction() {
        txtSearch.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchText(txtSearch.text.toString())
                txtSearch.hideSoftKeyboard()
                return@OnEditorActionListener true
            }
            false
        })

    }

    private fun setupRecyclerView() {
        lstSearch.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            adapter = searchAdapter

        }
    }

    private fun setupAdapter() {
        searchAdapter.also {
            it.onItemClickListener = { navegateToGame(it) }

        }

    }

    private fun navegateToGame(id: Int) {
        val gameId = searchAdapter.currentList[id].id
        findNavController().navigate(
            R.id.navToGame, bundleOf(
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
        lstSearch.post {
            searchAdapter.submitList(results)
            emptyView.invisibleUnless(results.isEmpty())
        }
        //lstSearch.smoothScrollToPosition(0)


    }


    private fun searchText(text: String) {
        if (text.isNotEmpty()) {
            viewModel.search(text)
            txtSearch.clearFocus()
        }
        txtSearch.hideSoftKeyboard()
    }

    private fun setupAppBar() {
        (requireActivity() as AppCompatActivity).supportActionBar?.run {
            setDisplayShowTitleEnabled(false)

            setTitle(R.string.app_name)
            setDisplayHomeAsUpEnabled(false)
        }

    }



}
