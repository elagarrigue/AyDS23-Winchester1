package ayds.winchester.songinfo.home.model.repository.external.spotify.tracks

import ayds.winchester.songinfo.home.model.repository.external.spotify.SpotifyTrackService
import ayds.winchester.songinfo.home.model.repository.external.spotify.auth.SpotifyAuthInjector
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

object SpotifyTrackInjector {

    private const val SPOTIFY_URL = "https://api.spotify.com/v1/"
    private val spotifyAPIRetrofit = Retrofit.Builder()
        .baseUrl(SPOTIFY_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()
    private val spotifyTrackAPI = spotifyAPIRetrofit.create(SpotifyTrackAPI::class.java)
    private val spotifyToSongResolver: SpotifyToSongResolver = JsonToSongResolver()


    val spotifyTrackService: SpotifyTrackService = SpotifyTrackServiceImpl(
      spotifyTrackAPI,
      SpotifyAuthInjector.spotifyAccountService,
      spotifyToSongResolver
    )
}