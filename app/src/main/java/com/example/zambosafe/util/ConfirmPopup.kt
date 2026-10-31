package com.example.zambosafe.util

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import com.example.zambosafe.R

/** Small "Are you sure...?" popup that hovers above the button that was pressed. */
object ConfirmPopup {

    fun show(anchor: View, message: String, onConfirm: () -> Unit) {
        val context = anchor.context
        val density = context.resources.displayMetrics.density
        val screenWidth = context.resources.displayMetrics.widthPixels
        val margin = (8 * density).toInt()

        val content = LayoutInflater.from(context).inflate(R.layout.popup_confirm, null)
        content.findViewById<TextView>(R.id.popupMessage).text = message

        val popup = PopupWindow(
            content, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true
        )
        popup.elevation = 8 * density
        popup.isOutsideTouchable = true

        content.findViewById<View>(R.id.popupCancel).setOnClickListener { popup.dismiss() }
        content.findViewById<View>(R.id.popupConfirm).setOnClickListener {
            popup.dismiss()
            onConfirm()
        }

        // Measure so we can place it right above the button.
        val maxWidth = minOf(screenWidth - 2 * margin, (320 * density).toInt())
        content.measure(
            View.MeasureSpec.makeMeasureSpec(maxWidth, View.MeasureSpec.AT_MOST),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        val w = content.measuredWidth
        val h = content.measuredHeight

        val loc = IntArray(2)
        anchor.getLocationOnScreen(loc)
        val x = (loc[0] + anchor.width / 2 - w / 2).coerceIn(margin, screenWidth - w - margin)
        var y = loc[1] - h - margin
        if (y < margin) y = loc[1] + anchor.height + margin // no room above: show below

        popup.showAtLocation(anchor, Gravity.NO_GRAVITY, x, y)
    }
}
