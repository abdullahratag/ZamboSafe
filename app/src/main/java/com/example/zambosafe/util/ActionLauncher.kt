package com.example.zambosafe.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.example.zambosafe.R

/**
 * All implicit intents of the app live here (dialer, Facebook, website),
 * so the adapter and activity stay free of intent code.
 */
object ActionLauncher {

    /** Opens the phone dialer with the number filled in (user still presses call). */
    fun dial(context: Context, phone: String) {
        val number = phone.filter { it.isDigit() || it == '+' || it == '*' || it == '#' }
        launch(context, Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", number, null)))
    }

    /** Opens a web address. Used for both Facebook pages and official websites. */
    fun openUrl(context: Context, url: String) {
        val fixed = if (url.startsWith("http://") || url.startsWith("https://")) url else "https://$url"
        launch(context, Intent(Intent.ACTION_VIEW, Uri.parse(fixed)))
    }

    private fun launch(context: Context, intent: Intent) {
        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, R.string.error_no_app, Toast.LENGTH_SHORT).show()
        }
    }
}
