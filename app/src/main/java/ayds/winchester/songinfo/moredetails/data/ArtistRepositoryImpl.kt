package ayds.winchester.songinfo.moredetails.data

import ayds.winchester.songinfo.moredetails.data.external.WikipediaArticleService
import ayds.winchester.songinfo.moredetails.data.local.WikipediaLocalStorage
import ayds.winchester.songinfo.moredetails.domain.repository.ArtistRepository
import ayds.winchester.songinfo.moredetails.domain.entities.Artist
import java.io.IOException
import androidx.appcompat.app.AppCompatActivity

class ArtistRepositoryImpl(
    private val wikipediaLocalStorage: WikipediaLocalStorage,
    private val wikipediaArticleService: WikipediaArticleService
): ArtistRepository {
    override fun getArtist(artistName: String): Artist {
        var artist : Artist.WikipediaArtist? = wikipediaLocalStorage.getArtistInfoFromDataBase(artistName)
        when {
            artist != null ->  artist.markArtistAsLocal()
            else -> {
                try{
                    artist = wikipediaArticleService.getArtist(artistName)
                    wikipediaLocalStorage.saveArtist(artist)
                }catch (e1: Exception) {
                }
            }

        }
        return artist ?: Artist.EmptyArtist
    }
    private fun Artist.WikipediaArtist.markArtistAsLocal() {
        this.isInDataBase = true
    }

}