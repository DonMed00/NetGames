package com.donmedapp.netgames.ui.edit


import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager

import com.donmedapp.netgames.R
import com.donmedapp.netgames.avatars
import com.donmedapp.netgames.base.observeEvent
import com.donmedapp.netgames.extensions.hideSoftKeyboard
import com.donmedapp.netgames.ui.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.edit_fragment.*

/**
 * A simple [Fragment] subclass.
 */
class EditFragment : Fragment(R.layout.edit_fragment) {
    private val settings: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(activity)
    }

    private lateinit var avatarAdapter: EditFragmentAdapter

    var viewmodelActivity: MainViewModel = MainViewModel()


    private val viewModel: EditViewmodel by viewModels {
        EditViewmodelFactory(activity!!.application)
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
        viewmodelActivity.setupData()
        observeMessage()
        observeLiveData()
        fab.setOnClickListener { saveAvatar() }
    }

    private fun observeLiveData() {
        viewmodelActivity.avatar.observe(this) {
            if (it != -1) {
                imgActualAvatar.setImageResource(it)

            }
        }
        viewmodelActivity.avatarName.observe(this) {
            if (it != "") {
                txtNick.setText(it)
            }
        }
    }

    private fun observeMessage() {
        viewmodelActivity.message.observeEvent(this) {
            Snackbar.make(lstAvatars, it, Snackbar.LENGTH_SHORT).show()
        }
    }


    private fun setupRecyclerView() {
        lstAvatars.run {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(activity, 3)
            adapter = avatarAdapter
        }
        showGames()
    }

    private fun setupAdapter() {
        avatarAdapter = EditFragmentAdapter().also {
            it.onItemClickListener = { position -> selectCurrentAvatar(position) }

        }

    }

    private fun selectCurrentAvatar(id: Int) {
        viewmodelActivity.changeAvatar(avatarAdapter.currentList[id])

    }

    private fun saveAvatar() {
        txtNick.hideSoftKeyboard()
        if(txtNick.text.toString().isEmpty()){
            viewmodelActivity.setMessage("Nickname is empty")
        }else {
            viewmodelActivity.setName(txtNick.text.toString())
            viewmodelActivity.setupAvatar()
        }


    }

    private fun showGames() {
        lstAvatars.post {
            avatarAdapter.submitList(avatars)

        }

    }


    private fun setupAppBar() {
        (requireActivity() as AppCompatActivity).supportActionBar?.run {
            setTitle(R.string.edit_title)
            setDisplayHomeAsUpEnabled(true)
        }

    }
}
