package ayds.winchester.songinfo.moredetails.presentation

import ayds.observer.Observable
import ayds.observer.Subject
import ayds.winchester.songinfo.moredetails.domain.ArtistRepository
import ayds.winchester.songinfo.moredetails.domain.entities.Artist

interface MoreDetailsPresenter{
    fun generateArtistInfo()
}
class MoreDetailsPresenterImpl(
    private val artistRepository: ArtistRepository,
    private val format: InfoSongFormat,
) : MoreDetailsPresenter {

    private val uiStateSubject = Subject<OtherInfoUiState>()
    private val uiStateObservable: Observable<OtherInfoUiState> = uiStateSubject
    override fun generateArtistInfo() {
        Thread {
            val artist = artistRepository.getArtist()
            val uiState = createUiState(artist)
            uiStateSubject.notify(uiState)
            //displayArtistInfo(artist)
        }.start()
    }

    private fun createUiState(artist: Artist.WikipediaArtist?): OtherInfoUiState {
        val snipet = artistRepository
        val info = format.formatInfoSong(artist) // VER SNIPPET
        return OtherInfoUiState(info, artist?.wikipediaUrl)
    }

}