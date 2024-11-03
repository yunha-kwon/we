package com.we.presentation.util

import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

fun functionStatusBarColor(activity: FragmentActivity, color: Int) {
    activity.window.statusBarColor =
        ContextCompat.getColor(activity, color)
}