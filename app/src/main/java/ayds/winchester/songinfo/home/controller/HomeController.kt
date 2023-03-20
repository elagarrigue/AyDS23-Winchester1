package ayds.winchester.songinfo.home.controller

import ayds.winchester.songinfo.home.model.HomeModel
import ayds.winchester.songinfo.home.view.HomeUiEvent
import ayds.winchester.songinfo.home.view.HomeView
import ayds.observer.Observer
import ayds.winchester.songinfo.home.model.entities.Song

interface HomeController {

    fun setHomeView(homeView: HomeView)
}

internal class HomeControllerImpl(
    private val homeModel: HomeModel
) : HomeController {

    private lateinit var homeView: HomeView

    override fun setHomeView(homeView: HomeView) {
        this.homeView = homeView
        homeView.uiEventObservable.subscribe(observer)
    }

    private val observer: Observer<HomeUiEvent> =
        Observer { value ->
            when (value) {
                HomeUiEvent.Search -> searchSong()
                HomeUiEvent.MoreDetails -> moreDetails()
                is HomeUiEvent.OpenSongUrl -> openSongUrl()
            }
        }


    private fun searchSong() {
        // Warning: Never use Thread in android! Use coroutines
        Thread {
            homeModel.searchSong(homeView.uiState.searchTerm)
        }.start()
    }

    private fun moreDetails() {
        val song = homeModel.getSongById(homeView.uiState.songId)

        (song as? Song.SpotifySong)?.let {
            homeView.navigateToOtherDetails(song.artistName)
        }
    }

    private fun openSongUrl() {
        homeView.openExternalLink(homeView.uiState.songUrl)
    }
}