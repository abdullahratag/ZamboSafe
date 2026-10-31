package com.example.zambosafe.ui

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/** TextView that can draw an outline around its letters (used for the yellow category tag). */
class OutlineTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = android.R.attr.textViewStyle
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private var outlineColor: Int? = null
    private var outlineWidthPx = 0f

    fun setOutline(color: Int?, widthPx: Float = 0f) {
        outlineColor = color
        outlineWidthPx = widthPx
        invalidate()
    }

    override fun onDraw(canvas: android.graphics.Canvas) {
        val color = outlineColor
        if (color == null) {
            super.onDraw(canvas)
            return
        }
        val originalColors = textColors
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = outlineWidthPx
        paint.strokeJoin = Paint.Join.ROUND
        setTextColor(color)
        super.onDraw(canvas)

        paint.style = Paint.Style.FILL
        setTextColor(originalColors)
        super.onDraw(canvas)
    }
}
