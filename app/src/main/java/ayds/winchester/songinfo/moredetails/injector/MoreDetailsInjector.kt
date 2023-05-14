package ayds.winchester.songinfo.moredetails.injector

import android.content.Context
import ayds.winchester.songinfo.moredetails.data.ArtistRepositoryImpl
import ayds.winchester.songinfo.moredetails.data.external.WikipediaArticleService
import ayds.winchester.songinfo.moredetails.data.external.article.WikipediaAPI
import ayds.winchester.songinfo.moredetails.data.external.article.WikipediaToArtistResolver
import ayds.winchester.songinfo.moredetails.data.external.article.WikipediaToArtistResolverImpl
import ayds.winchester.songinfo.moredetails.data.external.article.WikipediaArticleServiceImpl
import ayds.winchester.songinfo.moredetails.data.local.WikipediaLocalStorage
import ayds.winchester.songinfo.moredetails.data.local.sqldb.CursorToWikipediaArtistMapper
import ayds.winchester.songinfo.moredetails.data.local.sqldb.CursorToWikipediaArtistMapperImpl
import ayds.winchester.songinfo.moredetails.data.local.sqldb.WikipediaLocalStorageImpl
import ayds.winchester.songinfo.moredetails.domain.repository.ArtistRepository
import ayds.winchester.songinfo.moredetails.presentation.*
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val WIKIPEDIA_BASE_URL = "https://en.wikipedia.org/w/"
object MoreDetailsInjector {

    private lateinit var artistRepository : ArtistRepository
    private lateinit var presenter : MoreDetailsPresenter

    fun init(otherInfoView: OtherInfoView){
        val format : InfoSongFormat = InfoSongFormatImpl()
        initRepository(otherInfoView, format)
        initPresenter(otherInfoView, format)
    }
    private fun initRepository(otherInfoView: OtherInfoView, format : InfoSongFormat){
        val wikipediaLocalStorage: WikipediaLocalStorage = generateWikipediaLocalStorage(otherInfoView)
        val wikipediaArticleService: WikipediaArticleService = generateWikipediaTrackService(format)
        this.artistRepository = ArtistRepositoryImpl(wikipediaLocalStorage,wikipediaArticleService)
    }

    private fun generateWikipediaLocalStorage(otherInfoView: OtherInfoView): WikipediaLocalStorage {
        val cursor : CursorToWikipediaArtistMapper = CursorToWikipediaArtistMapperImpl()
        return WikipediaLocalStorageImpl(otherInfoView as Context, cursor)
    }
    private fun generateWikipediaTrackService(format : InfoSongFormat): WikipediaArticleService{
        val wikipediaAPI = createWikipediaAPI()
        val wikipediaToArtistResolver : WikipediaToArtistResolver = WikipediaToArtistResolverImpl(format)
        return WikipediaArticleServiceImpl(wikipediaAPI,wikipediaToArtistResolver)
    }
    private fun createWikipediaAPI(): WikipediaAPI {
        val retrofit = createRetrofit()
        return retrofit.create(WikipediaAPI::class.java)
    }
    private fun createRetrofit() = Retrofit.Builder().baseUrl(WIKIPEDIA_BASE_URL).addConverterFactory(
        ScalarsConverterFactory.create()).build()
    private fun initPresenter(otherInfoView: OtherInfoView, format : InfoSongFormat){
        this.presenter = MoreDetailsPresenterImpl(artistRepository,format)
        otherInfoView.setPresenter(this.presenter)
    }
}