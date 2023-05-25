package ayds.winchester.songinfo.moredetails.data

import ayds.winchester.songinfo.moredetails.data.local.WikipediaLocalStorage
import ayds.winchester.songinfo.moredetails.domain.repository.ArtistRepository
import ayds.winchester.songinfo.moredetails.domain.entities.Card
import java.io.IOException
import androidx.appcompat.app.AppCompatActivity
import ayds.winchester.songinfo.moredetails.data.broker.ClientProxy
import wikipedia.external.external.entities.WikipediaArtist
import wikipedia.external.external.WikipediaArticleService

class ArtistRepositoryImpl(
    override val clientProxy: ClientProxy,
    private val wikipediaLocalStorage: WikipediaLocalStorage,
    private val wikipediaArticleService: WikipediaArticleService
): ArtistRepository, AppCompatActivity() {
    override fun getArtist(artistName: String): Card {
        var artist = wikipediaLocalStorage.getArtistInfoFromDataBase(artistName)
        when {
            artist != null ->  artist.markArtistAsLocal()
            else -> {
                try{
                    val wikipediaArtist: WikipediaArtist = wikipediaArticleService.getArtist(artistName)
                    artist = toArtist(wikipediaArtist)
                    wikipediaLocalStorage.saveArtist(artist)
                }catch (e1: IOException) {
                }
            }
        }
        return artist ?: Card.EmptyCard
    }
    private fun Card.ArtistCard.markArtistAsLocal() {
        this.isInDataBase = true
    }

    private fun toArtist(wikipediaArtist: WikipediaArtist): Card.ArtistCard {
        return  Card.ArtistCard(
                    name = wikipediaArtist.name,
                    description = wikipediaArtist.artistInfo,
                    infoUrl = wikipediaArtist.wikipediaUrl,
                    isInDataBase = wikipediaArtist.isInDataBase
                )
    }
}