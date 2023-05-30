package ayds.winchester.songinfo.moredetails.data

import ayds.winchester.songinfo.moredetails.data.local.ArtistLocalStorage
import ayds.winchester.songinfo.moredetails.domain.repository.ArtistRepository
import ayds.winchester.songinfo.moredetails.domain.entities.Card
import java.io.IOException
import androidx.appcompat.app.AppCompatActivity
import ayds.winchester.songinfo.moredetails.data.broker.Broker

class ArtistRepositoryImpl(
    private val localStorage: ArtistLocalStorage,
    private val broker: Broker
): ArtistRepository, AppCompatActivity() {
    override fun getArtist(artistName: String): List<Card?> {
        var cardList: List<Card?> = localStorage.getArtistInfoFromDataBase(artistName)
        when {
                cardList != null ->
                    for (card in cardList)
                        if (card is Card.ArtistCard) card.markArtistAsLocal()
                else -> {
                    try{
                        cardList = broker.getArtist(artistName)
                        for (card in cardList){
                           if (card is Card.ArtistCard) localStorage.saveArtist(card)
                        }
                    }catch (e1: IOException) {
                    }
            }
        }
        return cardList
    }
    private fun Card.ArtistCard.markArtistAsLocal() {
        this.isInDataBase = true
    }


}