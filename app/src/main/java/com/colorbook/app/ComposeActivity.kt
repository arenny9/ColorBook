package com.colorbook.app

import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ComposeActivity : AppCompatActivity() {

    private val palette = intArrayOf(
        0xFF7C4DFF.toInt(), 0xFFE91E63.toInt(), 0xFFFF7043.toInt(), 0xFF29B6F6.toInt(),
        0xFF66BB6A.toInt(), 0xFFFFEB3B.toInt(), 0xFFFF4081.toInt(), 0xFF26C6DA.toInt()
    )

    private var style = Post.STYLE_RAINBOW
    private var color = 0xFF7C4DFF.toInt()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val theme = ThemePrefs.current(this)
        window.statusBarColor = theme.bg
        setContentView(R.layout.activity_compose)
        findViewById<LinearLayout>(R.id.root).setBackgroundColor(theme.bg)

        val preview = findViewById<TextView>(R.id.preview)
        val input = findViewById<EditText>(R.id.input)

        val previewBg = GradientDrawable()
        previewBg.cornerRadius = 24f * resources.displayMetrics.density
        previewBg.setColor(theme.card)
        preview.background = previewBg

        input.setHintTextColor(theme.subtext)
        input.setTextColor(theme.text)
        for (id in intArrayOf(R.id.rb1, R.id.rb2, R.id.rb3)) {
            findViewById<RadioButton>(id).setTextColor(theme.text)
        }
        findViewById<TextView>(R.id.heading).setTextColor(theme.text)
        findViewById<TextView>(R.id.styleLabel).setTextColor(theme.text)
        findViewById<TextView>(R.id.colorLabel).setTextColor(theme.text)

        val publish = findViewById<Button>(R.id.publish)
        publish.backgroundTintList = ColorStateList.valueOf(theme.accent)
        publish.setOnClickListener {
            val text = input.text.toString().trim()
            if (text.isEmpty()) {
                input.error = getString(R.string.write_something)
                return@setOnClickListener
            }
            PostStore.add(
                this,
                Post(text, style, color, Profile.name(this), System.currentTimeMillis())
            )
            finish()
        }

        fun update() {
            val text = input.text.toString().trim()
            if (text.isEmpty()) {
                preview.setTextColor(theme.subtext)
                preview.text = getString(R.string.preview_empty)
            } else {
                preview.text = ColorText.apply(text, style, color)
            }
        }

        input.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                update()
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {}
        })

        findViewById<RadioGroup>(R.id.styles).setOnCheckedChangeListener { _, checkedId ->
            style = when (checkedId) {
                R.id.rb2 -> Post.STYLE_GRADIENT
                R.id.rb3 -> Post.STYLE_SOLID
                else -> Post.STYLE_RAINBOW
            }
            update()
        }

        val paletteRow = findViewById<LinearLayout>(R.id.palette)
        for (swatch in palette) {
            val view = View(this)
            val size = (28 * resources.displayMetrics.density).toInt()
            val params = LinearLayout.LayoutParams(size, size)
            params.marginEnd = (12 * resources.displayMetrics.density).toInt()
            view.layoutParams = params
            val dot = GradientDrawable()
            dot.shape = GradientDrawable.OVAL
            dot.setColor(swatch)
            view.background = dot
            view.setOnClickListener {
                color = swatch
                update()
            }
            paletteRow.addView(view)
        }

        update()
    }
}
