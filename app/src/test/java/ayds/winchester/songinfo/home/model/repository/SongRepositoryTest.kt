package ayds.winchester.songinfo.home.model.repository

import ayds.winchester.songinfo.home.model.entities.Song.SpotifySong
import ayds.winchester.songinfo.home.model.entities.Song.EmptySong
import ayds.winchester.songinfo.home.model.repository.external.spotify.SpotifyTrackService
import ayds.winchester.songinfo.home.model.repository.local.spotify.SpotifyLocalStorage
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Test
import java.lang.Exception

class SongRepositoryTest {

    private val spotifyLocalStorage: SpotifyLocalStorage = mockk(relaxUnitFun = true)
    private val spotifyTrackService: SpotifyTrackService = mockk(relaxUnitFun = true)

    private val songRepository: SongRepository by lazy {
        SongRepositoryImpl(spotifyLocalStorage, spotifyTrackService)
    }

    @Test
    fun `given non existing song by id should return empty song`() {
        every { spotifyLocalStorage.getSongById("id") } returns null

        val result = songRepository.getSongById("id")

        assertEquals(EmptySong, result)
    }

    @Test
    fun `given existing song by id should return song`() {
        val song: SpotifySong = mockk()
        every { spotifyLocalStorage.getSongById("id") } returns song

        val result = songRepository.getSongById("id")

        assertEquals(song, result)
    }

    @Test
    fun `given existing song by term should return song and mark it as local`() {
        val song = SpotifySong("id", "name", "artist", "album", "date", "url", "image", false)
        every { spotifyLocalStorage.getSongByTerm("term") } returns song

        val result = songRepository.getSongByTerm("term")

        assertEquals(song, result)
        assertTrue(song.isLocallyStored)
    }

    @Test
    fun `given non existing song by term should get the song and store it`() {
        val song = SpotifySong("id", "name", "artist", "album", "date", "url", "image", false)
        every { spotifyLocalStorage.getSongByTerm("term") } returns null
        every { spotifyTrackService.getSong("term") } returns song
        every { spotifyLocalStorage.getSongById("id") } returns null

        val result = songRepository.getSongByTerm("term")

        assertEquals(song, result)
        assertFalse(song.isLocallyStored)
        verify { spotifyLocalStorage.insertSong("term", song) }
    }

    @Test
    fun `given existing song by different term should get the song and update it`() {
        val song = SpotifySong("id", "name", "artist", "album", "date", "url", "image", false)
        every { spotifyLocalStorage.getSongByTerm("term") } returns null
        every { spotifyTrackService.getSong("term") } returns song
        every { spotifyLocalStorage.getSongById("id") } returns song

        val result = songRepository.getSongByTerm("term")

        assertEquals(song, result)
        assertFalse(song.isLocallyStored)
        verify { spotifyLocalStorage.updateSongTerm("term", "id") }
    }

    @Test
    fun `given non existing song by term should return empty song`() {
        every { spotifyLocalStorage.getSongByTerm("term") } returns null
        every { spotifyTrackService.getSong("term") } returns null

        val result = songRepository.getSongByTerm("term")

        assertEquals(EmptySong, result)
    }

    @Test
    fun `given service exception should return empty song`() {
        every { spotifyLocalStorage.getSongByTerm("term") } returns null
        every { spotifyTrackService.getSong("term") } throws mockk<Exception>()

        val result = songRepository.getSongByTerm("term")

        assertEquals(EmptySong, result)
    }
}