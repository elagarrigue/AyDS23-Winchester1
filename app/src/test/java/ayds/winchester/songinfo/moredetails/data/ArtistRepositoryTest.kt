package ayds.winchester.songinfo.moredetails.data

import ayds.winchester.songinfo.moredetails.data.external.WikipediaArticleService
import ayds.winchester.songinfo.moredetails.data.local.WikipediaLocalStorage
import ayds.winchester.songinfo.moredetails.domain.entities.Artist
import ayds.winchester.songinfo.moredetails.domain.repository.ArtistRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Test
import java.lang.Exception

class ArtistRepositoryTest {

    private val wikipediaLocalStorage: WikipediaLocalStorage = mockk(relaxUnitFun = true)
    private val wikipediaArticleService: WikipediaArticleService = mockk(relaxUnitFun = true)

    private val artistRepository: ArtistRepository by lazy {
        ArtistRepositoryImpl(wikipediaLocalStorage, wikipediaArticleService)
    }

    @Test
    fun `given existing artist by term should return artist and mark it as local`() {
        val artist = Artist.WikipediaArtist("name", "info", "artistURL", false)
        every { wikipediaLocalStorage.getArtistInfoFromDataBase("name") } returns artist

        val result = artistRepository.getArtist("name")

        assertEquals(artist, result)
        assertTrue(artist.isInDataBase)
    }

    @Test
    fun `given non existing artist by name should get the artist and store it`() {
        val artist= Artist.WikipediaArtist("name", "info", "artistUrl",  false)
        every { wikipediaLocalStorage.getArtistInfoFromDataBase("name") } returns null
        every { wikipediaArticleService.getArtist("name") } returns artist

        val result = artistRepository.getArtist("name")

        assertEquals(artist, result)
        assertFalse(artist.isInDataBase)
        verify { wikipediaLocalStorage.saveArtist(artist)}
    }

    @Test
    fun `given service exception should return empty artist`() {
        every { wikipediaLocalStorage.getArtistInfoFromDataBase("name") } returns null
        every { wikipediaArticleService.getArtist("name") } throws mockk<Exception>()

        val result = artistRepository.getArtist("name")

        assertEquals(Artist.EmptyArtist, result)
    }
}