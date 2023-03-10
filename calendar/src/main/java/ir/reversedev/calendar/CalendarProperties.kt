package ir.reversedev.calendar

import androidx.recyclerview.widget.RecyclerView
import ir.reversedev.calendar.adapter.day.viewholder.DayViewHolderType
import ir.reversedev.calendar.availablity.BaseAvailabilityRule
import ir.reversedev.calendar.calendar.BaseCalendar
import ir.reversedev.calendar.calendar.MyGregorianCalendar
import ir.reversedev.calendar.calendar.MyJalaliCalendar
import ir.reversedev.calendar.calendar.RegionalType
import ir.reversedev.calendar.model.Day
import ir.reversedev.calendar.model.Month
import ir.reversedev.calendar.model.agenda.AgendaDayRange
import ir.reversedev.calendar.model.agenda.AgendaDays
import ir.reversedev.calendar.types.CalendarType

/**
 *  @property regionalType set regional type of calendar
 *  @property calendarType set calendar view type
 *  @property calendarOrientation set orientation of calendar (HORIZONTAL,VERTICAL)
 *  @property availabilityRule for decision of check days availability
 */
class CalendarProperties {
    val regionalType: RegionalType
    val calendarType: CalendarType
    var calendarOrientation: Int = 0

    var availabilityRule: BaseAvailabilityRule
    var justAvailableCustomDays: Boolean = false

    //for set custom days pricing and etc
    var customDays: List<Day> = listOf()

    //for range selection
    var selectedCheckIn: Day? = null
    var selectedCheckOut: Day? = null

    //for multiple selection
    var selectedMultipleDay: ArrayList<Day> = ArrayList()

    //for single selection
    var selectedSingle: Day? = null

    //for AgendaDaysPriceViewHolder
    var agendaDays: List<AgendaDays> = listOf()

    //for AgendaRangeDaysViewHolder
    var agendaRangeDays: List<AgendaDayRange> = listOf()

    var calendar: BaseCalendar? = null
        get() = if (field == null) {
            field = when (regionalType) {
                RegionalType.Jalali -> MyJalaliCalendar()
                RegionalType.Gregorian -> MyGregorianCalendar()
                RegionalType.Unknown -> throw IllegalStateException("You must declare the regional")
            }
            field
        } else field

    constructor(
        regionalType: RegionalType,
        calendarType: CalendarType,
        calendarOrientation: Int,
        availabilityRule: BaseAvailabilityRule,
        selectedCheckIn: Day? = null,
        selectedCheckOut: Day? = null,
        selectedMultipleDay: ArrayList<Day> = ArrayList(),
        selectedSingle: Day? = null
    ) {
        this.regionalType = regionalType
        this.calendarType = calendarType
        this.calendarOrientation = calendarOrientation
        this.availabilityRule = availabilityRule
        this.selectedCheckIn = selectedCheckIn
        this.selectedCheckOut = selectedCheckOut
        this.selectedMultipleDay = selectedMultipleDay
        this.selectedSingle = selectedSingle
        if (dayViewHolderType == DayViewHolderType.Unknown) {
            dayViewHolderType = DayViewHolderType.DayViewHolder
        }
    }

    /**
     * AgendaDaysPriceViewHolder
     */
    constructor(
        regionalType: RegionalType,
        calendarType: CalendarType,
        calendarOrientation: Int,
        availabilityRule: BaseAvailabilityRule,
        justAvailableCustomDays: Boolean,
        agendaDays: List<AgendaDays>,
        customDays: List<Day>,
        selectedCheckIn: Day? = null,
        selectedCheckOut: Day? = null,
        selectedMultipleDay: ArrayList<Day> = arrayListOf(),
        selectedSingle: Day? = null
    ) : this(
        regionalType = regionalType,
        calendarType = calendarType,
        calendarOrientation = calendarOrientation,
        availabilityRule = availabilityRule,
        selectedCheckIn = selectedCheckIn,
        selectedCheckOut = selectedCheckOut,
        selectedMultipleDay = selectedMultipleDay,
        selectedSingle = selectedSingle
    ) {
        dayViewHolderType = DayViewHolderType.AgendaDaysPriceViewHolder
        this.justAvailableCustomDays = justAvailableCustomDays
        this.agendaDays = agendaDays
        this.customDays = customDays
    }

    /**
     * AgendaRangeDaysViewHolder
     */
    constructor(
        regionalType: RegionalType,
        calendarType: CalendarType,
        calendarOrientation: Int,
        availabilityRule: BaseAvailabilityRule,
        agendaRangeDays: List<AgendaDayRange>
    ) : this(
        regionalType = regionalType,
        calendarType = calendarType,
        calendarOrientation = calendarOrientation,
        availabilityRule = availabilityRule
    ) {
        dayViewHolderType = DayViewHolderType.AgendaRangeDaysViewHolder
        this.agendaRangeDays = agendaRangeDays
    }

    /**
     * DayPriceViewHolder
     */
    constructor(
        regionalType: RegionalType,
        calendarType: CalendarType,
        calendarOrientation: Int,
        availabilityRule: BaseAvailabilityRule,
        justAvailableCustomDays: Boolean,
        customDays: ArrayList<Day>,
        selectedCheckIn: Day? = null,
        selectedCheckOut: Day? = null,
        selectedMultipleDay: ArrayList<Day> = arrayListOf(),
        selectedSingle: Day? = null
    ) : this(
        regionalType = regionalType,
        calendarType = calendarType,
        calendarOrientation = calendarOrientation,
        availabilityRule = availabilityRule,
        selectedCheckIn = selectedCheckIn,
        selectedCheckOut = selectedCheckOut,
        selectedMultipleDay = selectedMultipleDay,
        selectedSingle = selectedSingle
    ) {
        dayViewHolderType = DayViewHolderType.DayPriceViewHolder
        this.justAvailableCustomDays = justAvailableCustomDays
        this.customDays = customDays
    }

    internal var dayViewHolderType = DayViewHolderType.Unknown

    internal fun calendarIsReverse() =
        regionalType == RegionalType.Jalali && calendarOrientation == HORIZONTAL

    internal fun isCheckInSelect() = selectedCheckIn != null && selectedCheckOut == null

    internal fun isCheckOutSelect() = selectedCheckIn != null && selectedCheckOut != null

    internal fun getToday(): Day = calendar?.getToday()!!

    internal fun findMonthInAgendaList(month: Month) = agendaDays.firstOrNull {
        it.agendaList.firstOrNull { day ->
            day.year == month.getYear && day.month == month.getMonth
        } != null
    }

    internal fun findMonthInAgendaRangeList(month: Month) = agendaRangeDays.firstOrNull {
        it.agendaRangeList.firstOrNull { range ->
            range.startDate.year == month.getYear &&
                    range.startDate.month == month.getMonth ||
                    range.endDate.year == month.getYear &&
                    range.endDate.month == month.getMonth
        } != null
    }

    companion object {
        const val HORIZONTAL = RecyclerView.HORIZONTAL
        const val VERTICAL = RecyclerView.VERTICAL
    }
}