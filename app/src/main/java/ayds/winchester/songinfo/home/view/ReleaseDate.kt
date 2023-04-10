package ayds.winchester.songinfo.home.view

import java.text.SimpleDateFormat
import java.util.*

object ReleaseDateFactory {
    fun get(precision: String, date: String) =
        when (precision) {
            "day" -> DayDate(date)
            "month" -> MonthDate(date)
            //"year" -> YearDate(date)
            else -> YearDate(date)
        }
}

sealed class ReleaseDate(
    val date: String
) {
    abstract fun getFormat(): String
}

class DayDate(
    date: String
) : ReleaseDate(date) {
    override fun getFormat(): String {
        val fechaConcreta: Date = SimpleDateFormat("yyyy-MM-dd").parse(date)
        return SimpleDateFormat("dd/MM/yyyy").format(fechaConcreta)
    }
}

class MonthDate(
    date: String
) : ReleaseDate(date) {
    override fun getFormat(): String {
        val fechaConcreta: Date = SimpleDateFormat("yyyy-MM").parse(date)
        return SimpleDateFormat("MMMM, yyyy").format(fechaConcreta)
    }
}

class YearDate(
    date: String
) : ReleaseDate(date) {
    override fun getFormat(): String {
        val fechaEntero: Int = date.toInt();
        var fechaNueva: String = date;
        if (añoBisiesto(fechaEntero))
            fechaNueva += " (leap year)"
        else
            fechaNueva += " (not a leap year) "

        return fechaNueva
    }

    private fun añoBisiesto(n: Int) = ((n % 4 == 0 && n % 100 != 0) || n % 400 == 0)

}