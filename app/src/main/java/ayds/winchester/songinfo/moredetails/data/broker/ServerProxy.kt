package ayds.winchester.songinfo.moredetails.data.broker

import ayds.winchester.songinfo.moredetails.domain.entities.Card

interface ServerProxy : ServiceInterface {
    val server: ServiceInterface
    val broker: Broker
    fun sendResponse(): Card {
        return broker.forwardResponse()
    }
}

internal class WikipediaProxy(
    override val server: ServiceInterface,
    override val broker: Broker
) : ServerProxy{
    override fun getArtist(artistName: String): Card {
        TODO("Not yet implemented")
    }
}