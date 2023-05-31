package ayds.winchester.songinfo.moredetails.presentation

import ayds.observer.Observable
import ayds.observer.Subject
import ayds.winchester.songinfo.moredetails.domain.repository.ArtistRepository
import ayds.winchester.songinfo.moredetails.domain.entities.Card

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
            notifyUiState(artist.component2())
        }.start()
    }

    private fun notifyUiState(card: Card?) {
        val uiState = createUiState(card)
        uiStateSubject.notify(uiState)
    }
    private fun createUiState(card: Card?): OtherInfoUiState {
        return when(card){
            is Card.ArtistCard -> {val info = infoSongFormat.formatInfoSong(card)
                OtherInfoUiState(sourceLogo = card.sourceLogoUrl ,description = info, sourceArticleUrl = card.infoUrl, sourceName = card.source.name)
            }
            else -> OtherInfoUiState()
        }
    }

}