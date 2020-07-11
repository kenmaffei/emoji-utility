package com.ken_maffei.emojiutility

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tv: TextView = findViewById(R.id.textView)

        EmojiUtility.registerEmojiFromDrawableResource(this, "@e1", R.drawable.circle_gradient)
        EmojiUtility.registerEmojiFromLayout(this, "@e2", R.layout.emoji_layout)

        val testString = "Creating emojis @e1 with Kotlin @e2"
        EmojiUtility.processString(testString, tv)
    }

    override fun onDestroy() {
        EmojiUtility.clear()
        super.onDestroy()
    }
}