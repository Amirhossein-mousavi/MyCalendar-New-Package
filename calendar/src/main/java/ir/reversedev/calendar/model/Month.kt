package ir.reversedev.calendar.model

import ir.reversedev.calendar.calendar.BaseCalendar
import ir.reversedev.calendar.calendar.MyGregorianCalendar
import ir.reversedev.calendar.calendar.MyJalaliCalendar
import ir.reversedev.calendar.calendar.RegionalType
import java.util.*

/**
 * Gregorian date sample
 * @sample Month(gregorianInstance,2050,0)
 * Jalali date sample
 * @sample Month(jalaliInstance,1420,0)
 *
 * @param calendar hold the instance of calendar
 * @param year hold every year that you give
 * @param month hold every month that you give (must be start with 0)
 */
data class Month(
    private val calendar: BaseCalendar,
    private val month: Int,
    private val year: Int
) {
    private val days = ArrayList<Day>()

    var listener: MonthItemListener? = null

    init {
        calendar.clear()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
    }

    val getYear get() = calendar.getYear()
    val getMonth get() = calendar.getMonth()
    val getMonthName get() = calendar.getMonthName()

    /**
     * if you want to change shift day parameters
     * @see Day.isEmptyDay method and refactor that depend on your parameters
     */
    fun generateDays(justAvailableCustomDays: Boolean, customDays: List<Day>): List<Day> {
        if (days.isNullOrEmpty()) {
            days.addAll(
                calendar.generateDays().map {
                    if (it == -1) {
                        //shift days
                        Day(-1, -1, -1, RegionalType.Unknown)
                    } else {
                        //for set day in calendar to operate time stamp
                        calendar.set(Calendar.DAY_OF_MONTH, it)

                        val day = Day(
                            year = calendar.getYear(),
                            month = calendar.getMonth(),
                            day = it,
                            regionalType = when (calendar) {
                                is MyJalaliCalendar -> RegionalType.Jalali
                                is MyGregorianCalendar -> RegionalType.Gregorian
                                else -> throw IllegalStateException("You must declare the regional")
                            }
                        )

                        day.monthAsString = calendar.getMonthName()
                        day.dayOfWeek = calendar.getDayOfWeek()
                        day.dayOfWeekAsString = calendar.getDayOfWeekAsString()
                        day.time = calendar.getTime()

                        if (justAvailableCustomDays)
                            customDays.firstOrNull { customDay -> customDay == day } ?: day.apply {
                                status = DayStatus.UN_AVAILABLE
                            }
                        else
                            customDays.firstOrNull { customDay -> customDay == day } ?: day
                    }
                }
            )
        }
        return days
    }
}