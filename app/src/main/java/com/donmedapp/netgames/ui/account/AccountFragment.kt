package com.donmedapp.netgames.ui.account

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.donmedapp.netgames.BuildConfig
import com.donmedapp.netgames.R
import com.donmedapp.netgames.base.observeEvent
import com.donmedapp.netgames.ui.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.account_fragment.*


/**
 * A simple [Fragment] subclass.
 */
class AccountFragment : Fragment(R.layout.account_fragment) {


    private val viewmodel: AccountViewmodel by viewModels {
        AccountViewmodelFactory(requireActivity().application, requireActivity())
    }

    private var viewmodelActivity: MainViewModel = MainViewModel()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViews()

    }


    private fun setupViews() {
        setupAppBar()
        setHasOptionsMenu(true)
        observeMessage()
        setupOnEditorAction(txtEmail)
        setupOnEditorAction(txtPassword)
        lblVersion.text = getString(R.string.appversion, BuildConfig.VERSION_NAME)
        txtEmail.setText(viewmodelActivity.mAuth.currentUser!!.email)
        txtPassword.setText(
            viewmodel.settings.getString(
                "currentPassword",
                getString(R.string.no_password)
            )
        )
        lblVersion.setOnClickListener { seeLicenses() }
        imgEdit.setOnClickListener { viewmodel.updateEmail(txtEmail, txtEmail.text.toString()) }
        imgEdit2.setOnClickListener {
            viewmodel.updatePassword(
                txtPassword,
                txtPassword.text.toString()
            )
        }
    }


    private fun seeLicenses() {
        val firstClicked = viewmodel.settings.getBoolean("firstClicked", true)
        val countTimes = viewmodel.settings.getInt("countTimes", 5)
        if (!firstClicked || countTimes == 0) {
            findNavController().navigate(R.id.infoDialog)
        }
        viewmodel.seeLicenses()

    }

    private fun setupOnEditorAction(editText: EditText) {
        editText.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (editText == txtEmail) {
                    viewmodel.updateEmail(txtEmail, txtEmail.text.toString())
                } else if (editText == txtPassword) {
                    viewmodel.updatePassword(txtPassword, txtPassword.text.toString())
                }
                return@OnEditorActionListener true
            }
            false
        })

    }

    private fun observeMessage() {
        viewmodel.message.observeEvent(this) {
            Snackbar.make(
                txtEmail,
                it,
                Snackbar.LENGTH_SHORT
            ).show()
        }
        viewmodel.messageT.observeEvent(this) {
            Toast.makeText(
                activity,
                it,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun setupAppBar() {
        (requireActivity() as AppCompatActivity).supportActionBar?.run {
            setDisplayShowTitleEnabled(true)
            setTitle(R.string.account_title)
            setDisplayHomeAsUpEnabled(true)
        }

    }
}




