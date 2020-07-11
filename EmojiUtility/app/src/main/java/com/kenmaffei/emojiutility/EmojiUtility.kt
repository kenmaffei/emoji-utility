package com.ken_maffei.emojiutility

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import android.view.View
import android.widget.TextView
import java.util.regex.Matcher
import java.util.regex.Pattern


/**
 * Created by Ken Maffei on 7/4/20.
 */

object EmojiUtility {
    private val emojiMap: HashMap<String, Drawable> = HashMap();
    private var pattern: Pattern? = null

    fun updatePattern() {
        //Update the pattern
        var patternString = ""
        for ((key, value) in emojiMap) {
            patternString += "$key|"
        }
        pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
    }

    fun registerEmojiFromLayout(activity: Activity, key: String, layoutResource: Int) {
        val cl = activity.layoutInflater.inflate(layoutResource.toInt(), null)

        cl.measure(
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        cl.layout(0, 0, cl.measuredWidth, cl.measuredHeight)
        val bm = Bitmap.createBitmap(cl.measuredWidth, cl.measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bm)
        cl.draw(canvas)
        val drawable = BitmapDrawable(activity.resources, bm)
        drawable.setBounds(0, 0, bm.width, bm.height)

        emojiMap[key] = drawable

        //Update the pattern
        updatePattern()
    }

    fun registerEmojiFromDrawable(key: String, drawable: Drawable) {
        emojiMap[key] = drawable
        updatePattern()
    }

    fun registerEmojiFromBitmap(activity: Activity, key: String, bitmap: Bitmap) {
        val drawable = BitmapDrawable(activity.resources, bitmap)
        drawable.setBounds(0, 0, bitmap.width, bitmap.height)
        emojiMap[key] = drawable
        updatePattern()
    }

    fun registerEmojiFromDrawableResource(activity: Activity, key: String, drawableResource: Int) {
        //Make sure to set a size in your drawable xml
        val drawable: Drawable
        try {
            drawable = Drawable.createFromXml(activity.resources, activity.resources.getXml(drawableResource))
        } catch (ex: Exception) {
           return
        }
        emojiMap[key] = drawable
        updatePattern()
    }

    fun processString(string: String, textView: TextView) {
        val ss = SpannableStringBuilder(string)
        pattern?.let { pattern ->
            val matcher: Matcher = pattern.matcher(ss)
            val textHeight = getTextSize(textView).toInt()
            while (matcher.find()) {
                val start = matcher.start()
                val end = matcher.end()
                val match: String = ss.subSequence(start, end).toString()
                val drawable = emojiMap[match]
                drawable?.let{d ->
                    val scale = textHeight.toFloat()/d.intrinsicHeight.toFloat()
                    d.setBounds(0, 0, (d.intrinsicWidth*scale).toInt(), textHeight)
                    val iSpan = ImageSpan(d, ImageSpan.ALIGN_BOTTOM)
                    ss.setSpan(iSpan, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                }
            }
        }
        textView.text = ss
    }

    private fun getTextSize(textView: TextView): Float {
        val font = textView.typeface
        val p = Paint()
        p.typeface = font
        p.textSize = textView.textSize
        return -p.fontMetrics.ascent + p.fontMetrics.descent
    }

    fun clear() {
        emojiMap.clear()
    }
}