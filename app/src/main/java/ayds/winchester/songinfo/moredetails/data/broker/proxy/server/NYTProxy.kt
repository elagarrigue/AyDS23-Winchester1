package ayds.winchester.songinfo.moredetails.data.broker.proxy.server

import ayds.NY1.NewYorkTimes.external.NYTArtistInfoService
import ayds.NY1.NewYorkTimes.external.entity.ArtistInformationExternal
import ayds.winchester.songinfo.moredetails.data.broker.ServerProxy
import ayds.winchester.songinfo.moredetails.domain.entities.Card
import ayds.winchester.songinfo.moredetails.domain.entities.Source

const val NYT_LOGO = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVioI832nuYIXqzySD8cOXRZEcdlAj3KfxA62UEC4FhrHVe0f7oZXp3_mSFG7nIcUKhg&usqp=CAU"

internal class NYTProxy(
    private val nytArticleService: NYTArtistInfoService
) : ServerProxy {

    override fun getArtist(artistName: String): Card {
        val nytArtist = nytArticleService.getArtistInfo(artistName)
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
                sourceLogoUrl = NYT_LOGO,
                isInDataBase = nytArtist.isLocallyStored
            )
        else
            Card.EmptyCard
    }

}