package ayds.winchester.songinfo.moredetails.data.broker.proxy.server

import ayds.winchester.songinfo.moredetails.data.broker.Broker
import ayds.winchester.songinfo.moredetails.data.broker.ServerProxy
import ayds.winchester.songinfo.moredetails.data.broker.ServiceInterface
import ayds.winchester.songinfo.moredetails.data.local.WikipediaLocalStorage
import ayds.winchester.songinfo.moredetails.domain.entities.Card
import wikipedia.external.external.WikipediaArticleService
import wikipedia.external.external.entities.WikipediaArtist
import java.io.IOException


internal class WikipediaProxy(
    override val server: ServiceInterface,
    // el server en este caso creo que sería el wikipediaArticleService
    // ¿hay que modificar WikipediaArticleService para que implemente ServiceInterface?
    override val broker: Broker,
    private val wikipediaArticleService: WikipediaArticleService,
    private val wikipediaLocalStorage: WikipediaLocalStorage
) : ServerProxy {

    init{
        broker.registerServer(this)
    }
    override fun getArtist(artistName: String): Card {
        var artist = wikipediaLocalStorage.getArtistInfoFromDataBase(artistName)
        when {
            artist != null ->  artist.markArtistAsLocal()
            else -> {
                try{
                    val wikipediaArtist: WikipediaArtist = wikipediaArticleService.getArtist(artistName)
                    artist = toArtist(wikipediaArtist)
                    wikipediaLocalStorage.saveArtist(artist)
                }catch (e1: IOException) {
                }
            }
        }
        return artist ?: Card.EmptyCard
    }
    private fun Card.ArtistCard.markArtistAsLocal() {
        this.isInDataBase = true
    }

    private fun toArtist(wikipediaArtist: WikipediaArtist): Card.ArtistCard {
        return  Card.ArtistCard(
            name = wikipediaArtist.name,
            description = wikipediaArtist.artistInfo,
            infoUrl = wikipediaArtist.wikipediaUrl,
            isInDataBase = wikipediaArtist.isInDataBase
        )
    }
}