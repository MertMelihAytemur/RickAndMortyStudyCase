package com.vmlmedia.rickandmortystudycase.core.ui

import android.content.Context
import com.vmlmedia.core.presentation.CoreLoadingDialog
import com.vmlmedia.rickandmortystudycase.R

class LoadingDialog(context: Context) : CoreLoadingDialog(context) {
    override val layoutId: Int
        get() = R.layout.dialog_loading
}