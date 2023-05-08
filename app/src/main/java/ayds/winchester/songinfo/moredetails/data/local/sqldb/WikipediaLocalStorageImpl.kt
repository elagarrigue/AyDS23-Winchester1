package ayds.winchester.songinfo.moredetails.data.local.sqldb

import ayds.winchester.songinfo.moredetails.data.local.WikipediaLocalStorage

private const val PREFIX_DATABASE = "[*]"
class WikipediaLocalStorageImpl(): WikipediaLocalStorage {

    private fun getArtistInfoFromDataBase(artistName: String): String? {
        return dataBase.getInfo(artistName)
    }
    private fun formatFromDataBase(infoSong: String) = "$PREFIX_DATABASE$infoSong"

}