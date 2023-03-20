package ayds.winchester.songinfo.home.model

import android.content.Context
import ayds.winchester.songinfo.home.model.repository.SongRepository
import ayds.winchester.songinfo.home.model.repository.SongRepositoryImpl
import ayds.winchester.songinfo.home.model.repository.external.spotify.SpotifyInjector
import ayds.winchester.songinfo.home.model.repository.external.spotify.SpotifyTrackService
import ayds.winchester.songinfo.home.model.repository.local.spotify.SpotifyLocalStorage
import ayds.winchester.songinfo.home.model.repository.local.spotify.sqldb.CursorToSpotifySongMapperImpl
import ayds.winchester.songinfo.home.model.repository.local.spotify.sqldb.SpotifyLocalStorageImpl
import ayds.winchester.songinfo.home.view.HomeView

object HomeModelInjector {

    private lateinit var homeModel: HomeModel

    fun getHomeModel(): HomeModel = homeModel

    fun initHomeModel(homeView: HomeView) {
        val spotifyLocalStorage: SpotifyLocalStorage = SpotifyLocalStorageImpl(
          homeView as Context, CursorToSpotifySongMapperImpl()
        )
        val spotifyTrackService: SpotifyTrackService = SpotifyInjector.spotifyTrackService

        val repository: SongRepository =
            SongRepositoryImpl(spotifyLocalStorage, spotifyTrackService)

        homeModel = HomeModelImpl(repository)
    }
}