package ayds.winchester.songinfo.moredetails.data.local

import ayds.winchester.songinfo.moredetails.domain.entities.Card

interface CardLocalStorage {
    fun getCardsFromDataBase(cardName: String): List<Card.ArtistCard?>
    fun saveCard(card: Card.ArtistCard)
}