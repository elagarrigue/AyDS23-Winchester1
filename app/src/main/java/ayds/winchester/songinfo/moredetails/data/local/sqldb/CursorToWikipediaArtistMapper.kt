package ayds.winchester.songinfo.moredetails.data.local.sqldb

import android.database.Cursor
import ayds.winchester.songinfo.moredetails.domain.entities.Card
interface CursorToWikipediaArtistMapper{
    fun map(cursor: Cursor): Card.ArtistCard?
}

internal class CursorToWikipediaArtistMapperImpl : CursorToWikipediaArtistMapper{

    override fun map(cursor: Cursor): Card.ArtistCard? =
        with(cursor) {
            if (moveToNext()) {
                Card.ArtistCard(
                    name=getString(cursor.getColumnIndexOrThrow(ARTIST_COLUMN)),
                    description = getString(cursor.getColumnIndexOrThrow(INFO_COLUMN)),
                    infoUrl = getString(cursor.getColumnIndexOrThrow(WIKIPEDIA_URL_COLUMN)),
                    isInDataBase = true
                )
            } else {
                null
            }
        }

}