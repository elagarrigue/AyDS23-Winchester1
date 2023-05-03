package ayds.winchester.songinfo.moredetails.fulllogic

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ayds.winchester.songinfo.R
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.util.*

private const val WIKIPEDIA_ARTICLE_URL = "https://en.wikipedia.org/?curid="
private const val WIKIPEDIA_LOGO = "https://upload.wikimedia.org/wikipedia/commons/8/8c/Wikipedia-logo-v2-es.png"
private const val WIKIPEDIA_BASE_URL = "https://en.wikipedia.org/w/"
private const val NO_RESULT = "No Results"
private const val QUERY = "query"
private const val SNIPPET = "snippet"
private const val PAGE_ID = "pageid"
private const val SEARCH = "search"
private const val HTML_WIDTH = "<html><div width=400>"
private const val HTML_FONT_OPEN = "<font face=\"arial\">"
private const val HTML_FONT_CLOSE = "</font></div></html>"
private const val PREFIX_DATABASE = "[*]"

class OtherInfoWindow : AppCompatActivity() {
    private lateinit var dataBase : DataBase
    private lateinit var urlButton : Button
    private lateinit var artistInfoTextView : TextView
    private lateinit var imageView : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)
        initProperties()
        generateArtistInfo()
    }

    private fun initProperties(){
        urlButton = findViewById(R.id.openUrlButton)
        artistInfoTextView = findViewById(R.id.textPane2)
        imageView = findViewById(R.id.imageView)
        dataBase = DataBase(this)
    }

    private fun generateArtistInfo() {
        Thread {
            val artist = getArtist()
            displayArtistInfo(artist)
        }.start()
    }

    private fun getArtist(): Artist {
        val artistName = intent.getStringExtra(ARTIST_NAME_EXTRA).toString()
        val infoSong = getArtistInfoFromDataBase(artistName)
        val artistInfo = infoSong?.let { formatFromDataBase(infoSong) } ?: getArtistInfoFromRepository(artistName)
        val wikipediaUrl = getArticleUrl(artistName)
        return Artist(name = artistName, artistInfo = artistInfo, wikipediaUrl = wikipediaUrl,isInDataBase = true)
    }

    private fun getArtistInfoFromDataBase(artistName: String): String? {
        return dataBase.getInfo(artistName)
    }

    private fun formatFromDataBase(infoSong: String) = "$PREFIX_DATABASE$infoSong"


    private fun getArtistInfoFromRepository(artistName: String): String {
        return try {
            getArtistInfo(artistName)
        } catch (e1: IOException){
            e1.printStackTrace()
            ""
        }
    }

    private fun getArtistInfo(artistName: String): String {
        val callResponse = getArtistInfoFromService(artistName)
        val query = getQuery(callResponse)
        val snippet = getSnippet(query)
        return resolveInfoSong(snippet, artistName)
    }

    private fun getArtistInfoFromService(artistName: String): Response<String>{
        val wikipediaAPI = createWikipediaAPI()
        return wikipediaAPI.getArtistInfo(artistName).execute()
    }

    private fun createWikipediaAPI(): WikipediaAPI {
        val retrofit = createRetrofit()
        return retrofit.create(WikipediaAPI::class.java)
    }

    private fun createRetrofit() = Retrofit.Builder().baseUrl(WIKIPEDIA_BASE_URL).addConverterFactory(ScalarsConverterFactory.create()).build()

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

    private fun formatInfoSong(snippet: JsonElement, artistName: String): String {
        var infoSong = snippet.asString.replace("\\n", "\n")
        infoSong = textToHtml(infoSong, artistName)
        return infoSong
    }

    private fun textToHtml(text: String, term: String?): String {
        val builder = StringBuilder()
        builder.append(HTML_WIDTH)
        builder.append(HTML_FONT_OPEN)
        val textWithBold = text
            .replace("'", " ")
            .replace("\n", "<br>")
            .replace(
                "(?i)$term".toRegex(),
                "<b>" + term?.uppercase(Locale.getDefault()) + "</b>"
            )
        builder.append(textWithBold)
        builder.append(HTML_FONT_CLOSE)
        return builder.toString()
    }

    private fun saveInDataBase(infoSong: String?, artistName: String){
        dataBase.saveArtist(artistName, infoSong)
    }

    private fun getArticleUrl(artistName: String): String {
        val callResponse = getArtistInfoFromService(artistName)
        val query = getQuery(callResponse)
        val pageId = getPageId(query)
        return "$WIKIPEDIA_ARTICLE_URL$pageId"
    }

    private fun getPageId(query: JsonObject?): JsonElement? {
        val searchArray = query?.get(SEARCH)?.asJsonArray
        val firstResult = searchArray?.get(0)
        val firstResultObj =  firstResult?.asJsonObject
        return firstResultObj?.get(PAGE_ID)
    }

    private fun displayArtistInfo(artist: Artist) {
        loadImage(WIKIPEDIA_LOGO)
        setText(artist.artistInfo)
        setListener(artist.wikipediaUrl)
    }

    private fun loadImage(imageUrl: String) {
        runOnUiThread {
            Picasso.get().load(imageUrl).into(imageView)
        }
    }

    private fun setText(finalText: String?) {
        runOnUiThread {
            artistInfoTextView.text = Html.fromHtml(finalText)
        }
    }

    private fun setListener(urlString: String){
        urlButton.setOnClickListener {
            openUrlInExternalApp(urlString)
        }
    }

    private fun openUrlInExternalApp(urlString: String){
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(urlString)
        startActivity(intent)
    }

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }
}
data class Artist(
    val name : String,
    var artistInfo : String,
    var wikipediaUrl: String = WIKIPEDIA_BASE_URL,
    var isInDataBase : Boolean = false
)