package ayds.winchester.songinfo.moredetails.domain.repository

import ayds.winchester.songinfo.moredetails.domain.entities.Artist

interface ArtistRepository {
    fun getArtist(artistName: String): Artist

}