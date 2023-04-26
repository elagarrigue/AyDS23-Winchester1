package ayds.winchester.songinfo.moredetails.fulllogic

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.sql.*

private const val TIMEOUT = 30
private const val URL_CONNECTION = "jdbc:sqlite:./dictionary.db"
private const val ID_COLUMN = "id"
private const val ARTIST_COLUMN = "artist"
private const val INFO_COLUMN = "info"
private const val SOURCE_COLUMN = "source"
private const val TABLE_ARTIST_NAME = "artists"
private const val artistQuery = "select * from artists"
class DataBase(context: Context?) : SQLiteOpenHelper(context, "dictionary.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "create table artists (id INTEGER PRIMARY KEY AUTOINCREMENT, artist string, info string, source integer)"
        )
        Log.i("DB", "DB created")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
        fun testDB() {
            try {
                val connection = createConnection(URL_CONNECTION)
                val statement = prepareStatement(connection)
                val rs = executeQuery(statement, artistQuery)
                printResults(rs)
                closeConnection(connection)
            } catch (e: SQLException) {
                System.err.println(e.message)
            }
        }

        private fun createConnection(url: String): Connection{
            return DriverManager.getConnection(url)
        }

        private fun prepareStatement(connection: Connection) : Statement {
            val statement = connection.createStatement()
            statement.queryTimeout = TIMEOUT
            return statement
        }

        private fun executeQuery(statement: Statement, query: String): ResultSet {
            return statement.executeQuery(query)
        }

        private fun printResults(rs: ResultSet){
            while (rs.next()) {
                println("id = " + rs.getInt(ID_COLUMN))
                println("artist = " + rs.getString(ARTIST_COLUMN))
                println("info = " + rs.getString(INFO_COLUMN))
                println("source = " + rs.getString(SOURCE_COLUMN))
            }
        }

        private fun closeConnection(connection: Connection){
            connection.close()
        }


        fun saveArtist(dbHelper: DataBase, artist: String?, info: String?) {
            dbHelper.writableDatabase.insert(TABLE_ARTIST_NAME, null, getValues(artist, info))
        }

        private fun getValues(artist: String?, info: String?) : ContentValues{
            val values = ContentValues()
            values.put(ARTIST_COLUMN, artist)
            values.put(INFO_COLUMN, info)
            values.put(SOURCE_COLUMN, 1)

            return values
        }

        fun getInfo(dbHelper: DataBase, artist: String): String? = getArtistInfo(makeQuery(artist))

        private fun makeQuery(artist: String) : Cursor =
            this.readableDatabase.query(
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
            while (cursor.moveToNext()) {
                val info = cursor.getString(
                    cursor.getColumnIndexOrThrow("info")
                )
                items.add(info)
            }
            cursor.close()
            return if (items.isEmpty()) null else items[0]
        }
    }