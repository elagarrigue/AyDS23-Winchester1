package ayds.winchester.songinfo.moredetails.domain.repository

import ayds.winchester.songinfo.moredetails.data.broker.ServiceInterface
import ayds.winchester.songinfo.moredetails.domain.entities.Card

interface ArtistRepository {
    val clientProxy: ServiceInterface
    fun getArtist(artistName: String): Card

}