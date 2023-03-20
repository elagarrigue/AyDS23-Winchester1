package ayds.winchester.songinfo.home.model.repository.local.spotify.sqldb

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ayds.winchester.songinfo.home.model.entities.Song.SpotifySong
import ayds.winchester.songinfo.home.model.repository.local.spotify.SpotifyLocalStorage

private const val DATABASE_VERSION = 1
private const val DATABASE_NAME = "songs.db"

internal class SpotifyLocalStorageImpl(
    context: Context,
    private val cursorToSpotifySongMapper: CursorToSpotifySongMapper,
) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION),
    SpotifyLocalStorage {

    private val projection = arrayOf(
        ID_COLUMN,
        NAME_COLUMN,
        ARTIST_COLUMN,
        ALBUM_COLUMN,
        ALBUM_COLUMN,
        RELEASE_DATE_COLUMN,
        SPOTIFY_URL_COLUMN,
        IMAGE_URL_COLUMN
    )

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(createSongsTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    override fun updateSongTerm(query: String, songId: String) {
        val values = ContentValues().apply {
            put(TERM_COLUMN, query)
        }

        writableDatabase?.update(SONGS_TABLE, values, "$ID_COLUMN LIKE ?", arrayOf(songId))
    }

    override fun insertSong(query: String, song: SpotifySong) {
        val values = ContentValues().apply {
            put(ID_COLUMN, song.id)
            put(TERM_COLUMN, query)
            put(NAME_COLUMN, song.songName)
            put(ARTIST_COLUMN, song.artistName)
            put(ALBUM_COLUMN, song.albumName)
            put(RELEASE_DATE_COLUMN, song.releaseDate)
            put(SPOTIFY_URL_COLUMN, song.spotifyUrl)
            put(IMAGE_URL_COLUMN, song.imageUrl)
        }

        writableDatabase?.insert(SONGS_TABLE, null, values)
    }

    override fun getSongByTerm(term: String): SpotifySong? {
        val cursor = readableDatabase.query(
            SONGS_TABLE,
            projection,
            "$TERM_COLUMN = ?",
            arrayOf(term),
            null,
            null,
            null
        )

        return cursorToSpotifySongMapper.map(cursor)
    }

    override fun getSongById(id: String): SpotifySong? {
        val cursor = readableDatabase.query(
            SONGS_TABLE,
            projection,
            "$ID_COLUMN = ?",
            arrayOf(id),
            null,
            null,
            null
        )

        return cursorToSpotifySongMapper.map(cursor)
    }
}