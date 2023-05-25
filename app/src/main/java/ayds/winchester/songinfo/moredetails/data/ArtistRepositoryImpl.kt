package ayds.winchester.songinfo.moredetails.data

import ayds.winchester.songinfo.moredetails.data.local.WikipediaLocalStorage
import ayds.winchester.songinfo.moredetails.domain.repository.ArtistRepository
import ayds.winchester.songinfo.moredetails.domain.entities.Card
import java.io.IOException
import androidx.appcompat.app.AppCompatActivity
import ayds.winchester.songinfo.moredetails.data.broker.ClientProxy
import wikipedia.external.external.entities.WikipediaArtist
import wikipedia.external.external.WikipediaArticleService

class ArtistRepositoryImpl(
    private val clientProxy: ClientProxy
): ArtistRepository, AppCompatActivity() {
    private var index = 0
    override fun getArtist(artistName: String): Card {
        val resultList = clientProxy.getArtist(artistName)
        val result = resultList[index]
        index = (index + 1) % resultList.size
        return result
    }
}