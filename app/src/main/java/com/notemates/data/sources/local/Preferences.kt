package com.notemates.data.sources.local

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

class Preferences @Inject constructor(
    private val application: Application,
) {
    fun instance(): SharedPreferences = application.getSharedPreferences(
        "notemates-preferences",
        Context.MODE_PRIVATE,
    )
}