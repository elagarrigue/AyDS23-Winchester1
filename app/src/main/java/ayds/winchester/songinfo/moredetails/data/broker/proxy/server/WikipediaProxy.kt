package ayds.winchester.songinfo.moredetails.data.broker.proxy.server

import ayds.winchester.songinfo.moredetails.data.broker.ServerProxy
import ayds.winchester.songinfo.moredetails.domain.entities.Card
import ayds.winchester.songinfo.moredetails.domain.entities.Source
import wikipedia.external.external.WikipediaArticleService
import wikipedia.external.external.entities.WikipediaArtist

const val WIKIPEDIA_LOGO = "https://upload.wikimedia.org/wikipedia/commons/8/8c/Wikipedia-logo-v2-es.png"
internal class WikipediaProxy(
    private val wikipediaArticleService: WikipediaArticleService
) : ServerProxy {

    override fun getArtist(artistName: String): Card {
        val wikipediaArtist = wikipediaArticleService.getArtist(artistName)
        return toCard(wikipediaArtist)
    }

    private fun toCard(wikipediaArtist: WikipediaArtist): Card.ArtistCard {
        return  Card.ArtistCard(
            name = wikipediaArtist.name,
            description = wikipediaArtist.artistInfo,
            infoUrl = wikipediaArtist.wikipediaUrl,
            source = Source.Wikipedia,
            sourceLogoUrl = WIKIPEDIA_LOGO,
            isInDataBase = wikipediaArtist.isInDataBase
        )
    }

}