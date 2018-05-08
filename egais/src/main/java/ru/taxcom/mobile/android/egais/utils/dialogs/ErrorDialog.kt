package ru.taxcom.mobile.android.egais.utils.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.annotation.NonNull
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AlertDialog
import ru.taxcom.mobile.android.egais.R

class ErrorDialog : DialogFragment() {
    private lateinit var mMessage: String

    companion object {
        const val TAG = "ErrorDialog"
        private val ARG_MESSAGE = "arg-message"
        fun getInstance(message: String): ErrorDialog {
            val errorDialog = ErrorDialog()
            val args = Bundle()
            args.putString(ARG_MESSAGE, message)
            errorDialog.arguments = args
            return errorDialog
        }
    }

    private var actionListener: ActionListener? = null

    interface ActionListener {
        fun errorDialogDismissed()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is ActionListener) {
            actionListener = context
        } else {
            throw IllegalArgumentException("${context?.packageName} should implement ${ActionListener::class.java.simpleName}")
        }
    }

    @NonNull
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (arguments != null) {
            mMessage = arguments!!.getString(ARG_MESSAGE)
        }
        val builder = AlertDialog.Builder(context!!)
        builder.setMessage(mMessage)
        builder.setPositiveButton(R.string.dialog_positive_btn) { dialogInterface, _ ->
            actionListener?.errorDialogDismissed()
            dialogInterface.dismiss()
        }
        isCancelable = false
        return builder.create()
    }

    fun show(supportFragmentManager: FragmentManager) {
        try {
            supportFragmentManager
                    .beginTransaction()
                    .add(this, this.tag)
                    .commitAllowingStateLoss()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    override fun onDetach() {
        super.onDetach()
        actionListener = null
    }
}