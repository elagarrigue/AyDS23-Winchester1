package ayds.winchester.songinfo.moredetails.data

import ayds.winchester.songinfo.moredetails.data.external.WikipediaTrackService
import ayds.winchester.songinfo.moredetails.data.local.WikipediaLocalStorage
import ayds.winchester.songinfo.moredetails.domain.ArtistRepository
import ayds.winchester.songinfo.moredetails.domain.entities.Artist
import java.io.IOException
import android.content.Intent

class ArtistRepositoryImpl(
    private val wikipediaLocalStorage: WikipediaLocalStorage,
    private val wikipediaTrackService: WikipediaTrackService
): ArtistRepository {

    override fun getArtist(): Artist.WikipediaArtist {
        val artistName = getArtistNameFromIntent()
        val infoSong = wikipediaLocalStorage.getArtistInfoFromDataBase(artistName)
        val artistInfo = infoSong?.let { wikipediaLocalStorage.formatFromDataBase(infoSong) } ?: getArtistInfoFromRepository(artistName)
        val wikipediaUrl = wikipediaTrackService.getArticleUrl(artistName)
        return Artist.WikipediaArtist(
            name = artistName,
            artistInfo = artistInfo,
            wikipediaUrl = wikipediaUrl,
            isInDataBase = true
        )
    }

    private fun getArtistNameFromIntent() = intent.getStringExtra(ARTIST_NAME_EXTRA).toString()

    private fun getArtistInfoFromRepository(artistName: String): String {
        return try {
            wikipediaTrackService.getArtistInfo(artistName)
        } catch (e1: IOException){""}
    }

    private fun saveInDataBase(infoSong: String?, artistName: String){
        dataBase.saveArtist(artistName, infoSong)
    }

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }

}