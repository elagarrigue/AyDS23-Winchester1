package ayds.winchester.songinfo.moredetails.injector

import android.content.Context
import ayds.winchester.songinfo.moredetails.data.ArtistRepositoryImpl
import wikipedia.external.external.WikipediaArticleService
import ayds.winchester.songinfo.moredetails.data.local.WikipediaLocalStorage
import ayds.winchester.songinfo.moredetails.data.local.sqldb.CursorToWikipediaArtistMapper
import ayds.winchester.songinfo.moredetails.data.local.sqldb.CursorToWikipediaArtistMapperImpl
import ayds.winchester.songinfo.moredetails.data.local.sqldb.WikipediaLocalStorageImpl
import ayds.winchester.songinfo.moredetails.domain.repository.ArtistRepository
import ayds.winchester.songinfo.moredetails.presentation.*
import wikipedia.external.external.WikipediaInjector

object MoreDetailsInjector {

    private lateinit var artistRepository : ArtistRepository
    private lateinit var presenter : MoreDetailsPresenter

    fun init(otherInfoView: OtherInfoView){
        initRepository(otherInfoView)
        initPresenter(otherInfoView)
    }
    private fun initRepository(otherInfoView: OtherInfoView){
        val wikipediaLocalStorage: WikipediaLocalStorage = generateWikipediaLocalStorage(otherInfoView)
        val wikipediaArticleService: WikipediaArticleService = WikipediaInjector.generateWikipediaService()
        this.artistRepository = ArtistRepositoryImpl(wikipediaLocalStorage,wikipediaArticleService)
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