package ayds.winchester.songinfo.moredetails.presentation

import ayds.winchester.songinfo.moredetails.domain.entities.Artist
import com.google.gson.JsonElement
import java.util.*

private const val HTML_WIDTH = "<html><div width=400>"
private const val HTML_FONT_OPEN = "<font face=\"arial\">"
private const val HTML_FONT_CLOSE = "</font></div></html>"
private const val PREFIX_DATABASE = "[*]"
private const val NO_RESULT = "No Results"

interface InfoSongFormat{
    fun formatInfoSong(artist: Artist): String
    fun formatInfoSong(snippet: JsonElement, artistName: String): String
}
class InfoSongFormatImpl() : InfoSongFormat {
    override fun formatInfoSong(artist: Artist): String {
        return when(artist){
            is Artist.WikipediaArtist ->
                textToHtml(artist.artistInfo, artist.name)
            else -> NO_RESULT
        }
    }
    override fun formatInfoSong(snippet: JsonElement, artistName: String): String {
        var infoSong = snippet.asString.replace("\\n", "\n")
        infoSong = textToHtml(infoSong, artistName)
        return infoSong
    }
    private fun textToHtml(text: String, term: String?): String {
        val text = text.replace("\\n", "\n")
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

}