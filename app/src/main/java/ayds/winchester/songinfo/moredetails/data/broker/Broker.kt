package ayds.winchester.songinfo.moredetails.data.broker

import ayds.winchester.songinfo.moredetails.domain.entities.Card

interface Broker {

    fun locateServer(): ServerProxy
    fun locateClient(): ClientProxy
    fun registerServer(server: ServerProxy)
    fun unregisterServer(server: ServerProxy)
    fun forwardRequest()
    fun forwardResponse(card: Card)
}

internal class InfoBroker : Broker{
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

    override fun forwardRequest() {
        TODO("Not yet implemented")
    }

    override fun forwardResponse(card: Card){
        TODO("Not yet implemented")
    }
}