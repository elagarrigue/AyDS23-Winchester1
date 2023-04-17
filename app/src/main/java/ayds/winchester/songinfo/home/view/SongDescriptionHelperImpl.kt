package ayds.winchester.songinfo.home.view

import ayds.winchester.songinfo.home.model.entities.Song
import ayds.winchester.songinfo.home.model.entities.Song.EmptySong
import ayds.winchester.songinfo.home.model.entities.Song.SpotifySong
import java.text.SimpleDateFormat
import java.util.*

interface SongDescriptionHelper {
    fun getSongDescriptionText(song: Song = EmptySong): String
}

internal class SongDescriptionHelperImpl (private val releaseDateF: ReleaseDateFactory): SongDescriptionHelper {
    override fun getSongDescriptionText(song: Song): String {
        return when (song) {
            is SpotifySong ->
                "${
                    "Song: ${song.songName} " +
                            if (song.isLocallyStored) "[*]" else ""
                }\n" +
                        "Artist: ${song.artistName}\n" +
                        "Album: ${song.albumName}\n" +
                        "Release Date: ${song.getReleasedDateFormat()}\n"
            else -> "Song not found"
        }
    }
    private fun SpotifySong.getReleasedDateFormat() = releaseDateF.getReleaseDate(this.releaseDatePrecision, this.releaseDate).getFormat()
}