package com.example.zambosafe.ui

import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.zambosafe.R
import com.example.zambosafe.model.Agency
import com.example.zambosafe.util.ActionLauncher
import com.example.zambosafe.util.ConfirmPopup
import com.google.android.material.button.MaterialButton

/** Binds each Agency to a card (layout/item_agency.xml). Call submitList() to update. */
class AgencyAdapter : ListAdapter<Agency, AgencyAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val category: OutlineTextView = view.findViewById(R.id.categoryText)
        val name: TextView = view.findViewById(R.id.nameText)
        val location: TextView = view.findViewById(R.id.locationText)
        val phone: TextView = view.findViewById(R.id.phoneText)
        val callButton: MaterialButton = view.findViewById(R.id.callButton)
        val facebookButton: MaterialButton = view.findViewById(R.id.facebookButton)
        val websiteButton: MaterialButton = view.findViewById(R.id.websiteButton)

        val notes: TextView = view.findViewById(R.id.notesText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_agency, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val agency = getItem(position)
        val context = holder.itemView.context
        holder.notes.text = agency.notes
        holder.notes.visibility =
            if (agency.notes.isNullOrBlank()) View.GONE else View.VISIBLE

        bindCategory(holder.category, agency.category)
        holder.name.text = agency.name

        holder.location.text = agency.location
        holder.location.visibility = if (agency.location.isEmpty()) View.GONE else View.VISIBLE

        holder.phone.text = agency.phone
        holder.phone.visibility = if (agency.phone == null) View.GONE else View.VISIBLE

        bindAction(holder.callButton, agency.phone, context.getString(R.string.confirm_call, agency.name)) {
            ActionLauncher.dial(context, it)
        }
        bindAction(holder.facebookButton, agency.facebookUrl, context.getString(R.string.confirm_facebook, agency.name)) {
            ActionLauncher.openUrl(context, it)
        }
        bindAction(holder.websiteButton, agency.websiteUrl, context.getString(R.string.confirm_website, agency.name)) {
            ActionLauncher.openUrl(context, it)
        }
    }

    /** Colors the category tag: Police red+blue, Fire orange, Medical red, Disaster Risk yellow w/ black outline, Utilities green. */
    private fun bindCategory(view: OutlineTextView, category: String) {
        val context = view.context
        fun color(id: Int) = ContextCompat.getColor(context, id)
        val label = category.uppercase()
        view.setOutline(null)
        when (category.lowercase()) {
            "police" -> {
                val half = label.length / 2
                val spannable = SpannableString(label)
                spannable.setSpan(ForegroundColorSpan(color(R.color.tag_red)), 0, half, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                spannable.setSpan(ForegroundColorSpan(color(R.color.tag_blue)), half, label.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                view.text = spannable
            }
            "disaster risk" -> {
                view.text = label
                view.setTextColor(color(R.color.tag_yellow))
                view.setOutline(color(R.color.tag_outline), 3f * context.resources.displayMetrics.density)
            }
            else -> {
                view.text = label
                val id = when (category.lowercase()) {
                    "fire" -> R.color.tag_orange
                    "medical" -> R.color.tag_red
                    "utilities" -> R.color.tag_green
                    else -> R.color.zs_secondary
                }
                view.setTextColor(color(id))
            }
        }
    }

    /** Shows the button only if the agency has a value, and wires its click. */
    private fun bindAction(button: MaterialButton, value: String?, confirmMessage: String, onClick: (String) -> Unit) {
        if (value == null) {
            button.visibility = View.GONE
            button.setOnClickListener(null)
        } else {
            button.visibility = View.VISIBLE
            button.setOnClickListener { ConfirmPopup.show(button, confirmMessage) { onClick(value) } }
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<Agency>() {
        override fun areItemsTheSame(old: Agency, new: Agency) = old.id == new.id
        override fun areContentsTheSame(old: Agency, new: Agency) = old == new
    }
}
