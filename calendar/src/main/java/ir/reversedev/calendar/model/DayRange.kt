package ir.reversedev.calendar.model

import ir.reversedev.calendar.model.Day

data class DayRange(
    val startDate: Day,
    val endDate: Day
) {
    override fun toString(): String {
        return if (startDate.month == endDate.month) {
            "${startDate.day} تا ${endDate.day} ${startDate.monthAsString}"
        } else {
            "${startDate.day} ${startDate.monthAsString} تا ${endDate.day} ${endDate.monthAsString}"
        }
    }
}