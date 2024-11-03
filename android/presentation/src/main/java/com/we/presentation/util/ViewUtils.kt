package com.we.presentation.util

import android.animation.ObjectAnimator
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

fun setDistanceX(viewOne: View, viewTwo: View): Float { // 두 View 사이의 거리 구하기
    val viewOneValue = IntArray(2)
    val viewTwoValue = IntArray(2)

    viewOne.getLocationOnScreen(viewOneValue)
    viewTwo.getLocationOnScreen(viewTwoValue)

    return Math.abs(viewOneValue[0] - viewTwoValue[0]).toFloat()
}

fun View.setTransactionX(distance: Float) {
    val anim = ObjectAnimator.ofFloat(this, "translationX", distance)
    anim.start()
}

//ViewPager
fun ViewPager2.addCustomItemDecoration() {
    this.addItemDecoration(object : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            outRect.right = 70.dpToPx
            outRect.left = 70.dpToPx
        }
    })
    val scaleFactor = 0.8f

    val pageTranslationX = 150.dpToPx

    this.setPageTransformer { page, position ->
        page.translationX = -pageTranslationX * (position)

        if (position <= -1 || position >= 1) {
            // 페이지가 보이지 않을 때
            page.scaleX = scaleFactor
            page.scaleY = scaleFactor
        }else{
            // 왼쪽 페이지 && 오른쪽 페이지
            val absolute = 1 - abs(position)
            page.scaleX = scaleFactor + (1 - scaleFactor) * absolute
            page.scaleY = scaleFactor + (1 - scaleFactor) * absolute
        }

    }
}