package com.bajobozic.port.detail_ui.presentation

internal interface DetailScreenEvent {
    data class ToggleVideoFullscreen(val toggleToFullScreen: Boolean) : DetailScreenEvent
    data object OpenMaps : DetailScreenEvent
    data object OnNavigateUp : DetailScreenEvent
    data object ResetNotification : DetailScreenEvent
    data class ShowNotification(val movieName: String) : DetailScreenEvent
}