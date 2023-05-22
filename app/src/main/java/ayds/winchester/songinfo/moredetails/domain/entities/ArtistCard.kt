package ayds.winchester.songinfo.moredetails.domain.entities

sealed class Card {

    data class ArtistCard(
        val name : String,
        var description : String,
        var infoUrl: String,
        var source: Source = Source.Wikipedia,
        var sourceLogoUrl: String = "",
        var isInDataBase : Boolean
    ): Card()

    object EmptyCard : Card()
}

enum class Source{
    Wikipedia
}