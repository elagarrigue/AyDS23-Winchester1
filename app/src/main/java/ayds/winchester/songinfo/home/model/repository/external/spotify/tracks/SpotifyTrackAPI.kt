package ayds.winchester.songinfo.home.model.repository.external.spotify.tracks

import retrofit2.Call
import retrofit2.http.*

internal interface SpotifyTrackAPI {

    @GET("search?type=track")
    fun getTrackInfo(
      @Header("Authorization") token: String,
      @Query("q") query: String
    ): Call<String>
}