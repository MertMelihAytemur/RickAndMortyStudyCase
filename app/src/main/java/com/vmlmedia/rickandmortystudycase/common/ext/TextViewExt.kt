package com.vmlmedia.rickandmortystudycase.common.ext

import android.view.View
import android.widget.TextView

fun TextView.fadeVisibility(duration: Long = 0, visibleDuration: Long = 1000) {
    alpha = 0f
    visibility = View.VISIBLE

    animate()
        .alpha(1f)
        .setDuration(duration)
        .withEndAction {
            postDelayed({
                animate()
                    .alpha(0f)
                    .setDuration(duration)
                    .withEndAction {
                        visibility = View.GONE
                    }
            }, visibleDuration)
        }
}