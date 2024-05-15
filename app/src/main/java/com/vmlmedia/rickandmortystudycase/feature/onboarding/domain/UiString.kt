package com.vmlmedia.rickandmortystudycase.feature.onboarding.domain

import android.content.Context
import androidx.annotation.StringRes
import java.io.Serializable

sealed class UiString: Serializable {

    data class PlainResource(@StringRes val id: Int): UiString()

    data class ArgumentsResource(@StringRes val id: Int, val args: Any): UiString()
}

fun UiString.getText(context: Context) = when(this){
    is UiString.ArgumentsResource -> context.getString(id, args)
    is UiString.PlainResource -> context.getText(id)
}