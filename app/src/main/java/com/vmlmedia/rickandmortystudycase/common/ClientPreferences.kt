package com.vmlmedia.rickandmortystudycase.common

import android.content.Context
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ClientPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun clear() {
        prefs.edit().clear().apply()
    }

    private fun <T> toJsonString(obj: T): String {
        return Gson().toJson(obj)
    }

    private inline fun <reified T> fromJsonString(json: String): T {
        return Gson().fromJson(json, T::class.java)
    }

    companion object{
        private const val PREFS_NAME = "client_preferences"
    }
}