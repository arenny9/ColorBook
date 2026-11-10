package com.colorbook.app

import android.content.Context

object Profile {

    private const val PREFS = "profile"
    private const val KEY_NAME = "name"

    fun name(ctx: Context): String =
        ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getString(KEY_NAME, "Tú") ?: "Tú"

    fun save(ctx: Context, name: String) {
        ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().putString(KEY_NAME, name).apply()
    }
}
