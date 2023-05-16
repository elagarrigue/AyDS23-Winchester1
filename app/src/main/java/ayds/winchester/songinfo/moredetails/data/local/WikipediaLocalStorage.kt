package ayds.winchester.songinfo.moredetails.data.local

import ayds.winchester.songinfo.moredetails.domain.entities.Artist

interface WikipediaLocalStorage {
    fun getArtistInfoFromDataBase(artistName: String): Artist.WikipediaArtist?
    fun saveArtist(artist: Artist.WikipediaArtist)
}