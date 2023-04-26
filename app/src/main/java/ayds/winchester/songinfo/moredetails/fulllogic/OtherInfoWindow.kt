package ayds.winchester.songinfo.moredetails.fulllogic

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ayds.winchester.songinfo.R
import ayds.winchester.songinfo.home.model.repository.external.spotify.tracks.SpotifyTrackAPI
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

class OtherInfoWindow : AppCompatActivity() {
    private lateinit var artistInfoTextView: TextView
    private lateinit var dataBase: DataBase
    private lateinit var wikipediaAPI: WikipediaAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)
        artistInfoTextView = findViewById(R.id.textPane2)
        open(intent.getStringExtra("artistName"))
    }

    private fun open(artist: String?) {
        dataBase = DataBase(this)
        dataBase.saveArtist("test", "sarasa")
        createWikipediaAPI()
        getArtistInfo(artist)
    }

    private fun createWikipediaAPI(){
        val retrofit = createRetrofit()
        wikipediaAPI = retrofit.create(WikipediaAPI::class.java)
    }

    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(WIKIPEDIA_BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
    }

    private fun getArtistInfo(artistName: String?) {
        Thread {
            var infoSong = getInfoFromService(artistName)
            infoSong = infoSong?.let { "[*]$it" } ?: infoSongIsNull(artistName)
            loadImage(WIKIPEDIA_LOGO)
            setText(infoSong)
        }.start()
    }

    private fun getInfoFromService(artistName: String?): String? {
        return artistName?.let { dataBase.getInfo(artistName) } ?: null
    }

    private fun infoSongIsNull(artistName: String?): String? {
        var infoSong = ""
        try {
            val callResponse = getArtistInfoFromService(artistName)
            val query = getQuery(callResponse)
            val snippet = getSnippet(query)
            val pageId = getPageId(query)
            infoSong = resolveInfoSong(snippet, artistName)
            pageId?.let { setListener(it) }
        } catch (e1: IOException) {
            e1.printStackTrace()
        }
        return infoSong
    }

    private fun getArtistInfoFromService(artistName: String?): Response<String>{
        return wikipediaAPI.getArtistInfo(artistName).execute()
    }

    private fun resolveInfoSong(snippet: JsonElement?, artistName: String?): String{
        var infoSong = snippet?.let {
            val infoSong = formatInfoSong(snippet,artistName)
            saveInDataBase(infoSong, artistName)
            infoSong
        } ?: NO_RESULT
        return infoSong
    }

    private fun formatInfoSong(snippet: JsonElement, artistName: String?): String {
        var infoSong = snippet.asString.replace("\\n", "\n")
        infoSong = textToHtml(infoSong, artistName)
        return infoSong
    }

    private fun saveInDataBase(infoSong: String?, artistName: String?) {
        dataBase.saveArtist(artistName, infoSong)
    }

    private fun textToHtml(text: String, term: String?): String {
        val builder = StringBuilder()
        builder.append("<html><div width=400>")
        builder.append("<font face=\"arial\">")
        val textWithBold = text
            .replace("'", " ")
            .replace("\n", "<br>")
            .replace(
                "(?i)$term".toRegex(),
                "<b>" + term!!.uppercase(Locale.getDefault()) + "</b>"
            )
        builder.append(textWithBold)
        builder.append("</font></div></html>")
        return builder.toString()
    }

    private fun setListener(pageId: JsonElement){
        val urlStringAux = "$WIKIPEDIA_ARTICLE_URL$pageId"
        findViewById<View>(R.id.openUrlButton).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(urlStringAux)
            startActivity(intent)
        }
    }

    private fun loadImage(imageUrl: String) {
        runOnUiThread {
            Picasso.get().load(imageUrl).into(findViewById<View>(R.id.imageView) as ImageView)
        }
    }

    private fun setText(finalText: String?) {
        runOnUiThread {
            artistInfoTextView.text = Html.fromHtml(finalText)
        }
    }

    private fun getJobj(gson: Gson, callResponse: Response<String>): JsonObject? {
        return gson.fromJson(callResponse.body(), JsonObject::class.java)
    }

    private fun getQuery(callResponse: Response<String>): JsonObject? {
        val gson = Gson()
        val jobj = getJobj(gson,callResponse)
        return jobj?.get(QUERY)?.asJsonObject
    }

    private fun getSnippet(query: JsonObject?): JsonElement? {
        return query?.get(SEARCH)?.asJsonArray?.get(0)?.asJsonObject?.get(SNIPPET)
    }

    private fun getPageId(query: JsonObject?): JsonElement? {
        return query?.get(SEARCH)?.asJsonArray?.get(0)?.asJsonObject?.get(PAGE_ID)
    }

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }
}