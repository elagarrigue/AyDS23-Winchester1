package ayds.winchester.songinfo.home.model.repository

import ayds.winchester.songinfo.home.model.entities.Song.EmptySong
import ayds.winchester.songinfo.home.model.entities.Song
import ayds.winchester.songinfo.home.model.entities.Song.SpotifySong
import ayds.winchester.songinfo.home.model.repository.external.spotify.SpotifyTrackService
import ayds.winchester.songinfo.home.model.repository.local.spotify.SpotifyLocalStorage

interface SongRepository {
    fun getSongByTerm(term: String): Song
    fun getSongById(id: String): Song
}

internal class SongRepositoryImpl(
    private val spotifyLocalStorage: SpotifyLocalStorage,
    private val spotifyTrackService: SpotifyTrackService
) : SongRepository {

    override fun getSongByTerm(term: String): Song {
        var spotifySong = spotifyLocalStorage.getSongByTerm(term)

        when {
            spotifySong != null -> markSongAsLocal(spotifySong)
            else -> {
                try {
                    spotifySong = spotifyTrackService.getSong(term)

                    spotifySong?.let {
                        when {
                            it.isSavedSong() -> spotifyLocalStorage.updateSongTerm(term, it.id)
                            else -> spotifyLocalStorage.insertSong(term, it)
                        }
                    }
                } catch (e: Exception) {
                    spotifySong = null
                }
            }
        }

        return spotifySong ?: EmptySong
    }

    override fun getSongById(id: String): Song {
        return spotifyLocalStorage.getSongById(id) ?: EmptySong
    }

    private fun SpotifySong.isSavedSong() = spotifyLocalStorage.getSongById(id) != null

    private fun markSongAsLocal(song: SpotifySong) {
        song.isLocallyStored = true
    }
}