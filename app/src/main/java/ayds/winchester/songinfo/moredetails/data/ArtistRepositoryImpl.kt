package ayds.winchester.songinfo.moredetails.data

import ayds.winchester.songinfo.moredetails.data.external.WikipediaArticleService
import ayds.winchester.songinfo.moredetails.data.local.WikipediaLocalStorage
import ayds.winchester.songinfo.moredetails.domain.repository.ArtistRepository
import ayds.winchester.songinfo.moredetails.domain.entities.Artist
import java.io.IOException
import androidx.appcompat.app.AppCompatActivity

private const val NO_RESULT = "No Results"

class ArtistRepositoryImpl(
    private val wikipediaLocalStorage: WikipediaLocalStorage,
    private val wikipediaArticleService: WikipediaArticleService
): ArtistRepository, AppCompatActivity() {

    override fun getArtist(artistName: String): Artist {
        var artist : Artist.WikipediaArtist? = wikipediaLocalStorage.getArtistInfoFromDataBase(artistName)
        when {
            artist != null ->  artist.markArtistAsLocal()
            else -> {
                try{
                    artist = wikipediaArticleService.getArtist(artistName)
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

}