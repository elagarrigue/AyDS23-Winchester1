package ayds.winchester.songinfo.moredetails.injector

import android.content.Context
import ayds.winchester.songinfo.moredetails.data.ArtistRepositoryImpl
import ayds.winchester.songinfo.moredetails.data.external.WikipediaTrackService
import ayds.winchester.songinfo.moredetails.data.external.tracks.WikipediaAPI
import ayds.winchester.songinfo.moredetails.data.external.tracks.WikipediaToArtistResolver
import ayds.winchester.songinfo.moredetails.data.external.tracks.WikipediaToArtistResolverImpl
import ayds.winchester.songinfo.moredetails.data.external.tracks.WikipediaTrackServiceImpl
import ayds.winchester.songinfo.moredetails.data.local.WikipediaLocalStorage
import ayds.winchester.songinfo.moredetails.data.local.sqldb.WikipediaLocalStorageImpl
import ayds.winchester.songinfo.moredetails.domain.ArtistRepository
import ayds.winchester.songinfo.moredetails.presentation.*
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val WIKIPEDIA_BASE_URL = "https://en.wikipedia.org/w/"
object MoreDetailsInjector {

    private lateinit var artistRepository : ArtistRepository
    private lateinit var presenter : MoreDetailsPresenter

    fun init(otherInfoView: OtherInfoView){
        initRepository(otherInfoView)
        initPresenter(otherInfoView)
    }

    private fun initRepository(otherInfoView: OtherInfoView){
        val wikipediaLocalStorage: WikipediaLocalStorage = WikipediaLocalStorageImpl(otherInfoView as Context)
        val wikipediaTrackService: WikipediaTrackService = generateWikipediaTrackService()
        this.artistRepository = ArtistRepositoryImpl(wikipediaLocalStorage,wikipediaTrackService)
    }

    private fun generateWikipediaTrackService(): WikipediaTrackService{
        val wikipediaAPI = createWikipediaAPI()
        val wikipediaToArtistResolver : WikipediaToArtistResolver = WikipediaToArtistResolverImpl()
        return WikipediaTrackServiceImpl(wikipediaAPI,wikipediaToArtistResolver)
    }

    private fun createWikipediaAPI(): WikipediaAPI {
        val retrofit = createRetrofit()
        return retrofit.create(WikipediaAPI::class.java)
    }

    private fun createRetrofit() = Retrofit.Builder().baseUrl(WIKIPEDIA_BASE_URL).addConverterFactory(
        ScalarsConverterFactory.create()).build()

    private fun initPresenter(otherInfoView: OtherInfoView){
        val format : InfoSongFormat = InfoSongFormatImpl()
        this.presenter = MoreDetailsPresenterImpl(artistRepository,format)
        otherInfoView.setPresenter(this.presenter)
    }
}