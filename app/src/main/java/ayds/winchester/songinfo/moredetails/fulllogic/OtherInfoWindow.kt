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
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.util.*

class OtherInfoWindow : AppCompatActivity() {
    private var textPane2: TextView? = null
    private var dataBase: DataBase? = null
    private val urlString = "https://en.wikipedia.org/?curid="
    private val imageUrl = "https://upload.wikimedia.org/wikipedia/commons/8/8c/Wikipedia-logo-v2-es.png"
    private val baseUrl = "https://en.wikipedia.org/w/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)
        textPane2 = findViewById(R.id.textPane2)
        open(intent.getStringExtra("artistName"))
    }

    fun getArtistInfo(artistName: String?) {
        val retrofit = createRetrofit()
        val wikipediaAPI = retrofit.create(WikipediaAPI::class.java)
        Log.e("TAG", "artistName $artistName")
        Thread {
            var infoSong = DataBase.getInfo(dataBase, artistName)
            infoSong = infoSong?.let { "[*]$it" } ?: infoSongIsNull(infoSong,wikipediaAPI, artistName)

            loadImage(imageUrl)

            setText(infoSong)
        }.start()
    }

    private fun infoSongIsNull(infoSong: String?, wikipediaAPI: WikipediaAPI, artistName: String?): String? {
        var infoSongAux = infoSong
        try {
            val callResponse = wikipediaAPI.getArtistInfo(artistName).execute()
            println("JSON " + callResponse.body())//Hay que imprimirlo?

            val gson = Gson()
            val jobj = getJobj(gson,callResponse)
            val query = getQuery(jobj)
            val snippet = getSnippet(query)
            val pageid = getPageId(query)

            infoSongAux = snippet?.let { getInfoSong(it, artistName) }
            pageid?.let { setListener(it) }

        } catch (e1: IOException) {
            Log.e("TAG", "Error $e1")
            e1.printStackTrace()
        }

        return infoSongAux
    }

    private fun open(artist: String?) {
        dataBase = DataBase(this)
        DataBase.saveArtist(dataBase, "test", "sarasa")
        Log.e("TAG", "" + DataBase.getInfo(dataBase, "test"))
        Log.e("TAG", "" + DataBase.getInfo(dataBase, "nada"))
        getArtistInfo(artist)
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
            .baseUrl(baseUrl)
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

    private fun getInfoSong(snippet: JsonElement, artistName: String?): String {
        var infoSong: String
        if (snippet == null) {
            infoSong = "No Results"
        } else {
            infoSong = formatInfoSong(snippet, artistName)
            saveInDataBase(infoSong, artistName)
        }
        return infoSong
    }

    private fun setListener(pageid: JsonElement){
        val urlStringAux = "$urlString$pageid"
        findViewById<View>(R.id.openUrlButton).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(urlStringAux)
            startActivity(intent)
        }
    }

    private fun loadImage(imageUrl: String) {
        Log.e("TAG", "Get Image from $imageUrl")
        runOnUiThread {
            Picasso.get().load(imageUrl).into(findViewById<View>(R.id.imageView) as ImageView)
        }
    }

    private fun setText(finalText: String) {
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