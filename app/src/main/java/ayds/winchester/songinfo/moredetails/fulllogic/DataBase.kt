package ayds.winchester.songinfo.moredetails.fulllogic

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class DataBase(context: Context?) : SQLiteOpenHelper(context, "dictionary.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "create table artists (id INTEGER PRIMARY KEY AUTOINCREMENT, artist string, info string, source integer)"
        )
        Log.i("DB", "DB created")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    companion object {
        fun testDB() {
            var connection: Connection? = null
            try {
                // create a database connection
                connection = DriverManager.getConnection("jdbc:sqlite:./dictionary.db")
                val statement = connection.createStatement()
                statement.queryTimeout = 30 // set timeout to 30 sec.

                //statement.executeUpdate("drop table if exists person");
                //statement.executeUpdate("create table person (id integer, name string)");
                //statement.executeUpdate("insert into person values(1, 'leo')");
                //statement.executeUpdate("insert into person values(2, 'yui')");
                val rs = statement.executeQuery("select * from artists")
                while (rs.next()) {
                    // read the result set
                    println("id = " + rs.getInt("id"))
                    println("artist = " + rs.getString("artist"))
                    println("info = " + rs.getString("info"))
                    println("source = " + rs.getString("source"))
                }
            } catch (e: SQLException) {
                // if the error message is "out of memory",
                // it probably means no database file is found
                System.err.println(e.message)
            } finally {
                try {
                    connection?.close()
                } catch (e: SQLException) {
                    // connection close failed.
                    System.err.println(e)
                }
            }
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