package ayds.winchester.songinfo.moredetails.data.local.sqldb

import android.database.Cursor
import ayds.winchester.songinfo.moredetails.domain.entities.Card
import ayds.winchester.songinfo.moredetails.domain.entities.Source

interface CursorToArtistMapper{
    fun map(cursor: Cursor): List<Card.ArtistCard?>
}

internal class CursorToArtistMapperImpl : CursorToArtistMapper{

    override fun map(cursor: Cursor): List<Card.ArtistCard?> {
        val cards = mutableListOf<Card.ArtistCard?>()
        with(cursor) {
            while(moveToNext()) {
                val values = Source.values()
                val sourceInt = getInt(getColumnIndexOrThrow(SOURCE_COLUMN))
                cards.add(
                    Card.ArtistCard(
                        source = values[sourceInt],
                        name = getString(cursor.getColumnIndexOrThrow(ARTIST_COLUMN)),
                        description = getString(cursor.getColumnIndexOrThrow(INFO_COLUMN)),
                        infoUrl = getString(cursor.getColumnIndexOrThrow(SOURCE_URL_COLUMN)),
                        sourceLogoUrl = getString(cursor.getColumnIndexOrThrow(SOURCE_LOGO_URL_COLUMN)),
                        isInDataBase = true,
                    )
                )
            }
        }
        return cards
    }
}