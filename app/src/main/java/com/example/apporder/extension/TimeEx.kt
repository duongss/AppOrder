package com.example.apporder.extension

import androidx.fragment.app.FragmentManager
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

const val timeFormat1 = "dd-MM-yyyy"
const val timeFormat2 = "HH:mm"
const val timeFormat3 = "MM-dd-yyyy HH-mm-ss"
const val timeFormat4 = "MM-dd"
const val timeFormat5 = "yyyy"
const val timeFormat6 = "MM/dd/yyyy"
const val timeFormat7 = "MM-dd-yyyy HH:mm:ss"
const val timeFormat8 = "mm"

fun getTimeFromDuration(millis: Long): String {
    if (millis / (36000000) >= 1)
        return String.format(
            "%02d:%02d:%02d",
            TimeUnit.MILLISECONDS.toHours(millis),
            TimeUnit.MILLISECONDS.toMinutes(millis) -
                    TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), // The change is in this line
            TimeUnit.MILLISECONDS.toSeconds(millis) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
        )

    return String.format(
        "%02d:%02d",
        TimeUnit.MILLISECONDS.toMinutes(millis) -
                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), // The change is in this line
        TimeUnit.MILLISECONDS.toSeconds(millis) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
    )
}

fun Long.getDateTime(timeFormat: String = timeFormat1): String {
    val formatter = SimpleDateFormat(timeFormat, Locale.ENGLISH)
    val calendar: Calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    return formatter.format(calendar.time)
}


fun formatDateLocale(date: Long): String? {
    val dateFormat =
        DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT, Locale.getDefault())
    return dateFormat.format(date)
}

fun formatDateLocaleDMY(date: Long): String? {
    val dateFormat =
        DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault())
    return dateFormat.format(date)
}

fun formatTimeLocale(date: Long): String? {
    val dateFormat =
        DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault())
    return dateFormat.format(date)
}

fun getDateTimeOfWeek(milliSeconds: Long, timeFormat: String = "EE, dd-MM-yyyy, HH:mm aa"): String {
    val formatter = SimpleDateFormat(timeFormat, Locale.getDefault())
    val calendar: Calendar = Calendar.getInstance()
    calendar.timeInMillis = milliSeconds
    return formatter.format(calendar.time)
}

fun getDate(
    milliSeconds: Long = Calendar.getInstance().timeInMillis,
    dateFormat: String = "MM-dd-yyyy"
): String {
    val formatter = SimpleDateFormat(dateFormat, Locale.ENGLISH)
    val calendar: Calendar = Calendar.getInstance()
    calendar.timeInMillis = milliSeconds
    return formatter.format(calendar.time)
}

fun String.toMillisecond(format: String, locale: Locale = Locale.getDefault()): Long? =
    SimpleDateFormat(format, locale).parse(this)?.time


fun getStartDate(): Long {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, -7)
    return calendar.timeInMillis / 1000
}