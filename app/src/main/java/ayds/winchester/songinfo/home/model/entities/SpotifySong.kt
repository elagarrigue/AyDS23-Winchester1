package ayds.winchester.songinfo.home.model.entities

sealed class Song {

    data class SpotifySong(
        val id: String,
        val songName: String,
        val artistName: String,
        val albumName: String,
        val releaseDate: String,
        val spotifyUrl: String,
        val imageUrl: String,
        var isLocallyStored: Boolean = false
    ) : Song() {

        val year: String = releaseDate.split("-").first()
    }

    object EmptySong : Song()
}