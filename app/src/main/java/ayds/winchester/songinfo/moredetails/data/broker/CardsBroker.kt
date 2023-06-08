package ayds.winchester.songinfo.moredetails.data.broker

import ayds.winchester.songinfo.moredetails.data.broker.proxy.server.ServerProxy
import ayds.winchester.songinfo.moredetails.domain.entities.Card

interface CardsBroker {

    fun registerServer(server: ServerProxy)
    fun unregisterServer(server: ServerProxy)
    fun getCards(cardName: String): List<Card>
}

internal class CardsBrokerImpl : CardsBroker{
    private val serversList : MutableList<ServerProxy> = mutableListOf()

    override fun registerServer(server: ServerProxy) {
        serversList.add(server)
    }

    override fun unregisterServer(server: ServerProxy) {
        serversList.remove(server)
    }

    override fun getCards(cardName: String): List<Card> {
        val list : MutableList<Card> = mutableListOf()
        for(server in serversList){
            val card = server.getCardFormService(cardName)
            if (card is Card.ArtistCard)
                list.add(card)
        }
        return list
    }

}