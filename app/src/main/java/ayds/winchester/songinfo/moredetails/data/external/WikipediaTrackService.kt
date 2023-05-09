package ayds.winchester.songinfo.moredetails.data.external

import retrofit2.Response

interface WikipediaTrackService {
    fun getArticleUrl(artistName: String): String
    fun getArtistInfoFromService(artistName: String): Response<String>
    fun getArtistInfo(artistName: String): String
}