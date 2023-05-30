package ayds.winchester.songinfo.moredetails.injector

import android.content.Context
import ayds.NY1.NewYorkTimes.external.DependenciesInjector
import ayds.winchester.songinfo.moredetails.data.ArtistRepositoryImpl
import ayds.winchester.songinfo.moredetails.data.broker.Broker
import ayds.winchester.songinfo.moredetails.data.broker.BrokerImpl
import ayds.winchester.songinfo.moredetails.data.broker.proxy.server.LastFMProxy
import ayds.winchester.songinfo.moredetails.data.broker.proxy.server.NYTProxy
import ayds.winchester.songinfo.moredetails.data.broker.proxy.server.WikipediaProxy
import ayds.winchester.songinfo.moredetails.data.local.WikipediaLocalStorage
import ayds.winchester.songinfo.moredetails.data.local.sqldb.CursorToWikipediaArtistMapper
import ayds.winchester.songinfo.moredetails.data.local.sqldb.CursorToWikipediaArtistMapperImpl
import ayds.winchester.songinfo.moredetails.data.local.sqldb.WikipediaLocalStorageImpl
import ayds.winchester.songinfo.moredetails.domain.repository.ArtistRepository
import ayds.winchester.songinfo.moredetails.presentation.*
import lisboa5lastfm.ExternalServiceInjector
import lisboa5lastfm.artist.ArtistExternalService
import wikipedia.external.external.WikipediaArticleService
import wikipedia.external.external.WikipediaInjector

object MoreDetailsInjector {

    private lateinit var artistRepository : ArtistRepository
    private lateinit var presenter : MoreDetailsPresenter

    fun init(otherInfoView: OtherInfoView){
        initRepository(otherInfoView)
        initPresenter(otherInfoView)
    }
    private fun initRepository(otherInfoView: OtherInfoView){
        val broker = initBroker()
        val wikipediaLocalStorage: WikipediaLocalStorage = generateWikipediaLocalStorage(otherInfoView)
        this.artistRepository = ArtistRepositoryImpl(wikipediaLocalStorage, broker)
    }

    private fun initBroker(): Broker{
        val broker: Broker = BrokerImpl()

        val wikipediaService: WikipediaArticleService = WikipediaInjector.generateWikipediaService()
        val wikipediaProxy = WikipediaProxy(wikipediaService)
        broker.registerServer(wikipediaProxy)

        val lastFMService : ArtistExternalService = ExternalServiceInjector.getLastFMService()
        val lastFMProxy = LastFMProxy(lastFMService)
        broker.registerServer(lastFMProxy)

        val nytService = DependenciesInjector.init()
        val nytProxy = NYTProxy(nytService)
        broker.registerServer(nytProxy)

        return broker
    }

    private fun generateWikipediaLocalStorage(otherInfoView: OtherInfoView): WikipediaLocalStorage {
        val cursor : CursorToWikipediaArtistMapper = CursorToWikipediaArtistMapperImpl()
        return WikipediaLocalStorageImpl(otherInfoView as Context, cursor)
    }
    private fun initPresenter(otherInfoView: OtherInfoView){
        val format : InfoSongFormat = InfoSongFormatImpl()
        this.presenter = MoreDetailsPresenterImpl(artistRepository,format)
        otherInfoView.setPresenter(this.presenter)
    }
}