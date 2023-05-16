package ayds.winchester.songinfo.moredetails.data.external.article

import ayds.winchester.songinfo.moredetails.data.external.WikipediaArticleService
import ayds.winchester.songinfo.moredetails.domain.entities.Artist
import retrofit2.Response

internal class WikipediaArticleServiceImpl(
    private val wikipediaAPI : WikipediaAPI,
    private val wikipediaToArtistResolver : WikipediaToArtistResolver,
): WikipediaArticleService {

    override fun getArtist(artistName: String): Artist.WikipediaArtist {
        val callResponse = getArtistInfoFromService(artistName)
        return Artist.WikipediaArtist(
            name = artistName,
            artistInfo = getArtistInfo(artistName, callResponse),
            wikipediaUrl = getArticleUrl(artistName, callResponse),
            isInDataBase = false
        )
    }

    private fun getArtistInfoFromService(artistName: String): Response<String> {
        return wikipediaAPI.getArtistInfo(artistName).execute()
    }
    private fun getArtistInfo(artistName: String, callResponse: Response<String>): String{
        return wikipediaToArtistResolver.getArtistInfo(artistName, callResponse)
    }

    private fun getArticleUrl(artistName: String, callResponse: Response<String>): String{
        return wikipediaToArtistResolver.getArticleUrl(artistName, callResponse)
    }


}