package ayds.winchester.songinfo.home.model.repository.external.spotify.auth

import com.google.gson.Gson
import com.google.gson.JsonObject

interface SpotifyToTokenResolver {
    fun getTokenFromExternalData(serviceData: String?): String?
}

private const val ACCESS_TOKEN = "access_token"

internal class JsonToTokenResolver : SpotifyToTokenResolver {

    private val gson = Gson()

    override fun getTokenFromExternalData(serviceData: String?): String {
        val tokenResp = gson.fromJson(serviceData, JsonObject::class.java)
        val accessToken = tokenResp[ACCESS_TOKEN]
        return accessToken.asString
    }
}