package ayds.winchester.songinfo.moredetails.injector

import android.content.Context
import ayds.winchester.songinfo.moredetails.data.ArtistRepositoryImpl
import ayds.winchester.songinfo.moredetails.data.broker.*
import ayds.winchester.songinfo.moredetails.data.broker.ClientProxyImp
import ayds.winchester.songinfo.moredetails.data.broker.artistBroker
import ayds.winchester.songinfo.moredetails.data.broker.proxy.server.WikipediaProxy
import wikipedia.external.external.WikipediaArticleService
import ayds.winchester.songinfo.moredetails.data.local.WikipediaLocalStorage
import ayds.winchester.songinfo.moredetails.data.local.sqldb.CursorToWikipediaArtistMapper
import ayds.winchester.songinfo.moredetails.data.local.sqldb.CursorToWikipediaArtistMapperImpl
import ayds.winchester.songinfo.moredetails.data.local.sqldb.WikipediaLocalStorageImpl
import ayds.winchester.songinfo.moredetails.domain.repository.ArtistRepository
import ayds.winchester.songinfo.moredetails.presentation.*
import wikipedia.external.external.WikipediaInjector

object MoreDetailsInjector {

    private val broker : Broker = artistBroker()
    private val clientProxy: ClientProxy = ClientProxyImp(broker)
    private lateinit var artistRepository : ArtistRepository
    private lateinit var presenter : MoreDetailsPresenter

    fun init(otherInfoView: OtherInfoView){
        initRepository(otherInfoView)
        initPresenter(otherInfoView)
    }
    private fun initRepository(otherInfoView: OtherInfoView){
        val wikipediaLocalStorage: WikipediaLocalStorage = generateWikipediaLocalStorage(otherInfoView)
        val wikipediaArticleService: WikipediaArticleService = WikipediaInjector.generateWikipediaService()
        val wikipediaProxy : ServerProxy = WikipediaProxy(
            server = clientProxy, //solamente para que no salte error, esto no tiene sentido
            broker = broker,
            wikipediaArticleService = wikipediaArticleService,
            wikipediaLocalStorage = wikipediaLocalStorage
        )
        this.artistRepository = ArtistRepositoryImpl(clientProxy)
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