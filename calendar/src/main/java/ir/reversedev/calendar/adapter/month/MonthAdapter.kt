package ir.reversedev.calendar.adapter.month

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ir.reversedev.calendar.R
import ir.reversedev.calendar.model.Month
import ir.reversedev.calendar.adapter.day.DaysAdapterListener
import ir.reversedev.calendar.CalendarProperties

internal class MonthAdapter(
    private val properties: CalendarProperties,
    private val listener: MonthAdapterListener
) : RecyclerView.Adapter<MonthViewHolder>(), DaysAdapterListener {

    private val monthList = ArrayList<Month>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthViewHolder {
        return MonthViewHolder(
            view = LayoutInflater.from(parent.context).inflate(
                if (properties.calendarOrientation == CalendarProperties.VERTICAL)
                    R.layout.month_item_vertical
                else
                    R.layout.month_item_horizontal,
                parent,
                false
            ),
            properties = properties,
            daysAdapterListener = this
        )
    }

    override fun onBindViewHolder(holder: MonthViewHolder, position: Int) {
        holder.bind(monthList.size, position, monthList[position], listener)
    }

    override fun onDaysNotifyDataSetChanged() {
        for (indic in monthList.indices)
            monthList.getOrNull(indic)?.listener?.onDataSetChanged()
    }

    override fun getItemCount(): Int = monthList.size

    fun submitList(list: List<Month>) {
        monthList.clear()
        monthList.addAll(list)
        notifyDataSetChanged()
    }
}