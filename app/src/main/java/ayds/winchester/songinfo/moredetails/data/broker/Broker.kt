package ayds.winchester.songinfo.moredetails.data.broker

import ayds.winchester.songinfo.moredetails.domain.entities.Card

interface Broker {

    fun locateServer(): ServerProxy
    fun locateClient(): ClientProxy
    fun registerServer(server: ServerProxy)
    fun unregisterServer(server: ServerProxy)
    fun getArtist(artistName: String): List<Card>
}

internal class artistBroker : Broker{
    private val serversList : MutableList<ServerProxy> = mutableListOf()
    private lateinit var client: ClientProxy
    override fun locateServer(): ServerProxy {
        TODO("Not yet implemented")
    }

    override fun locateClient(): ClientProxy {
        TODO("Not yet implemented")
    }

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