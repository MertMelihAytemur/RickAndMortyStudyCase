package com.vmlmedia.rickandmortystudycase.common.ext

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavController

fun NavController.navigateSafely(
    @IdRes currentDestinationId: Int,
    @IdRes destinationId: Int,
    args: Bundle? = null
) {
    if (currentDestination?.id != currentDestinationId) {
        navigate(currentDestinationId)
    } else {
        navigate(destinationId, args)
    }
}