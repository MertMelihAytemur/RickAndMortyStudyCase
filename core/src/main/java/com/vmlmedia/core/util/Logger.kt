package com.vmlmedia.core.util

import android.util.Log
import com.vmlmedia.core.BuildConfig

private const val DEFAULT_LOG_TAG = "vl_media_logger"

fun logD(message: String,tag: String= DEFAULT_LOG_TAG) {
    log { Log.d(tag, message) }
}

fun logE(message: String,tag: String= DEFAULT_LOG_TAG) {
    log { Log.e(tag, message) }
}

fun logI(message: String,tag: String= DEFAULT_LOG_TAG) {
    log { Log.i(tag, message) }
}

fun logV(message: String,tag: String= DEFAULT_LOG_TAG) {
    log { Log.v(tag, message) }
}

private inline fun log(logAction: () -> Int) {
    if (BuildConfig.ENABLE_LOG) {
        logAction()
    }
}