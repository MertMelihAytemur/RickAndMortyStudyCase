package com.vmlmedia.core.data

data class LoadingState(
    var show : Visibility = Visibility.IDLE
)

enum class Visibility {
    SHOW, HIDE, IDLE
}