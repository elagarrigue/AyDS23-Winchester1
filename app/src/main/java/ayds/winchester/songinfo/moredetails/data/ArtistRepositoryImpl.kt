package ayds.winchester.songinfo.moredetails.data

import ayds.winchester.songinfo.moredetails.data.external.WikipediaTrackService
import ayds.winchester.songinfo.moredetails.data.local.WikipediaLocalStorage
import ayds.winchester.songinfo.moredetails.domain.ArtistRepository
import ayds.winchester.songinfo.moredetails.domain.entities.Artist
import java.io.IOException
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

private const val NO_RESULT = "No Results"

class ArtistRepositoryImpl(
    private val wikipediaLocalStorage: WikipediaLocalStorage,
    private val wikipediaTrackService: WikipediaTrackService
): ArtistRepository, AppCompatActivity() {
    override  fun getArtist(artistName: String): Artist.WikipediaArtist{
        var artist = wikipediaLocalStorage.getArtistInfoFromDataBase(artistName)
        if(artist == null){
            val artistInfo = getArtistInfoFromExternal(artistName)
            val wikipediaUrl = wikipediaTrackService.getArticleUrl(artistName)
            artist = Artist.WikipediaArtist(
                name = artistName,
                artistInfo = artistInfo,
                wikipediaUrl = wikipediaUrl,
                isInDataBase = true
            )
            wikipediaLocalStorage.saveArtist(artist)
        }

        return artist
    }
    private fun getArtistInfoFromExternal(artistName: String): String {
        return try {
            wikipediaTrackService.getArtistInfo(artistName)
        } catch (e1: IOException){""}
    }


}