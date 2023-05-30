package ayds.winchester.songinfo.moredetails.data.broker

import ayds.winchester.songinfo.moredetails.domain.entities.Card

interface Broker {

    fun registerServer(server: ServerProxy)
    fun unregisterServer(server: ServerProxy)
    fun getArtist(artistName: String): List<Card>
}

internal class BrokerImpl : Broker{
    private val serversList : MutableList<ServerProxy> = mutableListOf()

    override fun registerServer(server: ServerProxy) {
        serversList.add(server)
    }

    override fun unregisterServer(server: ServerProxy) {
        serversList.remove(server)
    }

    override fun getArtist(artistName: String): List<Card> {
        val list : MutableList<Card> = mutableListOf()
        for(server in serversList){
            list.add(server.getArtist(artistName))
        }
        return list
    }

}