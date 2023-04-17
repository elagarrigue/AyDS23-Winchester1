package ayds.winchester.songinfo.home.view

import java.text.SimpleDateFormat
import java.util.*

enum class Precision{
    day,
    month,
    year
}
interface ReleaseDateFactory {
    fun getReleaseDate(precision: String, date: String): ReleaseDate
}
internal class ReleaseDateFactoryImpl : ReleaseDateFactory {
    override fun getReleaseDate(precision: String, date: String) =
        when (precision) {
            Precision.day.name -> DayDate(date)
            Precision.month.name -> MonthDate(date)
            Precision.year.name -> YearDate(date)
            else -> DefaultDate(date)
        }
}

sealed class ReleaseDate(
    val date: String
) {
    abstract fun getFormat(): String
}

internal class DayDate(
    date: String
) : ReleaseDate(date) {
    override fun getFormat(): String {
        val specificDate: Date = SimpleDateFormat("yyyy-MM-dd").parse(date)
        return SimpleDateFormat("dd/MM/yyyy").format(specificDate)
    }
}

internal class MonthDate(
    date: String
) : ReleaseDate(date) {
    override fun getFormat(): String {
        val specificDate: Date = SimpleDateFormat("yyyy-MM").parse(date)
        return SimpleDateFormat("MMMM, yyyy").format(specificDate)
    }
}

internal class YearDate(
    date: String
) : ReleaseDate(date) {
    override fun getFormat(): String {
        val intDate: Int = date.toInt()
        var newDate: String = date
        newDate += if (leapYear(intDate))
            " (leap year)"
        else
            " (not a leap year)"
        return newDate
    }
    private fun leapYear(n: Int) = ((n % 4 == 0 && n % 100 != 0) || n % 400 == 0)
}

internal class DefaultDate(
    date: String
): ReleaseDate(date){
    override fun getFormat()= date
}