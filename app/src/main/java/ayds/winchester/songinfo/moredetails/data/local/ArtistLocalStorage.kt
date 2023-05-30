package ayds.winchester.songinfo.moredetails.data.local

import ayds.winchester.songinfo.moredetails.domain.entities.Card

interface ArtistLocalStorage {
    fun getArtistInfoFromDataBase(artistName: String): List<Card.ArtistCard?>
    fun saveArtist(card: Card.ArtistCard)
}