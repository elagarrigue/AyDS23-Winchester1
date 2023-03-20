package ayds.winchester.songinfo.home.model

import ayds.winchester.songinfo.home.model.entities.Song
import ayds.winchester.songinfo.home.model.repository.SongRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert
import org.junit.Test

class HomeModelTest {

    private val repository: SongRepository = mockk()

    private val homeModel: HomeModel by lazy {
        HomeModelImpl(repository)
    }

    @Test
    fun `getSongById should return song`() {
        val song: Song = mockk()
        every { repository.getSongById("id") } returns song

        val result = homeModel.getSongById("id")

        Assert.assertEquals(song, result)
    }

    @Test
    fun `on search song it should notify the result`() {
        val song: Song = mockk()
        every { repository.getSongByTerm("term") } returns song
        val songTester: (Song) -> Unit = mockk(relaxed = true)
        homeModel.songObservable.subscribe {
            songTester(it)
        }

        homeModel.searchSong("term")

        verify { songTester(song) }
    }
}