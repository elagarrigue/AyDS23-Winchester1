package ayds.winchester.songinfo.moredetails.presentation

import com.google.gson.JsonElement
import java.util.*

private const val HTML_WIDTH = "<html><div width=400>"
private const val HTML_FONT_OPEN = "<font face=\"arial\">"
private const val HTML_FONT_CLOSE = "</font></div></html>"

interface InfoSongFormat{
    fun formatInfoSong(snippet: JsonElement, artistName: String): String
}
class InfoSongFormatImpl() : InfoSongFormat {

    override fun formatInfoSong(snippet: JsonElement, artistName: String): String {
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

}