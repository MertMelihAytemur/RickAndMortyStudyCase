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

    fun setHasOnBoarding(hasOnBoarding: Boolean) {
        prefs.edit().putBoolean(KEY_ON_BOARDING_DONE, hasOnBoarding).apply()
    }

    fun isOnBoardingDone(): Boolean {
        return prefs.getBoolean(KEY_ON_BOARDING_DONE, false)
    }

    companion object{
        private const val PREFS_NAME = "client_preferences"
        private const val KEY_ON_BOARDING_DONE = "client_preferences"
    }
}