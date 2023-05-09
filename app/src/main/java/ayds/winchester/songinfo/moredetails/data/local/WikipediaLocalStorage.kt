package ayds.winchester.songinfo.moredetails.data.local

interface WikipediaLocalStorage {
    fun getArtistInfoFromDataBase(artistName: String): String?
    fun formatFromDataBase(infoSong: String): String
    fun saveArtist(artist: String?, info: String?)
}