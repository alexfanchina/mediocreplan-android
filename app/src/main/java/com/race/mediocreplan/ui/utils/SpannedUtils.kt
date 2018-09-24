package com.race.mediocreplan.ui.utils

import android.text.Html
import android.os.Build
import android.text.Spanned


abstract class SpannedUtils {

    companion object {
        @JvmStatic
        @SuppressWarnings("deprecation")
        fun fromHtml(html: String): Spanned {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
            } else {
                Html.fromHtml(html)
            }
        }
    }
}