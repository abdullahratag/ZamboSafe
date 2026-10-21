package com.example.zambosafe.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.zambosafe.R
import com.example.zambosafe.model.Agency
import com.example.zambosafe.util.ActionLauncher
import com.google.android.material.button.MaterialButton

/** Binds each Agency to a card (layout/item_agency.xml). Call submitList() to update. */
class AgencyAdapter : ListAdapter<Agency, AgencyAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val category: TextView = view.findViewById(R.id.categoryText)
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

        holder.category.text = agency.category.uppercase()
        holder.name.text = agency.name

        holder.location.text = agency.location
        holder.location.visibility = if (agency.location.isEmpty()) View.GONE else View.VISIBLE

        holder.phone.text = agency.phone
        holder.phone.visibility = if (agency.phone == null) View.GONE else View.VISIBLE

        bindAction(holder.callButton, agency.phone) { ActionLauncher.dial(context, it) }
        bindAction(holder.facebookButton, agency.facebookUrl) { ActionLauncher.openUrl(context, it) }
        bindAction(holder.websiteButton, agency.websiteUrl) { ActionLauncher.openUrl(context, it) }
    }

    /** Shows the button only if the agency has a value, and wires its click. */
    private fun bindAction(button: MaterialButton, value: String?, onClick: (String) -> Unit) {
        if (value == null) {
            button.visibility = View.GONE
            button.setOnClickListener(null)
        } else {
            button.visibility = View.VISIBLE
            button.setOnClickListener { onClick(value) }
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<Agency>() {
        override fun areItemsTheSame(old: Agency, new: Agency) = old.id == new.id
        override fun areContentsTheSame(old: Agency, new: Agency) = old == new
    }
}
