package com.vmlmedia.rickandmortystudycase.common.ext

import android.os.Build
import android.os.Bundle
import java.io.Serializable

@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
inline fun <reified T : Serializable> Bundle.getSerializable(key: String): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getSerializable(key, T::class.java)
    } else {
        getSerializable(key) as? T
    }
}