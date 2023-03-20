package ayds.winchester.songinfo.home.model.repository.external.spotify.auth

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

object SpotifyAuthInjector {

    private const val SPOTIFY_ACCOUNTS_URL = "https://accounts.spotify.com/api/"
    private val spotifyAccountRetrofit = Retrofit.Builder()
        .baseUrl(SPOTIFY_ACCOUNTS_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()
    private val spotifyTokenAPI = spotifyAccountRetrofit.create(SpotifyAuthAPI::class.java)
    private val spotifyToTokenResolver: SpotifyToTokenResolver = JsonToTokenResolver()

    val spotifyAccountService: SpotifyAccountService =
        SpotifyAccountServiceImpl(spotifyTokenAPI, spotifyToTokenResolver)

}