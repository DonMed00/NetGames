package com.donmedapp.netgames.ui.game


import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels

import com.donmedapp.netgames.R
import kotlinx.android.synthetic.main.game_fragment.*

/**
 * A simple [Fragment] subclass.
 */
class GameFragment : Fragment(R.layout.game_fragment) {


    private val viewModel: GameViewmodel by viewModels {
        GameViewmodelFactory(activity!!.application)
    }
    private val gameId: Long by lazy {
        arguments!!.getLong(getString(R.string.ARG_GAME_ID))
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getGame(gameId)
        setupViews()

    }

    private fun setupViews() {
        setupAppBar()
        setHasOptionsMenu(true)
        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.game.observe(this){
            lblName.text=it.name
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
