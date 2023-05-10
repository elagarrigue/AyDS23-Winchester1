package ayds.winchester.songinfo.moredetails.data.local

import ayds.winchester.songinfo.moredetails.domain.entities.Artist

interface WikipediaLocalStorage {
    fun getArtistInfoFromDataBase(artistName: String): String?
    fun formatFromDataBase(infoSong: String): String
    fun saveArtist(artist: Artist.WikipediaArtist)
}