package ayds.winchester.songinfo.moredetails.domain.repository

import ayds.winchester.songinfo.moredetails.domain.entities.Card

interface CardRepository {
    fun getCards(cardName: String): List<Card?>

}