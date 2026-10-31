package com.example.zambosafe.ui

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import android.widget.TextView
import com.example.zambosafe.R

/**
 * Shows a small bubble ABOVE a pressed button ("Calling ...", "Opening website ...")
 * as confirmation, then runs the real action after a short moment.
 * Taps made while a bubble is still showing are ignored (prevents double launches).
 */
object ActionHint {

    /** How long the bubble stays visible before the action starts. */
    private const val SHOW_MILLIS = 700L

    private val handler = Handler(Looper.getMainLooper())
    private var current: PopupWindow? = null

    fun show(anchor: View, message: String, action: () -> Unit) {
        if (current?.isShowing == true) return
        if (!anchor.isAttachedToWindow) {
            action()
            return
        }

        val content = LayoutInflater.from(anchor.context)
            .inflate(R.layout.popup_action_hint, null) as TextView
        content.text = message
        content.measure(
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )

        val gap = (6 * anchor.resources.displayMetrics.density).toInt()
        val popup = PopupWindow(
            content,
            content.measuredWidth,
            content.measuredHeight,
            false
        )
        popup.isTouchable = false
        current = popup

        popup.showAsDropDown(
            anchor,
            (anchor.width - content.measuredWidth) / 2,
            -(anchor.height + content.measuredHeight + gap)
        )

        handler.postDelayed({
            if (popup.isShowing) popup.dismiss()
            if (current === popup) current = null
            action()
        }, SHOW_MILLIS)
    }
}
