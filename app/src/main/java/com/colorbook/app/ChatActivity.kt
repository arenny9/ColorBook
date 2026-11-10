package com.colorbook.app

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ChatActivity : AppCompatActivity() {

    private data class Msg(val text: String, val mine: Boolean)

    private val msgs = mutableListOf(
        Msg("👋 ¡Hola! Este es un chat de demo.", false),
        Msg("Escríbeme algo y verás cómo respondo 😎", false)
    )

    private val replies = listOf(
        "¡Jajaja! 😄",
        "Cuéntame más 👀",
        "Totalmente de acuerdo 💯",
        "¿Y luego qué pasó?",
        "Con letras de colores todo se entiende mejor 🌈",
        "¡Chido! ✨"
    )

    private lateinit var adapter: ChatAdapter

    inner class ChatAdapter(private val theme: ThemePrefs.Theme) :
        RecyclerView.Adapter<ChatAdapter.Holder>() {

        inner class Holder(v: View) : RecyclerView.ViewHolder(v) {
            val bubble: TextView = v.findViewById(R.id.bubble)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder =
            Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false))

        override fun getItemCount(): Int = msgs.size

        override fun onBindViewHolder(holder: Holder, position: Int) {
            val msg = msgs[position]
            holder.bubble.text = msg.text

            val bg = GradientDrawable()
            bg.cornerRadius = 28f * holder.itemView.resources.displayMetrics.density
            if (msg.mine) {
                bg.setColor(theme.accent)
                holder.bubble.setTextColor(0xFFFFFFFF.toInt())
            } else {
                bg.setColor(theme.card)
                holder.bubble.setTextColor(theme.text)
            }
            holder.bubble.background = bg

            val params = holder.bubble.layoutParams as FrameLayout.LayoutParams
            params.gravity = if (msg.mine) Gravity.END else Gravity.START
            holder.bubble.layoutParams = params
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val theme = ThemePrefs.current(this)
        window.statusBarColor = theme.bg
        setContentView(R.layout.activity_chat)
        findViewById<LinearLayout>(R.id.root).setBackgroundColor(theme.bg)
        findViewById<TextView>(R.id.heading).setTextColor(theme.text)

        adapter = ChatAdapter(theme)
        val list = findViewById<RecyclerView>(R.id.list)
        list.layoutManager = LinearLayoutManager(this)
        list.adapter = adapter

        val input = findViewById<EditText>(R.id.input)
        input.setHintTextColor(theme.subtext)
        input.setTextColor(theme.text)
        input.hint = getString(R.string.chat_hint)

        val send = findViewById<Button>(R.id.send)
        send.backgroundTintList = android.content.res.ColorStateList.valueOf(theme.accent)
        send.setOnClickListener {
            val text = input.text.toString().trim()
            if (text.isEmpty()) return@setOnClickListener
            msgs.add(Msg(text, true))
            adapter.notifyItemInserted(msgs.size - 1)
            list.scrollToPosition(msgs.size - 1)
            input.setText("")
            Handler(Looper.getMainLooper()).postDelayed({
                msgs.add(Msg(replies.random(), false))
                adapter.notifyItemInserted(msgs.size - 1)
                list.scrollToPosition(msgs.size - 1)
            }, 700)
        }
    }
}
