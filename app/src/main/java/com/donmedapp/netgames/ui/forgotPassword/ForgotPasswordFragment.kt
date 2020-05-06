package com.donmedapp.netgames.ui.forgotPassword


import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels

import com.donmedapp.netgames.R
import com.donmedapp.netgames.base.observeEvent
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.forgot_password_fragment.*

/**
 * A simple [Fragment] subclass.
 */
class ForgotPasswordFragment : Fragment(R.layout.forgot_password_fragment) {


    private val viewmodel: ForgotPasswordViewmodel by viewModels {
        ForgotPasswordViewmodelFactory(activity!!.application, activity!!)
    }
    //Firebase references

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        setupAppBar()
        observeLiveDatas()
        setupOnEditorAction()
        btnSendPass!!.setOnClickListener {
            sendResetPasswordToEmail()
        }
    }

    private fun setupOnEditorAction() {
        txtEmailPassword.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                sendResetPasswordToEmail()
                return@OnEditorActionListener true
            }
            false
        })

    }

    private fun observeLiveDatas() {
        viewmodel.message.observeEvent(this) {
            Snackbar.make(
                txtEmailPassword,
                it,
                Snackbar.LENGTH_LONG
            ).show()
        }
        viewmodel.onBack.observeEvent(this) {
            if (it) {
                activity!!.onBackPressed()
            }
        }
    }

    private fun sendResetPasswordToEmail() {
        viewmodel.sendPasswordResetEmail(txtEmailPassword, txtEmailPassword.text.toString())
    }

    private fun setupAppBar() {
        (requireActivity() as AppCompatActivity).supportActionBar?.run {
            setTitle(R.string.send_password_title)
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }

    }


}
