package com.donmedapp.netgames.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.donmedapp.netgames.BuildConfig
import com.donmedapp.netgames.R

/**
 * A simple [Fragment] subclass.
 */
class InfoDialog : DialogFragment() {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.info_dialog_title))
            .setMessage(
                getString(
                    R.string.nameapp,
                    getString(R.string.app_name)
                ) + getString(
                    R.string.versionapp,
                    BuildConfig.VERSION_CODE.toString()
                ) + getString(
                    R.string.version_name,
                    BuildConfig.VERSION_NAME
                )  +
                        getString(
                            R.string.licenses
                        )
            )
            .setPositiveButton(getString(R.string.info_dialog_ok)) { _, _ -> }
            .create()
}
