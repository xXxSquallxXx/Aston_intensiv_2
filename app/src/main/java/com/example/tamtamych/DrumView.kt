package com.example.tamtamych

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import kotlin.math.roundToInt
import kotlin.random.Random

class DrumView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private var adjustedAngle: Float = 0f
    private var colorAtTwelveName: String = context.getString(R.string.unknown_color)
    private val rotationAngle = Random.nextFloat() * 360f
    private var currentAngle = rotationAngle
    var drumScale: Float = 1.0f
        set(value) {
            field = value
            invalidate()
        }

    private val sectorColors = listOf(
        0xFFFF0000.toInt(),
        0xFFFFA500.toInt(),
        0xFFFFFF00.toInt(),
        0xFF008000.toInt(),
        0xFF00FFFF.toInt(),
        0xFF0000FF.toInt(),
        0xFF800080.toInt()
    )

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    var spining = 0f

    private val rectF = RectF()
    private val sectorAngles = listOf(
        Pair(0f, 51.42f),
        Pair(51.43f, 102.86f),
        Pair(102.87f, 154.29f),
        Pair(154.30f, 205.72f),
        Pair(205.73f, 257.15f),
        Pair(257.16f, 308.58f),
        Pair(308.59f, 360f)
    )

    init {
        adjustedAngle = ((270 - rotationAngle + 360) % 360).let {
            (Math.round(it * 100) / 100f)
        }

        val colorNames = mapOf(
            0xFFFF0000.toInt() to context.getString(R.string.sector_red),
            0xFFFFA500.toInt() to context.getString(R.string.sector_orange),
            0xFFFFFF00.toInt() to context.getString(R.string.sector_yellow),
            0xFF008000.toInt() to context.getString(R.string.sector_green),
            0xFF00FFFF.toInt() to context.getString(R.string.sector_cyan),
            0xFF0000FF.toInt() to context.getString(R.string.sector_blue),
            0xFF800080.toInt() to context.getString(R.string.sector_purple)
        )

        for (i in sectorAngles.indices) {
            val (startAngle, endAngle) = sectorAngles[i]
            if (adjustedAngle in startAngle..endAngle) {
                colorAtTwelveName = colorNames[sectorColors[i]] ?: context.getString(R.string.unknown_color)
                break
            }
        }
    }

    fun getColorAtTwelve(): String {
        return colorAtTwelveName
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width.toFloat()
        val height = height.toFloat()
        val radius = (width.coerceAtMost(height) / 2 * 0.9f) * drumScale
        val centerX = width / 2
        val centerY = height / 2

        rectF.set(
            centerX - radius,
            centerY - radius,
            centerX + radius,
            centerY + radius
        )

        canvas.save()
        canvas.rotate(rotationAngle, centerX, centerY)

        for (i in sectorAngles.indices) {
            val (startAngle, endAngle) = sectorAngles[i]
            paint.color = sectorColors[i]
            canvas.drawArc(rectF, startAngle, endAngle - startAngle, true, paint)
        }
        canvas.restore()
    }
    fun spinDrum(spining: Float, onAnimationEnd: (() -> Unit)? = null) {
        currentAngle = (currentAngle + spining) % 360

        val animator = ObjectAnimator.ofFloat(this, "rotation", this.rotation, this.rotation + spining)
        animator.duration = 3000
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}

            override fun onAnimationEnd(animation: Animator) {
                adjustedAngle = ((270 - currentAngle + 360) % 360).roundToInt().toFloat()

                val colorNames = mapOf(
                    0xFFFF0000.toInt() to context.getString(R.string.sector_red),
                    0xFFFFA500.toInt() to context.getString(R.string.sector_orange),
                    0xFFFFFF00.toInt() to context.getString(R.string.sector_yellow),
                    0xFF008000.toInt() to context.getString(R.string.sector_green),
                    0xFF00FFFF.toInt() to context.getString(R.string.sector_cyan),
                    0xFF0000FF.toInt() to context.getString(R.string.sector_blue),
                    0xFF800080.toInt() to context.getString(R.string.sector_purple)
                )

                for (i in sectorAngles.indices) {
                    val (startAngle, endAngle) = sectorAngles[i]
                    if (adjustedAngle in startAngle..endAngle) {
                        colorAtTwelveName = colorNames[sectorColors[i]] ?: context.getString(R.string.unknown_color)
                        break
                    }
                }
                onAnimationEnd?.invoke()
            }
            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
        animator.start()
    }
}
