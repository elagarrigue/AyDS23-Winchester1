package ayds.winchester.songinfo.moredetails.data.broker

import ayds.winchester.songinfo.moredetails.domain.entities.Card

interface ServerProxy {
    fun getArtist(artistName: String): Card
}