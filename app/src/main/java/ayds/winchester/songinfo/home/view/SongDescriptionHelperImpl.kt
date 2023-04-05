package ayds.winchester.songinfo.home.view

import ayds.winchester.songinfo.home.model.entities.Song
import ayds.winchester.songinfo.home.model.entities.Song.EmptySong
import ayds.winchester.songinfo.home.model.entities.Song.SpotifySong
import java.text.SimpleDateFormat
import java.util.*


interface SongDescriptionHelper {
    fun getSongDescriptionText(song: Song = EmptySong): String
}

internal class SongDescriptionHelperImpl : SongDescriptionHelper {
    override fun getSongDescriptionText(song: Song): String {
        return when (song) {
            is SpotifySong ->
                "${
                    "Song: ${song.songName} " +
                            if (song.isLocallyStored) "[*]" else ""
                }\n" +
                        "Artist: ${song.artistName}\n" +
                        "Album: ${song.albumName}\n" +
                        when (song.releaseDatePrecision){
                            "day" -> "Release date: ${formatoFechaDia(song.releaseDate)}\n"
                            "month" -> "Release date: ${formatoFechaMes(song.releaseDate)}\n"
                            "year" -> "Release date: \n"
                            else -> {}
                        }
            else -> "Song not found"
        }
    }

    fun formatoFechaDia(fechaRecibida: String):String{
        val fechaConcreta: Date = SimpleDateFormat("yyyy-MM-dd").parse(fechaRecibida)
        val fechaNueva: String = SimpleDateFormat("dd/MM/yyyy").format(fechaConcreta)
        return fechaNueva
    }

    fun formatoFechaMes(fechaRecibida: String):String{
        val fechaConcreta: Date = SimpleDateFormat("yyyy-MM").parse(fechaRecibida)
        val fechaNueva: String = SimpleDateFormat("MMMM, yyyy").format(fechaConcreta)
        return fechaNueva
    }

    fun formatoFechaAño(fechaRecibida: String):String{
        val fechaEntero: Int = fechaRecibida.toInt();
        var fechaNueva: String = fechaRecibida;
        if (añoBisiesto(fechaEntero))
            fechaNueva += " (leap year)"
        else
            fechaNueva+= " (not a leap year) "

        return fechaNueva
    }

    fun añoBisiesto(n:Int) = ( (n % 4 == 0 && n % 100 != 0 ) || n % 400 == 0)


}