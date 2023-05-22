package ayds.winchester.songinfo.moredetails.data.local

import ayds.winchester.songinfo.moredetails.domain.entities.Card

interface WikipediaLocalStorage {
    fun getArtistInfoFromDataBase(artistName: String): Card.ArtistCard?
    fun saveArtist(card: Card.ArtistCard)
}