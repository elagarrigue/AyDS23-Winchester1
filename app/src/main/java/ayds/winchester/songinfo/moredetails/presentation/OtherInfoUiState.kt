package ayds.winchester.songinfo.moredetails.presentation

data class OtherInfoUiState(
    val wikipediaLogo: String = WIKIPEDIA_LOGO,
    val artistInfo: String? = "",
    val wikipediaArticleUrl: String = ""
){
    companion object {
        const val WIKIPEDIA_LOGO =
            "https://upload.wikimedia.org/wikipedia/commons/8/8c/Wikipedia-logo-v2-es.png"
    }
}