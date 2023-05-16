package ayds.winchester.songinfo.moredetails.data.local.sqldb

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ayds.winchester.songinfo.moredetails.data.local.WikipediaLocalStorage
import ayds.winchester.songinfo.moredetails.domain.entities.Artist

class WikipediaLocalStorageImpl(context: Context, private val cursorToWikipediaArtistMapper: CursorToWikipediaArtistMapper,): WikipediaLocalStorage, SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    private val projection = arrayOf(
        ID_COLUMN,
        ARTIST_COLUMN,
        INFO_COLUMN,
        WIKIPEDIA_URL_COLUMN,
        SOURCE_COLUMN)
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            createArtistTableQuery
        )
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
    override fun getArtistInfoFromDataBase(artistName: String): Artist.WikipediaArtist? {
        return this.getInfo(artistName)
    }
    private fun getInfo(artist: String): Artist.WikipediaArtist? = getArtistInfo(getArtistCursor(artist))

    private fun getArtistInfo(cursor: Cursor) : Artist.WikipediaArtist? {
        return cursorToWikipediaArtistMapper.map(cursor)
    }
    private fun getArtistCursor(artist: String) : Cursor =
        readableDatabase.query(
            TABLE_ARTIST_NAME,
            projection,
            "$ARTIST_COLUMN = ?",
            arrayOf(artist),
            null,
            null,
            "$ARTIST_COLUMN  DESC"
        )
    override fun saveArtist(artist: Artist.WikipediaArtist) {
        writableDatabase.insert(TABLE_ARTIST_NAME, null, getValues(artist))
    }
    private fun getValues(artist: Artist.WikipediaArtist): ContentValues {
        val values = ContentValues()
        values.put(ARTIST_COLUMN, artist.name)
        values.put(INFO_COLUMN, artist.artistInfo)
        values.put(WIKIPEDIA_URL_COLUMN, artist.wikipediaUrl)
        values.put(SOURCE_COLUMN, 1)
        return values
    }
}