package com.colorbook.app

import android.content.Context
import android.graphics.Color

object ThemePrefs {

    data class Theme(
        val id: String,
        val label: String,
        val emoji: String,
        val bg: Int,
        val card: Int,
        val text: Int,
        val subtext: Int,
        val accent: Int
    )

    private const val PREFS = "theme"
    private const val KEY_ID = "id"
    private const val KEY_APPLIED = "applied"

    val ALL = listOf(
        Theme("light", "Claro", "☀️", Color.parseColor("#F0F2F5"), Color.WHITE, Color.parseColor("#0B0F19"), Color.parseColor("#65676B"), Color.parseColor("#1877F2")),
        Theme("dark", "Oscuro", "🌙", Color.parseColor("#18191A"), Color.parseColor("#242526"), Color.parseColor("#E4E6EB"), Color.parseColor("#B0B3B8"), Color.parseColor("#45BD62")),
        Theme("pink", "Rosa", "🌸", Color.parseColor("#FFF0F5"), Color.WHITE, Color.parseColor("#4A1C33"), Color.parseColor("#94576F"), Color.parseColor("#E91E63")),
        Theme("blue", "Azul noche", "🌌", Color.parseColor("#0A1E3C"), Color.parseColor("#132B52"), Color.parseColor("#D8E6FF"), Color.parseColor("#7FA3D9"), Color.parseColor("#4FC3F7")),
        Theme("green", "Selva", "🌿", Color.parseColor("#0E1F17"), Color.parseColor("#17301F"), Color.parseColor("#DDF5E4"), Color.parseColor("#8CB99A"), Color.parseColor("#66BB6A")),
        Theme("sunset", "Atardecer", "🌇", Color.parseColor("#251309"), Color.parseColor("#3A2211"), Color.parseColor("#FFE9D6"), Color.parseColor("#C79A7B"), Color.parseColor("#FF7043"))
    )

    fun current(ctx: Context): Theme {
        val id = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getString(KEY_ID, "light")
        return ALL.firstOrNull { it.id == id } ?: ALL[0]
    }

    fun save(ctx: Context, id: String) {
        ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().putString(KEY_ID, id).apply()
    }

    fun applied(ctx: Context): String =
        ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getString(KEY_APPLIED, "") ?: ""

    fun markApplied(ctx: Context) {
        ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit()
            .putString(KEY_APPLIED, current(ctx).id).apply()
    }
}
