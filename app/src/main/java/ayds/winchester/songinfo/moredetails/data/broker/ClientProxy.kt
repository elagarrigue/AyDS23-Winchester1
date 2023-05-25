package ayds.winchester.songinfo.moredetails.data.broker

import ayds.winchester.songinfo.moredetails.domain.entities.Card

interface ClientProxy : ServiceInterface {
    val broker: Broker

    fun getArtist(artistName: String): List<Card>{
        return this.sendRequest(artistName)
    }

    fun sendRequest(artistName: String): List<Card> {
        return broker.forwardRequest()
    }
}

internal class ClientProxyImp(
    override val broker: Broker
) : ClientProxy{

}