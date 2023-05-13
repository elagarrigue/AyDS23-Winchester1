package ayds.winchester.songinfo.moredetails.presentation

import ayds.observer.Observable
import ayds.observer.Subject
import ayds.winchester.songinfo.moredetails.domain.ArtistRepository
import ayds.winchester.songinfo.moredetails.domain.entities.Artist

interface MoreDetailsPresenter{
    val uiStateObservable: Observable<OtherInfoUiState>
    fun generateArtistInfo(artistName: String)
}
internal class MoreDetailsPresenterImpl(
    private val artistRepository: ArtistRepository,
    private val infoSongFormat: InfoSongFormat,
) : MoreDetailsPresenter {

    private val uiStateSubject = Subject<OtherInfoUiState>()
    override val uiStateObservable: Observable<OtherInfoUiState> = uiStateSubject
    override fun generateArtistInfo(artistName: String) {
        Thread {
            val artist = artistRepository.getArtist(artistName)
            val uiState = createUiState(artist)
            uiStateSubject.notify(uiState)
        }.start()
    }
    private fun createUiState(artist: Artist.WikipediaArtist): OtherInfoUiState {
        val info = infoSongFormat.formatInfoSong(artist)
        return OtherInfoUiState("",info, artist.wikipediaUrl)
    }

}