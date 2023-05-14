package ayds.winchester.songinfo.moredetails.data.local.sqldb

const val ID_COLUMN = "id"
const val ARTIST_COLUMN = "artist"
const val INFO_COLUMN = "info"
const val SOURCE_COLUMN = "source"
const val WIKIPEDIA_URL_COLUMN = "url"
const val TABLE_ARTIST_NAME = "artists"

const val createArtistTableQuery =
    "create table $TABLE_ARTIST_NAME (" +
            "$ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "$ARTIST_COLUMN string, " +
            "$INFO_COLUMN string, " +
            "$WIKIPEDIA_URL_COLUMN string, " +
            "$SOURCE_COLUMN integer)"

