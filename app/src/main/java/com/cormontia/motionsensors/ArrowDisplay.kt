package com.cormontia.motionsensors

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View

/**
 * TODO: document your custom view class.
 */
class ArrowDisplay : View {

    private val measuredMatrix = Matrix()
    private val paint = Paint()

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        paint.color = Color.RED
        paint.style = Paint.Style.FILL_AND_STROKE
    }

    fun receiveMatrix(floats: FloatArray) {
        measuredMatrix.setValues(floats)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        val paddingLeft = paddingLeft
        val paddingTop = paddingTop
        val paddingRight = paddingRight
        val paddingBottom = paddingBottom

        val contentWidth = width - paddingLeft - paddingRight
        val contentHeight = height - paddingTop - paddingBottom

        //TODO!+ Add this line, after we have a nice bitmap.
        //canvas.drawBitmap(bmp, measuredMatrix, paint)

        //TODO!+ Find out why this is only shown in Portrait mode, when the other components ARE shown in Landscape mode.
        //TODO!- For testing.
        canvas.drawRect(Rect(10,10,210,210), paint)

    }
}