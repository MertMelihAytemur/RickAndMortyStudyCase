package com.vmlmedia.core.presentation

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window

/**
 * Base class for loading dialogs
 */
abstract class CoreLoadingDialog(private val context: Context) : Dialog(context),LoadingInterface {

    abstract val layoutId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(layoutId)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        setCancelable(false)
    }

    override fun show() {
        super.show()
    }

    override fun hide() {
        super.hide()
    }
}