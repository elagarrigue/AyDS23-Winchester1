package ayds.winchester.songinfo.home.model.repository.external.spotify

import ayds.winchester.songinfo.home.model.entities.Song.SpotifySong

interface SpotifyTrackService {

    fun getSong(title: String): SpotifySong?
}