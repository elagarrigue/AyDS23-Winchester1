package ayds.winchester.songinfo.moredetails.data.local.sqldb

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ayds.winchester.songinfo.moredetails.data.local.WikipediaLocalStorage
import ayds.winchester.songinfo.moredetails.domain.entities.Artist

private const val PREFIX_DATABASE = "[*]"
private const val ID_COLUMN = "id"
private const val ARTIST_COLUMN = "artist"
private const val INFO_COLUMN = "info"
private const val SOURCE_COLUMN = "source"
private const val WIKIPEDIA_URL_COLUMN = "url"
private const val TABLE_ARTIST_NAME = "artists"
private const val TABLE_ARTIST_QUERY = "create table $TABLE_ARTIST_NAME ($ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT, $ARTIST_COLUMN string, $INFO_COLUMN string, $WIKIPEDIA_URL_COLUMN string, $SOURCE_COLUMN integer)"
private const val DB_NAME = "dictionary.db"
private const val DB_VERSION = 1
class WikipediaLocalStorageImpl(context: Context): WikipediaLocalStorage, SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            TABLE_ARTIST_QUERY
        )
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
    override fun getArtistInfoFromDataBase(artistName: String): Artist.WikipediaArtist? {
        return this.getInfo(artistName)
    }
    private fun getInfo(artist: String): Artist.WikipediaArtist? = getArtistInfo(makeQuery(artist))
    private fun makeQuery(artist: String) : Cursor =
        readableDatabase.query(
            TABLE_ARTIST_NAME,
            arrayOf(ID_COLUMN, ARTIST_COLUMN, INFO_COLUMN,WIKIPEDIA_URL_COLUMN),
            "$ARTIST_COLUMN = ?",
            arrayOf(artist),
            null,
            null,
            "$ARTIST_COLUMN  DESC"
        )
    private fun getArtistInfo(cursor: Cursor) : Artist.WikipediaArtist? =
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