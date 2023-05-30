package ayds.winchester.songinfo.moredetails.data.broker.proxy.server

import ayds.winchester.songinfo.moredetails.data.broker.ServerProxy
import ayds.winchester.songinfo.moredetails.domain.entities.Card
import ayds.winchester.songinfo.moredetails.domain.entities.Source
import lisboa5lastfm.Artist
import lisboa5lastfm.artist.ArtistExternalService

const val LASTFM_LOGO = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"

internal class LastFMProxy(
    private val lastFMArticleService: ArtistExternalService
) : ServerProxy {

    override fun getArtist(artistName: String): Card {
        val lastFMArtistArtist = lastFMArticleService.getArtistFromLastFMAPI(artistName)
        return toCard(lastFMArtistArtist)
    }

    private fun toCard(lastFMArtist: Artist.ArtistData?): Card {
        return if (lastFMArtist != null)
            Card.ArtistCard(
                name = lastFMArtist.artistName,
                description = lastFMArtist.artistBioContent,
                infoUrl = lastFMArtist.artistURL,
                source = Source.LastFM,
                sourceLogoUrl = LASTFM_LOGO,
                isInDataBase = lastFMArtist.isLocallyStored
            )
        else
            Card.EmptyCard
    }

}