package com.example.tamtamych

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class TextDisplayView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private var textToDisplay: String = ""
    private var textColor: Int = Color.BLACK
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 64f
        textAlign = Paint.Align.CENTER
    }

    fun setTextAndColor(text: String, color: Int) {
        textToDisplay = text
        textColor = color
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        paint.color = textColor
        val x = width / 2f
        val y = height / 2f - (paint.descent() + paint.ascent()) / 2
        canvas.drawText(textToDisplay, x, y, paint)
    }
}
