package com.colorbook.app

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class ThemesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val density = resources.displayMetrics.density
        val theme = ThemePrefs.current(this)
        window.statusBarColor = theme.bg
        setContentView(R.layout.activity_themes)

        findViewById<LinearLayout>(R.id.root).setBackgroundColor(theme.bg)
        findViewById<TextView>(R.id.heading).setTextColor(theme.text)

        findViewById<Button>(R.id.name).setOnClickListener {
            val input = EditText(this)
            input.setText(Profile.name(this))
            AlertDialog.Builder(this)
                .setTitle(getString(R.string.change_name))
                .setView(input)
                .setPositiveButton(getString(R.string.save)) { _, _ ->
                    Profile.save(this, input.text.toString().trim().ifEmpty { "Tú" })
                }
                .setNegativeButton(getString(R.string.cancel), null)
                .show()
        }

        val container = findViewById<LinearLayout>(R.id.themes)
        for (option in ThemePrefs.ALL) {
            val card = TextView(this)
            card.gravity = Gravity.CENTER
            card.text = buildString {
                append(option.emoji)
                append("  ")
                append(option.label)
                if (option.id == theme.id) append("   ✓ activo")
            }
            card.setTextColor(option.text)

            val bg = GradientDrawable()
            bg.cornerRadius = 20f * density
            bg.setColor(option.card)
            card.background = bg

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0, (8 * density).toInt(), 0, 0)
            card.layoutParams = params
            card.setPadding(
                (16 * density).toInt(), (20 * density).toInt(),
                (16 * density).toInt(), (20 * density).toInt()
            )

            card.setOnClickListener {
                ThemePrefs.save(this, option.id)
                recreate()
            }
            container.addView(card)
        }
    }
}
