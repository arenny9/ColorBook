package com.colorbook.app

import android.graphics.Color
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan

object ColorText {

    fun apply(text: String, style: String, color: Int): SpannableStringBuilder {
        val sb = SpannableStringBuilder(text)
        val cps = text.codePoints().toArray()
        var offset = 0
        val total = cps.size
        val baseHue = hueOfColor(color)
        for (n in 0 until total) {
            val end = text.offsetByCodePoints(offset, 1)
            val fraction = if (total <= 1) 0f else n.toFloat() / (total - 1)
            val charColor = when (style) {
                Post.STYLE_RAINBOW -> Color.HSVToColor(floatArrayOf((n * 137.508f + fraction * 220f) % 360f, 0.85f, 1f))
                Post.STYLE_GRADIENT -> Color.HSVToColor(floatArrayOf(lerpHue(baseHue, (baseHue + 180f) % 360f, fraction), 0.75f, 1f))
                else -> color
            }
            sb.setSpan(ForegroundColorSpan(charColor), offset, end, SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE)
            offset = end
        }
        return sb
    }

    fun colorOfSeed(seed: String, saturation: Float = 0.55f, brightness: Float = 0.85f): Int =
        Color.HSVToColor(floatArrayOf(hueOfSeed(seed), saturation, brightness))

    fun hueOfSeed(seed: String): Float = (Math.abs(seed.hashCode().toLong()) * 137.508f) % 360f

    private fun hueOfColor(color: Int): Float {
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)
        return hsv[0]
    }

    private fun lerpHue(h1: Float, h2: Float, fraction: Float): Float {
        var delta = h2 - h1
        if (delta > 180f) delta -= 360f
        if (delta < -180f) delta += 360f
        return (h1 + delta * fraction + 360f) % 360f
    }
}
