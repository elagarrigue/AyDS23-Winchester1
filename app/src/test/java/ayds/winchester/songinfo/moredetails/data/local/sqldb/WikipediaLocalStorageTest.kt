package ayds.winchester.songinfo.moredetails.data.local.sqldb

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteOpenHelper
import ayds.winchester.songinfo.moredetails.data.local.WikipediaLocalStorage
import ayds.winchester.songinfo.moredetails.domain.entities.Artist
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert
import org.junit.Test


class WikipediaLocalStorageTest {

    private val context: Context = mockk()
    private val cursorToWikipediaArtistMapper: CursorToWikipediaArtistMapper = mockk()
    private val cursor: Cursor = mockk()
    private val wikipediaLocalStorage: WikipediaLocalStorageImpl by lazy { WikipediaLocalStorageImpl(context, cursorToWikipediaArtistMapper)}

    @Test
    fun `getInfo should return a WikipediaArtist`(){
        val artist: Artist.WikipediaArtist = mockk()
        every{
            cursorToWikipediaArtistMapper.map(
                wikipediaLocalStorage.readableDatabase.query(
                    TABLE_ARTIST_NAME,
                    arrayOf(ID_COLUMN, ARTIST_COLUMN, INFO_COLUMN,WIKIPEDIA_URL_COLUMN),
                    "$ARTIST_COLUMN = ?",
                    arrayOf("name"),
                    null,
                    null,
                    "$ARTIST_COLUMN  DESC"
        )) } returns artist

        val result = wikipediaLocalStorage.getArtistInfoFromDataBase("name")

        Assert.assertEquals(artist, result)
    }

    @Test
    fun `getInfo should return null if the artist doesn't exists`(){
        val result = wikipediaLocalStorage.getArtistInfoFromDataBase("unknow")

        Assert.assertEquals(null, result)
    }
}