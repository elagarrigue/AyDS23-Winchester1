package ayds.winchester.songinfo.moredetails.data.broker.proxy.server

import ayds.winchester.songinfo.moredetails.data.broker.Broker
import ayds.winchester.songinfo.moredetails.data.broker.ServerProxy
import ayds.winchester.songinfo.moredetails.data.broker.ServiceInterface
import ayds.winchester.songinfo.moredetails.domain.entities.Card
import ayds.winchester.wikipedia.external.external.WikipediaInjector


internal class WikipediaProxy(
    override val server: ServiceInterface,
    override val broker: Broker
) : ServerProxy {
    private val wikipediaArticleService: WikipediaInjector
    override fun getArtist(artistName: String): List<Card> {
        TODO("Not yet implemented")
    }

    override fun sendResponse(){
        broker.forwardResponse(getWikipediaArtist())
    }
    private fun getWikipediaArtist(artistName: String): Card {
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