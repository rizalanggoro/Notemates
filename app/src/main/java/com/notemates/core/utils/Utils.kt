package com.notemates.core.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar

class Utils {
    companion object {
        fun showSnackbar(
            view: View,
            message: String,
        ) = Snackbar
            .make(view, message, Snackbar.LENGTH_SHORT)
            .show()
    }
}