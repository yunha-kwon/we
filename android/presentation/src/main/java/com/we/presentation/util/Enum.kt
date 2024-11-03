package com.we.presentation.util

import android.content.Context
import com.presentation.component.custom.DropDownData
import com.we.presentation.R

enum class CalendarType {
    BEFORE,
    CURRENT,
    TODAY,
    AFTER
}

enum class ScheduleRegisterType{
    CONTENT,
    LOCATION,
    DATE,
    PRICE
}

/**
 * true -> visible
 * false -> gone
 */
enum class Page(val hideBottomNav: Boolean) {
    HOME(true),
    SCHEDULE(true),
    MY_PAGE(true),
    SCHEDULE_REGISTER(false),

    ;


    companion object {
        fun fromId(id: Int): Page? = entries.find { it.id == id }
    }

    val id: Int
        get() = when (this) {
            HOME -> R.id.homeFragment
            SCHEDULE -> R.id.scheduleFragment
            MY_PAGE -> R.id.myPageFragment
            SCHEDULE_REGISTER -> R.id.scheduleRegisterFragment
        }
}

enum class DropDownMenu(private val value: Int, private val resourceId: Int) {
    UPDATE(0, R.string.drop_down_menu_update),
    DELETE(1, R.string.drop_down_menu_delete);

    fun getText(context: Context): String {
        return context.getString(resourceId)
    }

    companion object {
        fun getMenu(value: Int): DropDownMenu = entries.find { it.value == value } ?: UPDATE

        fun getDropDown(value: List<Int>, context: Context): List<DropDownData> {
            val result = mutableListOf<DropDownData>()
            value.forEach { id ->
                val menu = getMenu(id)
                result.add(DropDownData(menu.getText(context), menu))
            }
            return result
        }

    }

}