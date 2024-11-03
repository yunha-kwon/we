package com.we.presentation.util

import timber.log.Timber
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

fun LocalDate.toYearMonth(): Pair<String, String> {
    val date = this.toString().split("-")
    return Pair(date[0], date[1])
}


fun String.convertIsoToLocalDate(): LocalDate? {
    return runCatching {
        OffsetDateTime.parse(this, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    }.map { offsetDateTime ->
        offsetDateTime.toLocalDate()
    }.onFailure {
        Timber.d("에러 처리 ${it.printStackTrace()}")
    }.getOrNull()
}

fun setYearMonthDay(year : Int, month : Int, day : Int) : String{
    return "${year}년 ${month}월 ${day}일"
}


/**
 * "yyyy년 MM월 dd일" 형식의 날짜 문자열을 ISO 8601 형식으로 변환하는 확장 함수.
 *
 * @param time 시간 정보 (기본값: 00:00:00.000)
 * @param zoneId 타임존 (기본값: UTC)
 * @return ISO 8601 형식의 날짜-시간 문자열 또는 변환 실패 시 null
 */
fun String.toIso8601(
    time: LocalTime = LocalTime.MIDNIGHT,
    zoneId: ZoneId = ZoneOffset.UTC
): String? {
    val originalFormatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일", Locale.KOREAN)
    val isoFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")

    return try {
        val localDate = LocalDate.parse(this, originalFormatter)
        val localDateTime = LocalDateTime.of(localDate, time)
        val instant = localDateTime.atZone(zoneId).format(isoFormatter)

        instant.toString()
    } catch (e: DateTimeParseException) {

        println("날짜 형식이 올바르지 않습니다: ${e.message}")
        null
    }
}

fun String.toYearMonthDay(): String {

    val parsedDate = ZonedDateTime.parse(this)

    val formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일", Locale.KOREAN)

    return parsedDate.format(formatter)
}