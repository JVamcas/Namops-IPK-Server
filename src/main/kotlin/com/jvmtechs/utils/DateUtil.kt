package com.jvmtechs.utils


import java.sql.Date
import java.sql.Timestamp
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class DateUtil {
    companion object {
        const val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"

        fun today(): Timestamp {
            val date = Date()
            return Timestamp(date.time)
        }

        fun Date._24(): String {
            return SimpleDateFormat(DATE_FORMAT, Locale.US).format(this)
        }

        fun LocalDateTime._24(): String{
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            return this.format(formatter)
        }
        fun localDateToday(): LocalDateTime{
            val date = Date()
            return Timestamp(date.time).toLocalDateTime()
        }

        fun thisMonthByName(): String {
            val cal = Calendar.getInstance()
            return monthByName(cal.get(Calendar.MONTH) + 1)
        }

        fun monthByName(index: Int): String = DateFormatSymbols().months[index - 1]

        fun thisYear() = Calendar.getInstance().get(Calendar.YEAR).toString()

        fun lastYear() = (Calendar.getInstance().get(Calendar.YEAR) - 1).toString()
    }
}