package ayds.winchester.songinfo.moredetails.domain

import ayds.winchester.songinfo.moredetails.domain.entities.Artist

interface ArtistRepository {
    fun getArtist(): Artist

    //Ver lo de la id
}