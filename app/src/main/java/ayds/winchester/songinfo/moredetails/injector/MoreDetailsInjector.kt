package ayds.winchester.songinfo.moredetails.injector

import android.content.Context
import ayds.ny1.newyorktimes.DependenciesInjector
import ayds.winchester.songinfo.moredetails.data.CardRepositoryImpl
import ayds.winchester.songinfo.moredetails.data.broker.CardsBroker
import ayds.winchester.songinfo.moredetails.data.broker.CardsBrokerImpl
import ayds.winchester.songinfo.moredetails.data.broker.proxy.server.LastFMProxy
import ayds.winchester.songinfo.moredetails.data.broker.proxy.server.NYTProxy
import ayds.winchester.songinfo.moredetails.data.broker.proxy.server.WikipediaProxy
import ayds.winchester.songinfo.moredetails.data.local.CardLocalStorage
import ayds.winchester.songinfo.moredetails.data.local.sqldb.CursorToCardMapper
import ayds.winchester.songinfo.moredetails.data.local.sqldb.CursorToCardMapperImpl
import ayds.winchester.songinfo.moredetails.data.local.sqldb.CardLocalStorageImpl
import ayds.winchester.songinfo.moredetails.domain.repository.CardRepository
import ayds.winchester.songinfo.moredetails.presentation.*
import lisboa5lastfm.ExternalServiceInjector
import lisboa5lastfm.artist.ArtistExternalService
import wikipedia.external.external.WikipediaArticleService
import wikipedia.external.external.WikipediaInjector

object MoreDetailsInjector {

    private lateinit var cardRepository : CardRepository
    private lateinit var presenter : MoreDetailsPresenter

    fun init(otherInfoView: OtherInfoView){
        initRepository(otherInfoView)
        initPresenter(otherInfoView)
    }
    private fun initRepository(otherInfoView: OtherInfoView){
        val broker = initBroker()
        val cardLocalStorage: CardLocalStorage = generateCardLocalStorage(otherInfoView)
        this.cardRepository = CardRepositoryImpl(cardLocalStorage, broker)
    }

    private fun initBroker(): CardsBroker{
        val broker: CardsBroker = CardsBrokerImpl()

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

    private fun generateCardLocalStorage(otherInfoView: OtherInfoView): CardLocalStorage {
        val cursor : CursorToCardMapper = CursorToCardMapperImpl()
        return CardLocalStorageImpl(otherInfoView as Context, cursor)
    }
    private fun initPresenter(otherInfoView: OtherInfoView){
        val format : InfoSongFormat = InfoSongFormatImpl()
        this.presenter = MoreDetailsPresenterImpl(cardRepository,format)
        otherInfoView.setPresenter(this.presenter)
    }
}