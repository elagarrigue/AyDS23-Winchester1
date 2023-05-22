package ayds.winchester.songinfo.moredetails.data

import ayds.winchester.songinfo.moredetails.data.local.WikipediaLocalStorage
import ayds.winchester.songinfo.moredetails.domain.repository.ArtistRepository
import ayds.winchester.songinfo.moredetails.domain.entities.Artist
import java.io.IOException
import androidx.appcompat.app.AppCompatActivity
import wikipedia.external.external.entities.WikipediaArtist
import wikipedia.external.external.WikipediaArticleService

class ArtistRepositoryImpl(
    private val wikipediaLocalStorage: WikipediaLocalStorage,
    private val wikipediaArticleService: WikipediaArticleService
): ArtistRepository, AppCompatActivity() {
    override fun getArtist(artistName: String): Artist {
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
        return artist ?: Artist.EmptyArtist
    }
    private fun Artist.WikipediaArtist.markArtistAsLocal() {
        this.isInDataBase = true
    }

    private fun toArtist(wikipediaArtist: WikipediaArtist): Artist.WikipediaArtist {
        return  Artist.WikipediaArtist(
                    wikipediaArtist.name,
                    wikipediaArtist.artistInfo,
                    wikipediaArtist.wikipediaUrl,
                    wikipediaArtist.isInDataBase
                )
    }
}