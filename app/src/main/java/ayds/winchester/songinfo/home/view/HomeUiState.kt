package ayds.winchester.songinfo.home.view

data class HomeUiState(
    val songId: String = "",
    val searchTerm: String = "",
    val songDescription: String = "",
    val songImageUrl: String = DEFAULT_IMAGE,
    val songUrl: String = "",
    val actionsEnabled: Boolean = false,
) {

    companion object {
        const val DEFAULT_IMAGE =
            "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d2/Winchester_Montage.jpg/600px-Winchester_Montage.jpg"
    }
}