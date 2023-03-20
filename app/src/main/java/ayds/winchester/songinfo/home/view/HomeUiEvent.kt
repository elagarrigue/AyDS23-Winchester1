package ayds.winchester.songinfo.home.view

sealed class HomeUiEvent {
    object Search : HomeUiEvent()
    object MoreDetails : HomeUiEvent()
    object OpenSongUrl : HomeUiEvent()
}