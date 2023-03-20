package ayds.winchester.songinfo.home.model.repository.external.spotify

import ayds.winchester.songinfo.home.model.repository.external.spotify.tracks.*

object SpotifyInjector {

    val spotifyTrackService: SpotifyTrackService = SpotifyTrackInjector.spotifyTrackService
}