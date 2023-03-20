package ayds.winchester.songinfo.home.model.repository.external.spotify.auth

import android.util.Base64
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Response

private const val clientId = "7db5d794e42845028ccabd50009e631f"
private const val clientSecret = "dc3db8626d86471b92662b72f5eff8ad"

interface SpotifyAccountService {

    val token: String?
}

internal class SpotifyAccountServiceImpl(
  private val spotifyTokenAPI: SpotifyAuthAPI,
  private val resolver: SpotifyToTokenResolver
) : SpotifyAccountService {

    override var token: String? = null
        get() {
            if (field == null) {
                val tokenResponse = tokenFromService
                field = getStringToken(tokenResponse)
            }
            return field
        }
        private set


    private val tokenFromService: Response<String>
        get() = spotifyTokenAPI.getToken(authorizationHeader, authorizationBody).execute()

    private val authorizationBody: RequestBody = RequestBody.create(
      MediaType.parse("application/x-www-form-urlencoded"), "grant_type=client_credentials"
    )

    private val authorizationHeader: String
        get() {
            val encodedData: String =
                Base64.encodeToString("$clientId:$clientSecret".toByteArray(), Base64.NO_WRAP)
            return "Basic $encodedData"
        }

    private fun getStringToken(tokenResponse: Response<String>): String? {
        return resolver.getTokenFromExternalData(tokenResponse.body())
    }
}