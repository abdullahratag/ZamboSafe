package com.example.zambosafe.model

/**
 * One emergency agency / hotline entry shown as a card in the list.
 *
 * Optional fields (phone, facebookUrl, websiteUrl) are null when the agency
 * doesn't have one. The card hides the matching button automatically.
 */
data class Agency(
    val id: String,
    val name: String,
    val category: String,
    val location: String,
    val phone: String?,
    val facebookUrl: String?,
    val websiteUrl: String?,
    val notes: String? = null
) {
    /**
     * Search rule used by the keyword filter. Case-insensitive; matches if the
     * text appears in the name, category, location or phone number.
     * To search more fields, add them to the list below.
     */
    fun matches(query: String): Boolean {
        val q = query.trim()
        if (q.isEmpty()) return true
        return listOfNotNull(name, category, location, phone)
            .any { it.contains(q, ignoreCase = true) }
    }
}
