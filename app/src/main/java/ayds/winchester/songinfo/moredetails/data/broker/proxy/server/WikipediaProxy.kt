package ayds.winchester.songinfo.moredetails.data.broker.proxy.server

import ayds.winchester.songinfo.moredetails.domain.entities.Card
import ayds.winchester.songinfo.moredetails.domain.entities.Source
import wikipedia.external.external.WikipediaArticleService
import wikipedia.external.external.entities.WIKIPEDIA_LOGO
import wikipedia.external.external.entities.WikipediaArtist
import java.io.IOException

internal class WikipediaProxy(
    private val wikipediaArticleService: WikipediaArticleService
) : ServerProxy {

    override fun getCardFormService(cardName: String): Card {
        return try{
            val wikipediaArtist = wikipediaArticleService.getArtist(cardName)
            toCard(wikipediaArtist)
        }catch (e1: IOException) {
            Card.EmptyCard
        }
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