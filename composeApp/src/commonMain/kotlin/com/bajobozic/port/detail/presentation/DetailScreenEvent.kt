package com.bajobozic.port.detail.presentation

interface DetailScreenEvent {
    data class ToggleVideoFullscreen(val toggleToFullScreen: Boolean) : DetailScreenEvent
    data object OpenMaps : DetailScreenEvent
}