package ayds.winchester.songinfo.moredetails.data.broker

interface Broker {
}

internal class InfoBroker : Broker{
    val serversList : MutableList<ServerProxy> = mutableListOf()
    val clientsList : MutableList<ClientProxy> = mutableListOf()
}