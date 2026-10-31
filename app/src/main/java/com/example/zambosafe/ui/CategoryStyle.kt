package com.example.zambosafe.ui

import android.graphics.drawable.GradientDrawable
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.zambosafe.R

/**
 * Colours for the category tag on each card.
 * To restyle a category, edit its entry in [styleFor] (colors live in res/values/colors.xml).
 * Categories not listed here keep the plain gray caption look.
 */
object CategoryStyle {

    private class Style(
        val background: Int,
        val textColors: List<Int>,   // letters cycle through these (1 colour = solid text)
        val stroke: Int? = null
    )

    private fun styleFor(textView: TextView, category: String): Style? {
        fun c(id: Int) = ContextCompat.getColor(textView.context, id)
        return when (category.lowercase()) {
            "police" -> Style(
                c(R.color.zs_primary),
                listOf(c(R.color.tag_red), c(R.color.tag_blue))
            )
            "fire" -> Style(
                c(R.color.zs_primary),
                listOf(c(R.color.tag_yellow), c(R.color.tag_red), c(R.color.tag_orange))
            )
            "medical" -> Style(
                c(R.color.zs_primary),
                listOf(c(R.color.tag_red)),
                stroke = c(R.color.tag_red)
            )
            "disaster risk" -> Style(
                c(R.color.zs_primary),
                listOf(c(R.color.tag_yellow))
            )
            "utilities" -> Style(
                c(R.color.zs_primary),
                listOf(c(R.color.tag_green))
            )
            else -> null
        }
    }

    /** Sets the (upper-cased) category text and applies its tag colours. */
    fun apply(textView: TextView, category: String) {
        val label = category.uppercase()
        val style = styleFor(textView, category)
        val density = textView.resources.displayMetrics.density

        if (style == null) {
            textView.text = label
            textView.background = null
            textView.setPadding(0, 0, 0, 0)
            return
        }

        val spannable = SpannableString(label)
        label.forEachIndexed { i, _ ->
            spannable.setSpan(
                ForegroundColorSpan(style.textColors[i % style.textColors.size]),
                i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        textView.text = spannable

        textView.background = GradientDrawable().apply {
            setColor(style.background)
            cornerRadius = 6 * density
            style.stroke?.let { setStroke((1 * density).toInt(), it) }
        }
        val h = (8 * density).toInt()
        val v = (3 * density).toInt()
        textView.setPadding(h, v, h, v)
    }
}
