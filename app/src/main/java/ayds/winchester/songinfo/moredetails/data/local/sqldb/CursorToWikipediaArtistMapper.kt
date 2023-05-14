package ayds.winchester.songinfo.moredetails.data.local.sqldb

import android.database.Cursor
import ayds.winchester.songinfo.moredetails.domain.entities.Artist

private const val PREFIX_DATABASE = "[*]"

interface CursorToWikipediaArtistMapper{
    fun map(cursor: Cursor): Artist.WikipediaArtist?
}

internal class CursorToWikipediaArtistMapperImpl : CursorToWikipediaArtistMapper{

    override fun map(cursor: Cursor): Artist.WikipediaArtist? =
        with(cursor) {
            if (moveToNext()) {
                Artist.WikipediaArtist(
                    name=getString(cursor.getColumnIndexOrThrow(ARTIST_COLUMN)),
                    artistInfo = formatFromDataBase(getString(cursor.getColumnIndexOrThrow(INFO_COLUMN))),
                    wikipediaUrl = getString(cursor.getColumnIndexOrThrow(WIKIPEDIA_URL_COLUMN)),
                    isInDataBase = true
                )
            } else {
                null
            }
        }

    private fun formatFromDataBase(infoSong: String) = "$PREFIX_DATABASE$infoSong"
}