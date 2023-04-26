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

private const val URL_STRING = "https://en.wikipedia.org/?curid="
private const val IMAGE_URL = "https://upload.wikimedia.org/wikipedia/commons/8/8c/Wikipedia-logo-v2-es.png"
private const val BASE_URL = "https://en.wikipedia.org/w/"
private const val NO_RESULT = "NO Results"

class OtherInfoWindow : AppCompatActivity() {
    private lateinit var textPane2: TextView
    private lateinit var dataBase: DataBase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)
        textPane2 = findViewById(R.id.textPane2)
        open(intent.getStringExtra("artistName"))
    }

    private fun open(artist: String?) {
        dataBase = DataBase(this)
        DataBase.saveArtist(dataBase, "test", "sarasa")
        val retrofit = createRetrofit()
        val wikipediaAPI = retrofit.create(WikipediaAPI::class.java)
        getArtistInfo(artist, wikipediaAPI)
    }

    private fun getArtistInfo(artistName: String?, wikipediaAPI: WikipediaAPI) {
        Thread {
            var infoSong = getInfoFromService(artistName)
            infoSong = infoSong?.let { "[*]$it" } ?: infoSongIsNull(infoSong,wikipediaAPI, artistName)
            loadImage(IMAGE_URL)
            setText(infoSong)
        }.start()
    }

    private fun getInfoFromService(artistName: String?): String? {
        return DataBase.getInfo(dataBase, artistName)
    }

    private fun infoSongIsNull(infoSong: String?, wikipediaAPI: WikipediaAPI, artistName: String?): String? {
        var infoSongAux = infoSong
        try {
            val callResponse = getArtistInfoFromService(wikipediaAPI, artistName)
            val gson = Gson()
            val jobj = getJobj(gson,callResponse)
            val query = getQuery(jobj)
            val snippet = getSnippet(query)
            val pageId = getPageId(query)

            infoSongAux = snippet?.let {
                val infoSong = formatInfoSong(snippet,artistName)
                saveInDataBase(infoSong, artistName)
                infoSong
            } ?: NO_RESULT
            pageId?.let { setListener(it) }

        } catch (e1: IOException) {
            e1.printStackTrace()
        }
        return infoSongAux
    }

    private fun getArtistInfoFromService(wikipediaAPI: WikipediaAPI, artistName: String?): Response<String>{
        return wikipediaAPI.getArtistInfo(artistName).execute()
    }

    private fun formatInfoSong(snippet: JsonElement, artistName: String?): String {
        var infoSong = snippet.asString.replace("\\n", "\n")
        infoSong = textToHtml(infoSong, artistName)
        return infoSong
    }

    private fun saveInDataBase(infoSong: String?, artistName: String?) {
        DataBase.saveArtist(dataBase, artistName, infoSong)
    }

    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
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
        val urlStringAux = "$URL_STRING$pageId"
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
            textPane2!!.text = Html.fromHtml(finalText)
        }
    }

    private fun getJobj(gson: Gson, callResponse: Response<String>): JsonObject? {
        return gson.fromJson(callResponse.body(), JsonObject::class.java)
    }

    private fun getQuery(jobj: JsonObject?): JsonObject? {
        return jobj?.get("query")?.asJsonObject
    }

    private fun getSnippet(query: JsonObject?): JsonElement? {
        return query?.get("search")?.asJsonArray?.get(0)?.asJsonObject?.get("snippet")
    }

    private fun getPageId(query: JsonObject?): JsonElement? {
        return query?.get("search")?.asJsonArray?.get(0)?.asJsonObject?.get("pageid")
    }

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }

}