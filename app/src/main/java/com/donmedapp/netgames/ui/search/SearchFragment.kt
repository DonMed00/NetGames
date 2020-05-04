package com.donmedapp.netgames.ui.search


import android.app.Activity
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
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
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.search_fragment.*


/**
 * A simple [Fragment] subclass.
 */
class SearchFragment : Fragment(R.layout.search_fragment) {

    private val viewModel: SearchViewmodel by viewModels {
        SearchViewmodelFactory(activity!!.application)
    }

    private var searchAdapter= SearchFragmentAdapter()


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViews()

    }

    private fun setupViews() {
        setupAppBar()
        setHasOptionsMenu(true)
        //search()
        if(isNetDisponible(context!!)){
            setupAdapter()
            setupRecyclerView()
            observeLiveData()
            imgSearch.setOnClickListener {
                searchText(txtSearch.text.toString())
                txtSearch.hideSoftKeyboard()
            }
        }else{
            Snackbar.make(
                lstSearch,
                getString(R.string.no_conection_detected),
                Snackbar.LENGTH_SHORT
            ).show()        }

    }

    private fun setupRecyclerView() {
        lstSearch.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity,RecyclerView.VERTICAL,false)
            adapter = searchAdapter

        }
    }

    private fun setupAdapter() {
        searchAdapter.also {
            it.onItemClickListener = {navegateToGame(it)}

        }

    }

    private fun navegateToGame(id: Int) {
        var gameId = searchAdapter.currentList[id].id
        findNavController().navigate(R.id.navToGame, bundleOf(
            getString(R.string.ARG_GAME_ID) to gameId
        ))
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

    private fun search() {
        txtSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                //searchText(txtSearch.text.toString())

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchText(txtSearch.text.toString())
            }
        })
    }


    private fun searchText(text: String) {
        if (text.isNotEmpty()) {
            viewModel.search(text)
        }
    }

    private fun setupAppBar() {
        (requireActivity() as AppCompatActivity).supportActionBar?.run {
            setTitle(R.string.app_name)

            setDisplayHomeAsUpEnabled(false)
        }

    }


}
