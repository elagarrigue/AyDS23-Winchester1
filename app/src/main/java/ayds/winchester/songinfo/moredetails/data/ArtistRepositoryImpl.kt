package ayds.winchester.songinfo.moredetails.data

import ayds.winchester.songinfo.moredetails.data.local.WikipediaLocalStorage
import ayds.winchester.songinfo.moredetails.domain.repository.ArtistRepository
import ayds.winchester.songinfo.moredetails.domain.entities.Card
import java.io.IOException
import androidx.appcompat.app.AppCompatActivity
import ayds.winchester.songinfo.moredetails.data.broker.Broker
import wikipedia.external.external.entities.WikipediaArtist
import wikipedia.external.external.WikipediaArticleService

class ArtistRepositoryImpl(
    private val localStorage: WikipediaLocalStorage,
    private val broker: Broker
): ArtistRepository, AppCompatActivity() {
    override fun getArtist(artistName: String): Card {
        var artist = localStorage.getArtistInfoFromDataBase(artistName)
        when {
            artist != null ->  artist.markArtistAsLocal()
            else -> {
                try{
                    val cardList: List<Card> = broker.getArtist(artistName)
                    for (card in cardList){
                       if (card is Card.ArtistCard) localStorage.saveArtist(card)
                    }
                }catch (e1: IOException) {
                }
            }
        }
        return artist ?: Card.EmptyCard
    }
    private fun Card.ArtistCard.markArtistAsLocal() {
        this.isInDataBase = true
    }


}