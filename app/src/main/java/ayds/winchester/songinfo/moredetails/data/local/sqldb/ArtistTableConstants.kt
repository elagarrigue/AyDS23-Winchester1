package ayds.winchester.songinfo.moredetails.data.local.sqldb

const val ID_COLUMN = "id"
const val ARTIST_COLUMN = "artist"
const val INFO_COLUMN = "info"
const val SOURCE_URL_COLUMN = "url"
const val SOURCE_COLUMN = "source"
const val SOURCE_LOGO_URL_COLUMN = "sourcelogourl"
const val TABLE_ARTIST_NAME = "artists"
const val DB_NAME = "dictionary.db"
const val DB_VERSION = 1

const val createArtistTableQuery =
    "create table $TABLE_ARTIST_NAME (" +
            "$ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "$ARTIST_COLUMN string, " +
            "$INFO_COLUMN string, " +
            "$SOURCE_URL_COLUMN string, " +
            "$SOURCE_LOGO_URL_COLUMN string, " +
            "$SOURCE_COLUMN integer)"


