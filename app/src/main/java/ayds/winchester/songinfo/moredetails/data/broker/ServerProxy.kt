package ayds.winchester.songinfo.moredetails.data.broker

import ayds.winchester.songinfo.moredetails.domain.entities.Card

interface ServerProxy : ServiceInterface {
    val server: ServiceInterface
    val broker: Broker

    fun getArtist(artistName: String): Card
}