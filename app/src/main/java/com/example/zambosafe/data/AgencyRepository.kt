package com.example.zambosafe.data

import android.content.Context
import android.util.Log
import com.example.zambosafe.model.Agency
import org.json.JSONArray
import org.json.JSONObject

/**
 * Loads the agency directory from the bundled JSON file
 * (app/src/main/assets/agencies.json). No network or database involved.
 */
object AgencyRepository {

    private const val TAG = "AgencyRepository"
    private const val FILE_NAME = "agencies.json"

    fun load(context: Context): List<Agency> {
        return try {
            val text = context.assets.open(FILE_NAME).bufferedReader().use { it.readText() }
            parse(text)
        } catch (e: Exception) {
            Log.e(TAG, "Could not load $FILE_NAME", e)
            emptyList()
        }
    }

    /** Separate from load() so it can be unit-tested without a Context. */
    fun parse(json: String): List<Agency> {
        val array = JSONArray(json)
        return (0 until array.length()).map { toAgency(array.getJSONObject(it)) }
    }

    private fun toAgency(o: JSONObject) = Agency(
        id = o.getString("id"),
        name = o.getString("name"),
        category = o.getString("category"),
        location = o.optNonBlank("location") ?: "",
        phone = o.optNonBlank("phone"),
        facebookUrl = o.optNonBlank("facebook"),
        websiteUrl = o.optNonBlank("website"),
        notes = o.optNonBlank("notes")
    )

    // Missing key, JSON null and "" are all treated as "not provided".
    private fun JSONObject.optNonBlank(key: String): String? =
        if (isNull(key)) null else optString(key).trim().ifEmpty { null }
}
