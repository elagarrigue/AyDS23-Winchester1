package ayds.winchester.songinfo.moredetails.presentation

data class OtherInfoUiState(
    val sourceLogo: String = WIKIPEDIA_LOGO,
    val description: String = "",
    val sourceArticleUrl: String = "",
    val sourceName: String = ""
){
    companion object {
        const val WIKIPEDIA_LOGO =
            "https://upload.wikimedia.org/wikipedia/commons/8/8c/Wikipedia-logo-v2-es.png"
    }
}