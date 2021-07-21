package holofyassignment.support

import android.util.Log
import android.view.View
import android.view.ViewGroup

fun View.setAspectRatio(dividedWidth: Float, aspectRatio: Float, totalMargin: Int) {
    val actualScreenWidth = Utils.getScreenWidth(context)
    val totalSpacingPercent = (totalMargin / 360f)
    val modifiedScreenWidth = ((actualScreenWidth * (1 - totalSpacingPercent)) / dividedWidth).toInt()
    val layoutParams = layoutParams as ViewGroup.LayoutParams
    layoutParams.width = modifiedScreenWidth
    layoutParams.height = (modifiedScreenWidth / aspectRatio).toInt()
    requestLayout()
}