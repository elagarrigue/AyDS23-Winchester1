package ayds.winchester.songinfo.moredetails.presentation

import ayds.winchester.songinfo.moredetails.domain.entities.Artist
import ayds.winchester.songinfo.moredetails.domain.repository.ArtistRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class MoreDetailsPresenterTest{
    private val artistRepository: ArtistRepository = mockk()
    private val infoSongFormat: InfoSongFormat = mockk()
    private val moreDetailsPresenter: MoreDetailsPresenter = MoreDetailsPresenterImpl(artistRepository, infoSongFormat)

    /*
    Tengo que hacer el test de que se hayan emitidos los UIStates correspondientes
    Debo comparar con Home con el modelo para ver los observers
    Mockk de un lamda y checkear que haya sido llamado
    Con checkear que se haya generado un UIState alcanza
    */
    @Test
    fun `given an artist's name in database it should notify to the UI`(){
        val artist = Artist.WikipediaArtist(
            "name",
            "info",
            "url",
            true
        )
        every{ artistRepository.getArtist("name") } returns artist

        val result = moreDetailsPresenter.generateArtistInfo("name")

        assertEquals(result, artist)
    }
}