package ayds.winchester.songinfo.moredetails.fulllogic

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.sql.*

class DataBase(context: Context?) : SQLiteOpenHelper(context, "dictionary.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "create table artists (id INTEGER PRIMARY KEY AUTOINCREMENT, artist string, info string, source integer)"
        )
        Log.i("DB", "DB created")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    companion object {
        val TIMEOUT = 30;
        val URL_CONNECTION = "jdbc:sqlite:./dictionary.db"
        val ID_COLUMN = "id"
        val ARTIST_COLUMN = "artist"
        val INFO_COLUMN = "info"
        val SOURCE_COLUMN = "source"
        var artistQuery = "select * from artists"

        fun testDB() {
            try {
                var connection = createConnection(URL_CONNECTION)
                val statement = prepareStatement(connection)
                val rs = executeQuery(statement, artistQuery)
                printResults(rs)
                closeConnection(connection)
            } catch (e: SQLException) {
                System.err.println(e.message)
            }
        }

        fun createConnection(url: String): Connection{
            return DriverManager.getConnection(url);
        }

        fun prepareStatement(connection: Connection) : Statement {
            var statement = connection.createStatement()
            statement.queryTimeout = TIMEOUT
            return statement
        }

        fun executeQuery(statement: Statement, query: String): ResultSet {
            return statement.executeQuery(query)
        }

        fun printResults(rs: ResultSet){
            while (rs.next()) {
                println("id = " + rs.getInt(ID_COLUMN))
                println("artist = " + rs.getString(ARTIST_COLUMN))
                println("info = " + rs.getString(INFO_COLUMN))
                println("source = " + rs.getString(SOURCE_COLUMN))
            }
        }

        fun closeConnection(connection: Connection){
            connection.close()
        }

        @JvmStatic
        fun saveArtist(dbHelper: DataBase, artist: String?, info: String?) {
            // Gets the data repository in write mode
            val db = dbHelper.writableDatabase

// Create a new map of values, where column names are the keys
            val values = ContentValues()
            values.put("artist", artist)
            values.put("info", info)
            values.put("source", 1)

// Insert the new row, returning the primary key value of the new row
            val newRowId = db.insert("artists", null, values)
        }

        @JvmStatic
        fun getInfo(dbHelper: DataBase, artist: String): String? {
            val db = dbHelper.readableDatabase

// Define a projection that specifies which columns from the database
// you will actually use after this query.
            val projection = arrayOf(
                "id",
                "artist",
                "info"
            )

// Filter results WHERE "title" = 'My Title'
            val selection = "artist  = ?"
            val selectionArgs = arrayOf(artist)

// How you want the results sorted in the resulting Cursor
            val sortOrder = "artist DESC"
            val cursor = db.query(
                "artists",  // The table to query
                projection,  // The array of columns to return (pass null to get all)
                selection,  // The columns for the WHERE clause
                selectionArgs,  // The values for the WHERE clause
                null,  // don't group the rows
                null,  // don't filter by row groups
                sortOrder // The sort order
            )
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

    }