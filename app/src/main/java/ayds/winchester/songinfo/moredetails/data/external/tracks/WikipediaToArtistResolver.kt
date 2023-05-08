package ayds.winchester.songinfo.moredetails.data.external.tracks

import ayds.winchester.songinfo.moredetails.fulllogic.PAGE_ID
import ayds.winchester.songinfo.moredetails.fulllogic.WIKIPEDIA_ARTICLE_URL
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import retrofit2.Response

private const val QUERY = "query"
private const val SNIPPET = "snippet"
private const val SEARCH = "search"
private const val PAGE_ID = "pageid"
private const val NO_RESULT = "No Results"
private const val WIKIPEDIA_ARTICLE_URL = "https://en.wikipedia.org/?curid="
class WikipediaToArtistResolver {

    private fun getArtistInfo(artistName: String): String {
        val callResponse = getArtistInfoFromService(artistName)
        val query = getQuery(callResponse)
        val snippet = getSnippet(query)
        return resolveInfoSong(snippet, artistName)
    }

    private fun getQuery(callResponse: Response<String>): JsonObject? {
        val gson = Gson()
        val jsonObject = fromJsonToJsonObject(gson, callResponse)
        val query = jsonObject?.get(QUERY)
        return query?.asJsonObject
    }

    private fun fromJsonToJsonObject(gson: Gson, callResponse: Response<String>): JsonObject? {
        return gson.fromJson(callResponse.body(), JsonObject::class.java)
    }

    private fun getSnippet(query: JsonObject?): JsonElement? {
        val searchArray = query?.get(SEARCH)?.asJsonArray
        val firstSearchResult = searchArray?.get(0)
        val firstSearchResultObj =  firstSearchResult?.asJsonObject
        return firstSearchResultObj?.get(SNIPPET)
    }

    private fun resolveInfoSong(snippet: JsonElement?, artistName: String): String{
        val infoSong = snippet?.let {
            val infoSong = formatInfoSong(snippet,artistName)
            saveInDataBase(infoSong, artistName)
            infoSong
        } ?: NO_RESULT
        return infoSong
    }

    private fun getArticleUrl(artistName: String): String {
        val callResponse = getArtistInfoFromService(artistName)
        val query = getQuery(callResponse)
        val pageId = getPageId(query)
        return "$WIKIPEDIA_ARTICLE_URL$pageId"
    }

    private fun getPageId(query: JsonObject?): JsonElement? {
        val searchArray = query?.get(ayds.winchester.songinfo.moredetails.fulllogic.SEARCH)?.asJsonArray
        val firstResult = searchArray?.get(0)
        val firstResultObj =  firstResult?.asJsonObject
        return firstResultObj?.get(PAGE_ID)
    }

}