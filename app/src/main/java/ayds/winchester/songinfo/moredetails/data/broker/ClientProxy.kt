package ayds.winchester.songinfo.moredetails.data.broker

import ayds.winchester.songinfo.moredetails.domain.entities.Card

interface ClientProxy : ServiceInterface {
    val broker: Broker

    fun sendRequest() {
        broker.forwardRequest()
    }
}

internal class ClientProxyImp(
    override val broker: Broker
) : ClientProxy{

}