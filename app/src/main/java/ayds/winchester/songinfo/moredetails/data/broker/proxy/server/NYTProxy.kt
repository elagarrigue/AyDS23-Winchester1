package ayds.winchester.songinfo.moredetails.data.broker.proxy.server

import ayds.ny1.newyorktimes.NYTArtistInfoService
import ayds.ny1.newyorktimes.entity.ArtistInformationExternal
import ayds.ny1.newyorktimes.entity.LOGO_URL
import ayds.winchester.songinfo.moredetails.domain.entities.Card
import ayds.winchester.songinfo.moredetails.domain.entities.Source

internal class NYTProxy(
    private val nytArticleService: NYTArtistInfoService
) : ServerProxy {

    override fun getCardFormService(cardName: String): Card {
        val nytArtist = nytArticleService.getArtistInfo(cardName)
        return if (nytArtist is ArtistInformationExternal.ArtistInformationDataExternal)
            toCard(nytArtist)
        else
            Card.EmptyCard
    }

    private fun toCard(nytArtist: ArtistInformationExternal.ArtistInformationDataExternal?): Card {
        return if (nytArtist != null)
            Card.ArtistCard(
                name = nytArtist.artistName,
                description = nytArtist.abstract ?: "",
                infoUrl = nytArtist.url ?: "",
                source = Source.NYT,
                sourceLogoUrl = LOGO_URL,
                isInDataBase = nytArtist.isLocallyStored
            )
        else
            Card.EmptyCard
    }

}