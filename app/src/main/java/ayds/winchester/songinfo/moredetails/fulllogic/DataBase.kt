package ayds.winchester.songinfo.moredetails.fulllogic

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

private const val ID_COLUMN = "id"
private const val ARTIST_COLUMN = "artist"
private const val INFO_COLUMN = "info"
private const val SOURCE_COLUMN = "source"
private const val TABLE_ARTIST_NAME = "artists"
private const val TABLE_ARTIST_QUERY = "create table $TABLE_ARTIST_NAME ($ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT, $ARTIST_COLUMN string, $INFO_COLUMN string, $SOURCE_COLUMN integer)"
private const val DB_NAME = "dictionary.db"
private const val DB_VERSION = 1

class DataBase(context: Context?) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            TABLE_ARTIST_QUERY
        )
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
    fun saveArtist(artist: String?, info: String?) {
        writableDatabase.insert(TABLE_ARTIST_NAME, null, getValues(artist, info))
    }
    private fun getValues(artist: String?, info: String?) : ContentValues{
        val values = ContentValues()
        values.put(ARTIST_COLUMN, artist)
        values.put(INFO_COLUMN, info)
        values.put(SOURCE_COLUMN, 1)
        return values
    }
    fun getInfo(artist: String): String? = getArtistInfo(makeQuery(artist))
    private fun makeQuery(artist: String) : Cursor =
        readableDatabase.query(
            TABLE_ARTIST_NAME,
            arrayOf(ID_COLUMN, ARTIST_COLUMN, INFO_COLUMN),
            "$ARTIST_COLUMN = ?",
            arrayOf(artist),
            null,
            null,
            "$ARTIST_COLUMN  DESC"
        )
    private fun getArtistInfo(cursor: Cursor) : String? {
        val items: MutableList<String> = ArrayList()
        if(cursor.moveToNext()){
            val info = cursor.getString(
                cursor.getColumnIndexOrThrow(INFO_COLUMN)
            )
            items.add(info)
        }
        cursor.close()
        return items.firstOrNull() ?: null
    }
}