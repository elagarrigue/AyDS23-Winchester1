package ayds.winchester.songinfo.moredetails.data

import ayds.winchester.songinfo.moredetails.data.external.WikipediaTrackService
import ayds.winchester.songinfo.moredetails.data.local.WikipediaLocalStorage
import ayds.winchester.songinfo.moredetails.domain.Repository

class RepositoryImpl(
    private val wikipediaLocalStorage: WikipediaLocalStorage,
    private val wikipediaTrackService: WikipediaTrackService
): Repository {



}