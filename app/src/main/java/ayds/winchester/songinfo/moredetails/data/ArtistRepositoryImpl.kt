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
    override  fun getArtist(artistName: String): Artist {
        var artist : Artist? = wikipediaLocalStorage.getArtistInfoFromDataBase(artistName)
        if(artist == null){
            artist = wikipediaArticleService.getArtist(artistName)
            wikipediaLocalStorage.saveArtist(artist as Artist.WikipediaArtist)
        }

        return artist
    }

}