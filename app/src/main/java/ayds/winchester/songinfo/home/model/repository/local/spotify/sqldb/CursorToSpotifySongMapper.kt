package ayds.winchester.songinfo.home.model.repository.local.spotify.sqldb

import android.database.Cursor
import ayds.winchester.songinfo.home.model.entities.Song.SpotifySong
import java.sql.SQLException

interface CursorToSpotifySongMapper {

    fun map(cursor: Cursor): SpotifySong?
}

internal class CursorToSpotifySongMapperImpl : CursorToSpotifySongMapper {

    override fun map(cursor: Cursor): SpotifySong? =
        try {
            with(cursor) {
                if (moveToNext()) {
                    SpotifySong(
                      id = getString(getColumnIndexOrThrow(ID_COLUMN)),
                      songName = getString(getColumnIndexOrThrow(NAME_COLUMN)),
                      artistName = getString(getColumnIndexOrThrow(ARTIST_COLUMN)),
                      albumName = getString(getColumnIndexOrThrow(ALBUM_COLUMN)),
                      releaseDate = getString(getColumnIndexOrThrow(RELEASE_DATE_COLUMN)),
                      spotifyUrl = getString(getColumnIndexOrThrow(SPOTIFY_URL_COLUMN)),
                      imageUrl = getString(getColumnIndexOrThrow(IMAGE_URL_COLUMN)),
                    )
                } else {
                    null
                }
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            null
        }
}