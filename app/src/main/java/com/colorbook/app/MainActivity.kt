package com.colorbook.app

import android.content.Intent
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var posts: MutableList<Post>
    private lateinit var adapter: PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemePrefs.markApplied(this)
        val theme = ThemePrefs.current(this)
        window.statusBarColor = theme.bg
        setContentView(R.layout.activity_main)
        findViewById<LinearLayout>(R.id.root).setBackgroundColor(theme.bg)

        posts = PostStore.load(this)
        adapter = PostAdapter(posts, theme)

        val list = findViewById<RecyclerView>(R.id.list)
        list.layoutManager = LinearLayoutManager(this)
        list.adapter = adapter

        findViewById<Button>(R.id.compose).setOnClickListener {
            startActivity(Intent(this, ComposeActivity::class.java))
        }
        findViewById<Button>(R.id.btnChat).setOnClickListener {
            startActivity(Intent(this, ChatActivity::class.java))
        }
        findViewById<Button>(R.id.btnThemes).setOnClickListener {
            startActivity(Intent(this, ThemesActivity::class.java))
        }

        gradientTitle()
    }

    override fun onResume() {
        super.onResume()
        if (ThemePrefs.applied(this) != ThemePrefs.current(this).id) {
            recreate()
            return
        }
        val fresh = PostStore.load(this)
        posts.clear()
        posts.addAll(fresh)
        adapter.notifyDataSetChanged()
    }

    private fun gradientTitle() {
        val theme = ThemePrefs.current(this)
        val title = findViewById<TextView>(R.id.title)
        val subtitle = findViewById<TextView>(R.id.subtitle)
        subtitle.setTextColor(theme.subtext)
        title.setTextColor(theme.accent)
        title.post {
            val width = title.width.toFloat()
            if (width > 0) {
                title.paint.shader = LinearGradient(
                    0f, 0f, width, 0f,
                    theme.accent, ColorText.colorOfSeed("magenta"),
                    Shader.TileMode.CLAMP
                )
            }
        }
    }
}
