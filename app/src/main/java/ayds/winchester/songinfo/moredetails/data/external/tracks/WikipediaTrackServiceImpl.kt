package ayds.winchester.songinfo.moredetails.data.external.tracks

import ayds.winchester.songinfo.moredetails.data.external.WikipediaTrackService
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

internal class WikipediaTrackServiceImpl(
    private val wikipediaAPI : WikipediaAPI,
    private val wikipediaToArtistResolver : WikipediaToArtistResolver,
): WikipediaTrackService {
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