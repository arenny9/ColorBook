package com.colorbook.app

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

class PostAdapter(
    private val posts: List<Post>,
    private val theme: ThemePrefs.Theme
) : RecyclerView.Adapter<PostAdapter.Holder>() {

    class Holder(v: View) : RecyclerView.ViewHolder(v) {
        val card: MaterialCardView = v.findViewById(R.id.card)
        val avatar: TextView = v.findViewById(R.id.avatar)
        val author: TextView = v.findViewById(R.id.author)
        val time: TextView = v.findViewById(R.id.time)
        val body: TextView = v.findViewById(R.id.body)
        val like: TextView = v.findViewById(R.id.like)
        val comment: TextView = v.findViewById(R.id.comment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder =
        Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false))

    override fun getItemCount(): Int = posts.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val post = posts[position]
        holder.card.setCardBackgroundColor(theme.card)

        val avatarBg = GradientDrawable()
        avatarBg.shape = GradientDrawable.OVAL
        avatarBg.setColor(ColorText.colorOfSeed(post.author))
        holder.avatar.background = avatarBg
        holder.avatar.text = post.author.trim().take(1).uppercase()

        holder.author.text = post.author
        holder.author.setTextColor(ColorText.colorOfSeed(post.author, 0.55f, 0.95f))

        holder.time.text = timeAgo(post.time)
        holder.time.setTextColor(theme.subtext)

        holder.body.text = ColorText.apply(post.text, post.style, post.color)

        holder.like.text = "👍 Me gusta · ${post.likes}"
        holder.comment.text = "💬 Comentar"
        holder.like.setTextColor(if (post.liked) theme.accent else theme.subtext)
        holder.comment.setTextColor(theme.subtext)

        holder.like.setOnClickListener {
            post.liked = !post.liked
            post.likes += if (post.liked) 1 else -1
            holder.like.text = "👍 Me gusta · ${post.likes}"
            holder.like.setTextColor(if (post.liked) theme.accent else theme.subtext)
        }
    }

    private fun timeAgo(then: Long): String {
        val seconds = (System.currentTimeMillis() - then) / 1000
        return when {
            seconds < 60 -> "hace un momento"
            seconds < 3600 -> "hace ${seconds / 60} min"
            seconds < 86400 -> "hace ${seconds / 3600} h"
            else -> "hace ${seconds / 86400} d"
        }
    }
}
