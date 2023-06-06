package ayds.winchester.songinfo.moredetails.data

import ayds.winchester.songinfo.moredetails.data.local.CardLocalStorage
import ayds.winchester.songinfo.moredetails.domain.repository.CardRepository
import ayds.winchester.songinfo.moredetails.domain.entities.Card
import java.io.IOException
import androidx.appcompat.app.AppCompatActivity
import ayds.winchester.songinfo.moredetails.data.broker.CardsBroker

class CardRepositoryImpl(
    private val localStorage: CardLocalStorage,
    private val broker: CardsBroker
): CardRepository, AppCompatActivity() {
    override fun getCards(cardName: String): List<Card?> {
        var cardList: List<Card?> = localStorage.getCardsFromDataBase(cardName)
        when {
                !cardList.isEmpty() ->{
                    for (card in cardList)
                        if (card is Card.ArtistCard) card.markCardAsLocal()}
                else -> {
                    try{
                        cardList = broker.getCards(cardName)
                        for (card in cardList){
                           if (card is Card.ArtistCard) localStorage.saveCard(card)
                        }
                    }catch (e1: IOException) {
                    }
            }
        }
        return cardList
    }
    private fun Card.ArtistCard.markCardAsLocal() {
        this.isInDataBase = true
    }

}