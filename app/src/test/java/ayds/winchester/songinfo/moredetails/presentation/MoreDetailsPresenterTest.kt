package ayds.winchester.songinfo.moredetails.presentation

import ayds.winchester.songinfo.moredetails.domain.entities.Artist
import ayds.winchester.songinfo.moredetails.domain.repository.ArtistRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class MoreDetailsPresenterTest{
    private val artistRepository: ArtistRepository = mockk()
    private val infoSongFormat: InfoSongFormat = mockk()
    private val moreDetailsPresenter: MoreDetailsPresenter by lazy{
        MoreDetailsPresenterImpl(artistRepository, infoSongFormat)
    }

    @Test
    fun `on search artist it should notify to the UI`() {
        val artist = Artist.WikipediaArtist(
            "name",
            "info",
            "url",
            true
        )

        every { artistRepository.getArtist("name") } returns artist
        every { infoSongFormat.formatInfoSong(artist) } returns "formated info"
        val otherWindowUiState = OtherInfoUiState(artistInfo = "formated info", wikipediaArticleUrl = "url")

        val otherInfoUiStateTester: (OtherInfoUiState) -> Unit = mockk(relaxed = true)
        moreDetailsPresenter.uiStateObservable.subscribe {
            otherInfoUiStateTester(it)
        }

        moreDetailsPresenter.generateArtistInfo("name")

        verify{ otherInfoUiStateTester(otherWindowUiState) }
    }
}