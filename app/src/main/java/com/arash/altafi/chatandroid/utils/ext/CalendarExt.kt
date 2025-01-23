package com.arash.altafi.chatandroid.utils.ext

import android.content.Context
import com.arash.altafi.chatandroid.R
import saman.zamani.persiandate.PersianDate
import java.text.SimpleDateFormat
import java.util.*

fun PersianDate.getClockString(withSecond: Boolean = false): String {
    val secondString: String = if (withSecond) "::$second" else ""
    return if (withSecond) "%02d:%02d:%02d".applyValue(hour, minute, secondString.toInt())
    else "%02d:%02d".applyValue(hour, minute)
}

fun PersianDate.getDateString(): String {
    val year = shYear
    val month = if (shMonth < 10) "0${shMonth}" else shMonth
    val day = if (shDay < 10) "0${shDay}" else shDay

    return ("$year/$month/$day")
}

fun PersianDate.getDateStringWithClock(withSecond: Boolean = false): String {
    val year = shYear
    val month = if (shMonth < 10) "0${shMonth}" else shMonth
    val day = if (shDay < 10) "0${shDay}" else shDay

    val secondString: String = if (withSecond) ":$second" else ""
    return ("$year/$month/$day $hour:$minute$secondString")
}

fun PersianDate.getDateStringWithClock2Digit(withSecond: Boolean = false): String {
    val year = shYear
    val month = if (shMonth < 10) "0$shMonth" else shMonth.toString()
    val day = if (shDay < 10) "0$shDay" else shDay.toString()

    val hourFormatted = String.format(Locale.getDefault(), "%02d", hour)
    val minuteFormatted = String.format(Locale.getDefault(), "%02d", minute)

    val secondString: String = if (withSecond) ":$second" else ""

    return ("$year/$month/$day - $hourFormatted:$minuteFormatted$secondString")
}

fun PersianDate.getTimeString(withSecond: Boolean = false): String {
    val secondString: String = if (withSecond) ":$second" else ""
    return ("$hour:$minute$secondString")
}

private fun getPersianWeekDayName(index: Int): String = when (index) {
    0 -> "شنبه"
    1 -> "یک شنبه"
    2 -> "دو شنبه"
    3 -> "سه شنبه"
    4 -> "چهار شنبه"
    5 -> "پنج شنبه"
    else -> "جمعه"
}

private fun getPersianMonthName(index: Int): String = when (index) {
    1 -> "فروردین"
    2 -> "اردیبهشت"
    3 -> "خرداد"
    4 -> "تیر"
    5 -> "مرداد"
    6 -> "شهریور"
    7 -> "مهر"
    8 -> "آبان"
    9 -> "آذر"
    10 -> "دی"
    11 -> "بهمن"
    else -> "اسفند"
}

fun PersianDate.getDateClassified(): String {
    val todayCalendar = Calendar.getInstance()
    val targetCalendar = Calendar.getInstance().apply { timeInMillis = this@getDateClassified.time }

    return when {
        //today > (HH:MM) 23:59
        this.dayUntilToday == 0L -> {
            "%02d:%02d".applyValue(hour, minute)
        }
        //more than 1 year > (yy.MM.DD) 01.12.31
        todayCalendar.get(Calendar.YEAR) != targetCalendar.get(Calendar.YEAR) -> {
            "%02d/%02d/%02d".applyValue(shYear % 100, shMonth, shDay)
        }
        //same week > (week-day name) جمعه
        todayCalendar.get(Calendar.WEEK_OF_YEAR) == targetCalendar.get(Calendar.WEEK_OF_YEAR) -> {
            getPersianWeekDayName(dayOfWeek())
        }
        //same year > (dd month-name) 29 اسفند
        else -> {
            "%02d %s".applyValue(
                shDay,
                getPersianMonthName(shMonth)
            )
        }
    }
}

/**
 * @return =>
 *  in this year: 25 شهریور
 *  past(or feature) years:  9 مهر 1401
 */
fun PersianDate.getDateClassifiedByDayMothYear(): String {
    val todayCalendar = Calendar.getInstance()
    val targetCalendar =
        Calendar.getInstance().apply { timeInMillis = this@getDateClassifiedByDayMothYear.time }

    return when {
        //this year > (yy.MM.DD) 9 مهر 1401
        todayCalendar.get(Calendar.YEAR) != targetCalendar.get(Calendar.YEAR) -> {
            "%02d.%02d.%02d".applyValue(shYear % 100, shMonth, shDay)
        }
        //same year > (dd month-name) 29 اسفند
        else -> {
            "%02d %s".applyValue(
                shDay,
                getPersianMonthName(shMonth)
            )
        }
    }
}

//fun AppCompatActivity.showCalendarDialog(
//    fromNow: Boolean = false,
//    listener: (timestamp: Long?) -> Unit
//) {
//    val calendar = PersianCalendar()
//    calendar.setPersian(1358, Month.FARVARDIN, 1)
//    val start = calendar.timeInMillis
//    calendar.setPersian(1500, Month.ESFAND, 29)
//    val end = calendar.timeInMillis
//
//    val openAt = PersianCalendar.getToday().timeInMillis
//    val constraints = CalendarConstraints.Builder()
//        .setStart(if (fromNow) openAt else start)
//        .setEnd(end)
//        .setOpenAt(openAt)
//
//    if (fromNow)
//        constraints.setValidator(DateValidatorPointForward.from(openAt))
//
//
//    MaterialDatePicker.Builder
//        .datePicker()
//        .setTitleText("select date")
//        .setCalendarConstraints(constraints.build()).build().apply {
//            addOnPositiveButtonClickListener {
//                listener(selection)
//            }
//        }
//        .show(supportFragmentManager, "MaterialDatePicker")
//}

fun Long.toMilliSecond(): Long =
    this * 1000


fun isSameDay(timestamp1: Long, timestamp2: Long): Boolean {
    val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val date1 = format.format(Date(timestamp1))
    val date2 = format.format(Date(timestamp2))
    return date1 == date2
}

fun Long.formatUnixTime(): String {
    val dateFormat = SimpleDateFormat("HH:mm:ss:SSS", Locale.getDefault())
    return dateFormat.format(Date(this))
}

fun Long.checkTime(): DATETIME {
    val yesterday = Calendar.getInstance().timeInMillis - 86400000

    return when (PersianDate(this).getDateString()) {
        PersianDate().getDateString() -> {
            DATETIME.TODAY
        }

        PersianDate(yesterday).getDateString() -> {
            DATETIME.YESTERDAY
        }

        else -> {
            DATETIME.LAST_DAYS
        }
    }
}

fun Long.getDateTime(context: Context): String {
    return when (checkTime()) {
        DATETIME.TODAY -> context.getString(R.string.today)
        DATETIME.YESTERDAY -> context.getString(R.string.yesterday)
        DATETIME.LAST_DAYS -> PersianDate(this).getDateClassifiedByDayMothYear()
    }
}

enum class DATETIME {
    TODAY, YESTERDAY, LAST_DAYS
}

private fun isSummerTime(): Boolean {
    val currentTimeZone = TimeZone.getDefault()
    val currentTimeMillis = System.currentTimeMillis()
    val currentDate = Date(currentTimeMillis)

    return currentTimeZone.inDaylightTime(currentDate)
}

fun Long.fixSummerTime(): Long {
    return if (isSummerTime()) {
        this - 3600000L
    } else this
}