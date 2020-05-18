package com.donmedapp.netgames.ui.register


import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.donmedapp.netgames.R
import com.donmedapp.netgames.base.observeEvent
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.register_fragment.*

/**
 * A simple [Fragment] subclass.
 */
class RegisterFragment : Fragment(R.layout.register_fragment) {


    private val viewmodel: RegisterViewmodel by viewModels {
        RegisterViewmodelFactory(activity!!.application, activity!!)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        setupAppBar()
        observeMessage()
        setupOnEditorAction()
        btnRegister.setOnClickListener {
            createNewAccount()
        }

    }

    private fun createNewAccount() {
        txtEmail.clearFocus()
        txtPassword.clearFocus()
        txtNickRegister.clearFocus()
        viewmodel.createNewAccount(
            txtEmail,
            txtEmail.text.toString(),
            txtPassword.text.toString(),
            txtNickRegister.text.toString()
        )

    }

    private fun observeMessage() {
        viewmodel.message.observeEvent(this) {
            Snackbar.make(
                txtEmail,
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

    private fun setupAppBar() {
        (requireActivity() as AppCompatActivity).supportActionBar?.run {
            setTitle(R.string.register_title)
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }

    }

    private fun setupOnEditorAction() {
        txtNickRegister.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                createNewAccount()
                return@OnEditorActionListener true
            }
            false
        })

    }

}
