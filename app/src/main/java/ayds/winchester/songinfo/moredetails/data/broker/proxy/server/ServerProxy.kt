package ayds.winchester.songinfo.moredetails.data.broker.proxy.server

import ayds.winchester.songinfo.moredetails.domain.entities.Card

interface ServerProxy {
    fun getCardFormService(cardName: String): Card
}