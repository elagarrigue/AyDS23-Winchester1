package ayds.winchester.songinfo.moredetails.data.local.sqldb

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ayds.winchester.songinfo.moredetails.data.local.CardLocalStorage
import ayds.winchester.songinfo.moredetails.domain.entities.Card

class CardLocalStorageImpl(context: Context, private val cursorToArtistMapper: CursorToCardMapper): CardLocalStorage, SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    private val projection = arrayOf(
        ID_COLUMN,
        ARTIST_COLUMN,
        INFO_COLUMN,
        SOURCE_URL_COLUMN,
        SOURCE_COLUMN,
        SOURCE_LOGO_URL_COLUMN)
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            createCardTableQuery
        )
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
    override fun getCardsFromDataBase(cardName: String): List<Card.ArtistCard?> {
        return this.getCard(getCardCursor(cardName))
    }

    private fun getCard(cursor: Cursor) : List<Card.ArtistCard?> {
        return cursorToArtistMapper.map(cursor)
    }
    private fun getCardCursor(cardName: String) : Cursor =
        readableDatabase.query(
            TABLE_CARD_NAME,
            projection,
            "$ARTIST_COLUMN = ?",
            arrayOf(cardName),
            null,
            null,
            "$ARTIST_COLUMN  DESC"
        )
    override fun saveCard(card: Card.ArtistCard) {
        println("${card.source.ordinal}")
        writableDatabase.insert(TABLE_CARD_NAME, null, getValues(card))
    }
    private fun getValues(card: Card.ArtistCard): ContentValues {
        val values = ContentValues()
        values.put(ARTIST_COLUMN, card.name)
        values.put(INFO_COLUMN, card.description)
        values.put(SOURCE_URL_COLUMN, card.infoUrl)
        values.put(SOURCE_COLUMN,  card.source.ordinal)
        values.put(SOURCE_LOGO_URL_COLUMN, card.sourceLogoUrl)
        return values
    }
}