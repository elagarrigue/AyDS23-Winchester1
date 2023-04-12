package ayds.winchester.songinfo.home.view

import java.text.SimpleDateFormat
import java.util.*


enum class Precision{
    day,
    month,
    year
}
interface ReleaseDateFactory {
    fun get(precision: String, date: String): ReleaseDate
}
internal class ReleaseDateFactoryImpl : ReleaseDateFactory {

    override fun get(precision: String, date: String) =
        when (precision) {
            "day" -> DayDate(date)
            "month" -> MonthDate(date)
            "year" -> YearDate(date)
            else -> DefaultDate(date)
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
        val specificDate: Date = SimpleDateFormat("yyyy-MM-dd").parse(date)
        return SimpleDateFormat("dd/MM/yyyy").format(specificDate)
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
        val intDate: Int = date.toInt();
        var newDate: String = date;

        if (leapYear(intDate))
            newDate += " (leap year)"
        else
            newDate += " (not a leap year) "

        return newDate
    }

    private fun leapYear(n: Int) = ((n % 4 == 0 && n % 100 != 0) || n % 400 == 0)

}

class DefaultDate(
    date: String
): ReleaseDate(date){
    override fun getFormat(): String {
        return date
    }

}