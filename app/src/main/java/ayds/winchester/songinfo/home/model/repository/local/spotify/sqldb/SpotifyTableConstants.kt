package ayds.winchester.songinfo.home.model.repository.local.spotify.sqldb

const val SONGS_TABLE = "songs"
const val ID_COLUMN = "id"
const val TERM_COLUMN = "term"
const val NAME_COLUMN = "name"
const val ARTIST_COLUMN = "artist"
const val ALBUM_COLUMN = "album"
const val RELEASE_DATE_COLUMN = "release_date"
const val SPOTIFY_URL_COLUMN = "spotify_url"
const val IMAGE_URL_COLUMN = "image_url"

const val createSongsTableQuery: String =
    "create table $SONGS_TABLE (" +
            "$ID_COLUMN string PRIMARY KEY, " +
            "$TERM_COLUMN string, " +
            "$NAME_COLUMN string, " +
            "$ARTIST_COLUMN integer, " +
            "$ALBUM_COLUMN string, " +
            "$RELEASE_DATE_COLUMN string, " +
            "$SPOTIFY_URL_COLUMN string, " +
            "$IMAGE_URL_COLUMN string)"