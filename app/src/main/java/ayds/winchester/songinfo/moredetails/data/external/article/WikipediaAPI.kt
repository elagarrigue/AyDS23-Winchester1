package ayds.winchester.songinfo.moredetails.data.external.article

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WikipediaAPI {
    @GET("api.php?action=query&list=search&utf8=&format=json&srlimit=1")
    fun getArtistInfo(@Query("srsearch") artist: String): Call<String>
}