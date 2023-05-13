package ayds.winchester.songinfo.moredetails.data.external.article

import ayds.winchester.songinfo.moredetails.data.external.WikipediaArticleService
import retrofit2.Response

internal class WikipediaArticleServiceImpl(
    private val wikipediaAPI : WikipediaAPI,
    private val wikipediaToArtistResolver : WikipediaToArtistResolver,
): WikipediaArticleService {
    override fun getArtistInfoFromService(artistName: String): Response<String> {
        return wikipediaAPI.getArtistInfo(artistName).execute()
    }
    override fun getArticleUrl(artistName: String): String{
        val callResponse = getArtistInfoFromService(artistName)
        return wikipediaToArtistResolver.getArticleUrl(artistName, callResponse)
    }
    override fun getArtistInfo(artistName: String): String{
        val callResponse = getArtistInfoFromService(artistName)
        return wikipediaToArtistResolver.getArtistInfo(artistName, callResponse)
    }


}