package ayds.winchester.songinfo.moredetails.presentation

import ayds.winchester.songinfo.moredetails.domain.entities.Card
import java.util.*

private const val HTML_WIDTH = "<html><div width=400>"
private const val HTML_FONT_OPEN = "<font face=\"arial\">"
private const val HTML_FONT_CLOSE = "</font></div></html>"
private const val PREFIX_DATABASE = "[*]"
private const val NO_RESULT = "No Results"

interface InfoSongFormat{
    fun formatInfoSong(card: Card): String
}
class InfoSongFormatImpl() : InfoSongFormat {

    override fun formatInfoSong(card: Card): String {
        return when(card){
            is Card.ArtistCard ->
                (if (card.isInDataBase) PREFIX_DATABASE else "") +
                        textToHtml(card.description, card.name)

            else -> NO_RESULT
        }
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