package ayds.winchester.songinfo.moredetails.presentation

import ayds.winchester.songinfo.moredetails.domain.entities.Artist
import io.mockk.mockk
import org.junit.Assert
import org.junit.Test

class InfoSongFormatTest {

    private val infoSongFormat by lazy { InfoSongFormatImpl() }

    @Test
    fun `given a local wikipedia artist it should return the format description`(){
        val artist: Artist = Artist.WikipediaArtist(
            name = "Adele",
            artistInfo = "<span class=\"searchmatch\">Adele</span> Laurie Blue Adkins MBE (/əˈdɛl/; born 5 May 1988) is an English singer-songwriter. After graduating in arts from the BRIT School in 2006, <span class=\"searchmatch\">Adele</span>",
            wikipediaUrl = "https://en.wikipedia.org/?curid=13041163",
            isInDataBase = true
        )
        val result = infoSongFormat.formatInfoSong(artist)

        val expected =
            "[*]<html><div width=400><font face=\"arial\"><span class=\"searchmatch\"><b>ADELE</b></span> Laurie Blue Adkins MBE (/əˈdɛl/; born 5 May 1988) is an English singer-songwriter. After graduating in arts from the BRIT School in 2006, <span class=\"searchmatch\"><b>ADELE</b></span></font></div></html>"

        Assert.assertEquals(expected, result)
    }

    @Test
    fun `given a non local wikipedia artist it should return the format description`(){
        val artist: Artist = Artist.WikipediaArtist(
            name = "Adele",
            artistInfo = "<span class=\"searchmatch\">Adele</span> Laurie Blue Adkins MBE (/əˈdɛl/; born 5 May 1988) is an English singer-songwriter. After graduating in arts from the BRIT School in 2006, <span class=\"searchmatch\">Adele</span>",
            wikipediaUrl = "https://en.wikipedia.org/?curid=13041163",
            isInDataBase = false
        )
        val result = infoSongFormat.formatInfoSong(artist)

        val expected =
            "<html><div width=400><font face=\"arial\"><span class=\"searchmatch\"><b>ADELE</b></span> Laurie Blue Adkins MBE (/əˈdɛl/; born 5 May 1988) is an English singer-songwriter. After graduating in arts from the BRIT School in 2006, <span class=\"searchmatch\"><b>ADELE</b></span></font></div></html>"

        Assert.assertEquals(expected, result)
    }

    @Test
    fun `given a non wikipedia artist it should return the artist not found description`(){
        val artist: Artist = mockk()

        val result = infoSongFormat.formatInfoSong(artist)

        val expected = "No Results"

        Assert.assertEquals(expected, result)
    }

}