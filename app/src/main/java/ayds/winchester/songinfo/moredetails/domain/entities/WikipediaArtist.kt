package ayds.winchester.songinfo.moredetails.domain.entities

sealed class Artist {

    data class WikipediaArtist(
        val name : String,
        var artistInfo : String,
        var wikipediaUrl: String,
        var isInDataBase : Boolean
    ): Artist() {
    }

    object EmptyArtist : Artist()
}