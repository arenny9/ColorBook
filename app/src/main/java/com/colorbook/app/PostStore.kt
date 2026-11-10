package com.colorbook.app

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

data class Post(
    val text: String,
    val style: String,
    val color: Int,
    val author: String,
    val time: Long,
    var likes: Int = 0,
    var liked: Boolean = false
) {
    companion object {
        const val STYLE_RAINBOW = "rainbow"
        const val STYLE_GRADIENT = "gradient"
        const val STYLE_SOLID = "solid"
    }
}

object PostStore {

    private const val PREFS = "posts"
    private const val KEY = "list"
    private const val KEY_SEEDED = "seeded"

    fun load(ctx: Context): MutableList<Post> {
        val prefs = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        if (!prefs.getBoolean(KEY_SEEDED, false)) {
            val seeds = mutableListOf<Post>()
            seed(seeds)
            save(ctx, seeds)
            return seeds
        }
        val list = mutableListOf<Post>()
        val arr = JSONArray(prefs.getString(KEY, "[]"))
        for (i in 0 until arr.length()) {
            val o = arr.getJSONObject(i)
            list.add(
                Post(
                    o.getString("text"),
                    o.getString("style"),
                    o.getInt("color"),
                    o.getString("author"),
                    o.getLong("time"),
                    o.getInt("likes"),
                    o.getBoolean("liked")
                )
            )
        }
        return list
    }

    fun add(ctx: Context, post: Post) {
        val current = load(ctx)
        current.add(0, post)
        save(ctx, current)
    }

    fun save(ctx: Context, posts: List<Post>) {
        val arr = JSONArray()
        for (p in posts) {
            arr.put(JSONObject().apply {
                put("text", p.text)
                put("style", p.style)
                put("color", p.color)
                put("author", p.author)
                put("time", p.time)
                put("likes", p.likes)
                put("liked", p.liked)
            })
        }
        ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit()
            .putString(KEY, arr.toString())
            .putBoolean(KEY_SEEDED, true)
            .apply()
    }

    private fun seed(list: MutableList<Post>) {
        val now = System.currentTimeMillis()
        val min = 60_000L
        val hour = 3_600_000L
        list.add(Post("¡Ya probé todas las letras de colores! Esta app está increíble 🔥", Post.STYLE_RAINBOW, 0xFF7C4DFF.toInt(), "Ana Verde", now - 6 * min, 23))
        list.add(Post("El viernes llegó con estrellitas ✨ y degradado de atardecer", Post.STYLE_GRADIENT, 0xFFFF7043.toInt(), "Luis Ramírez", now - 3 * hour, 15))
        list.add(Post("Cambien el tema a rosa porque las flores también escriben 🌸", Post.STYLE_SOLID, 0xFFE91E63.toInt(), "Majo", now - 9 * hour, 31))
        list.add(Post("¿Alguien más siente que el mundo se ve mejor con arcoíris en el texto? 🌈🌎💚💙💜", Post.STYLE_RAINBOW, 0xFF26C6DA.toInt(), "Nico", now - 26 * hour, 42))
    }
}
