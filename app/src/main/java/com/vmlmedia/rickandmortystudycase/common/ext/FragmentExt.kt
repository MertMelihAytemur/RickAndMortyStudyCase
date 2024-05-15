package com.vmlmedia.rickandmortystudycase.common.ext

import android.os.Bundle
import androidx.annotation.AnimRes
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.vmlmedia.rickandmortystudycase.R

fun Fragment.navigateWithAnimation(
    @IdRes destinationId: Int,
    bundle : Bundle? = null,
    @AnimRes enterAnim: Int = R.anim.slide_in_right,
    @AnimRes exitAnim: Int = R.anim.slide_out_left,
    @AnimRes popEnterAnim: Int = R.anim.slide_in_left,
    @AnimRes popExitAnim: Int = R.anim.slide_out_right
) {
    val navOptions = NavOptions.Builder()
        .setEnterAnim(enterAnim)
        .setExitAnim(exitAnim)
        .setPopEnterAnim(popEnterAnim)
        .setPopExitAnim(popExitAnim)
        .build()
    findNavController().navigate(destinationId, bundle, navOptions)
}