package ayds.winchester.songinfo.moredetails.data.local.sqldb

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ayds.winchester.songinfo.moredetails.data.local.ArtistLocalStorage
import ayds.winchester.songinfo.moredetails.domain.entities.Card

class ArtistLocalStorageImpl(context: Context, private val cursorToArtistMapper: CursorToArtistMapper): ArtistLocalStorage, SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    private val projection = arrayOf(
        ID_COLUMN,
        ARTIST_COLUMN,
        INFO_COLUMN,
        SOURCE_URL_COLUMN,
        SOURCE_LOGO_URL_COLUMN,
        SOURCE_COLUMN)
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            createArtistTableQuery
        )
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
    override fun getArtistInfoFromDataBase(artistName: String): List<Card.ArtistCard?> {
        return this.getInfo(artistName)
    }
    private fun getInfo(artist: String): List<Card.ArtistCard?> = getArtistInfo(getArtistCursor(artist))

    private fun getArtistInfo(cursor: Cursor) : List<Card.ArtistCard?> {
        return cursorToArtistMapper.map(cursor)
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
    override fun saveArtist(card: Card.ArtistCard) {
        writableDatabase.insert(TABLE_ARTIST_NAME, null, getValues(card))
    }
    private fun getValues(card: Card.ArtistCard): ContentValues {
        val values = ContentValues()
        values.put(ARTIST_COLUMN, card.name)
        values.put(INFO_COLUMN, card.description)
        values.put(SOURCE_URL_COLUMN, card.infoUrl)
        values.put(SOURCE_LOGO_URL_COLUMN, card.sourceLogoUrl)
        values.put(SOURCE_COLUMN,  card.source.ordinal)
        return values
    }
}