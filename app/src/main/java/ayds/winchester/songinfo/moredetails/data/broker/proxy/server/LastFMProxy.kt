package ayds.winchester.songinfo.moredetails.data.broker.proxy.server

import ayds.winchester.songinfo.moredetails.domain.entities.Card
import ayds.winchester.songinfo.moredetails.domain.entities.Source
import lisboa5lastfm.Artist
import lisboa5lastfm.LASTFM_LOGO_URL
import lisboa5lastfm.artist.ArtistExternalService
import java.io.IOException

internal class LastFMProxy(
    private val lastFMArticleService: ArtistExternalService
) : ServerProxy {

    override fun getCardFormService(cardName: String): Card {
        return try{
            val lastFMArtist = lastFMArticleService.getArtistFromLastFMAPI(cardName)
            toCard(lastFMArtist)
        }catch (e1: IOException) {
            Card.EmptyCard
        }
    }

    private fun toCard(lastFMArtist: Artist.ArtistData?): Card {
        return if (lastFMArtist != null)
            Card.ArtistCard(
                name = lastFMArtist.artistName,
                description = lastFMArtist.artistBioContent,
                infoUrl = lastFMArtist.artistURL,
                source = Source.LastFM,
                sourceLogoUrl = LASTFM_LOGO_URL,
                isInDataBase = lastFMArtist.isLocallyStored
            )
        else
            Card.EmptyCard
    }

}