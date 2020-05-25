package com.donmedapp.netgames.ui.edit


import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import com.donmedapp.netgames.R
import com.donmedapp.netgames.avatars
import com.donmedapp.netgames.base.observeEvent
import com.donmedapp.netgames.extensions.hideSoftKeyboard
import com.donmedapp.netgames.ui.MainViewModel
import com.donmedapp.netgames.utils.isNetDisponible
import com.donmedapp.netgames.utils.roundedImg
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.edit_fragment.*

/**
 * A simple [Fragment] subclass.
 */
class EditFragment : Fragment(R.layout.edit_fragment) {

    private lateinit var avatarAdapter: EditFragmentAdapter

    var viewmodelActivity: MainViewModel = MainViewModel()


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
        observeLiveDatas()
        observeLiveData()
        setupOnEditorAction()
        fab.setOnClickListener { saveAvatar() }
    }

    private fun observeLiveData() {
        viewmodelActivity.avatar.observe(this) {
            if (it != -1) {
                imgActualAvatar.setImageDrawable(roundedImg(it,resources))
                avatarAdapter.currentPosition= avatars.indexOf(it)

            }
        }
        viewmodelActivity.avatarName.observe(this) {
            if (it != "") {
                txtNick.setText(it)
            }
        }
    }

    private fun observeLiveDatas() {
        viewmodelActivity.message.observeEvent(this) {
            Snackbar.make(lstAvatars, it, Snackbar.LENGTH_SHORT).show()
        }
        viewmodelActivity.onBack.observeEvent(this) {
            if (it) {
                activity!!.onBackPressed()
            }
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
            it.onItemClickListener = { position -> selectCurrentAvatar(position)
                it.currentPosition = position
                it.notifyDataSetChanged()}

        }

    }

    private fun selectCurrentAvatar(id: Int) {
        viewmodelActivity.changeAvatar(avatarAdapter.currentList[id])

    }

    private fun saveAvatar() {
        txtNick.clearFocus()
        txtNick.hideSoftKeyboard()
        if(isNetDisponible(context!!)){
            if(txtNick.text.toString().isEmpty()){
                viewmodelActivity.setMessage(getString(R.string.nickname_empty))
            }else {
                viewmodelActivity.setName(txtNick.text.toString())
                viewmodelActivity.setupAvatar(getString(R.string.avatar_changed))

                }
        }else{
            viewmodelActivity.setMessage(getString(R.string.no_conection_detected))
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

    private fun setupOnEditorAction() {
        txtNick.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                saveAvatar()
                return@OnEditorActionListener true
            }
            false
        })

    }
}
