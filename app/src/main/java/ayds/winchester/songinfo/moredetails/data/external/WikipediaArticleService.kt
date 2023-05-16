package ayds.winchester.songinfo.moredetails.data.external

import ayds.winchester.songinfo.moredetails.domain.entities.Artist
import retrofit2.Response

interface WikipediaArticleService {
    fun getArtist(artistName: String): Artist.WikipediaArtist
}