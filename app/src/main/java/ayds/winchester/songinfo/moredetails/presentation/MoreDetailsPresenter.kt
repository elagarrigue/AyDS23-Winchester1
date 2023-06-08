package ayds.winchester.songinfo.moredetails.presentation

import ayds.observer.Observable
import ayds.observer.Subject
import ayds.winchester.songinfo.moredetails.domain.repository.CardRepository
import ayds.winchester.songinfo.moredetails.domain.entities.Card

interface MoreDetailsPresenter{
    val uiStateObservable: Observable<List<OtherInfoUiState>>
    fun generateCards(cardName: String)
}
internal class MoreDetailsPresenterImpl(
    private val cardRepository: CardRepository,
    private val infoSongFormat: InfoSongFormat,
) : MoreDetailsPresenter {

    private val uiStateSubject = Subject<List<OtherInfoUiState>>()
    override val uiStateObservable: Observable<List<OtherInfoUiState>> = uiStateSubject
    override fun generateCards(cardName: String) {
        Thread {
            val artistCards = cardRepository.getCards(cardName)
            notifyUiState(artistCards)
        }.start()
    }

    private fun notifyUiState(cardsList: List<Card?>) {
        val uiState = createUiState(cardsList)
        uiStateSubject.notify(uiState)
    }
    private fun createUiState(cardsList: List<Card?>): List<OtherInfoUiState> {
        val otherInfoUiStateList: MutableList<OtherInfoUiState> = mutableListOf()

        if(cardsList.isNotEmpty()){
            for(card in cardsList) {
                if (card != null && card is Card.ArtistCard) {
                    val info = infoSongFormat.formatInfoSong(card)
                    otherInfoUiStateList.add(
                        OtherInfoUiState(
                            sourceLogo = card.sourceLogoUrl,
                            description = info,
                            sourceArticleUrl = card.infoUrl,
                            sourceName = card.source.name
                        )
                    )
                }
            }
        }
        return otherInfoUiStateList
    }
}