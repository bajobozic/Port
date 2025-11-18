package com.bajobozic.port.home.presentation.component

interface DetailScreenEvent {
    data class ToggleVideoFullscreen(val toggleToFullScreen: Boolean) : DetailScreenEvent
}