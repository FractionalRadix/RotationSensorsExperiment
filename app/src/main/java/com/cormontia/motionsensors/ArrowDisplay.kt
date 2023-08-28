package com.cormontia.motionsensors

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View

/**
 * TODO: document your custom view class.
 */
class ArrowDisplay : View {

    private val measuredMatrix = Matrix()
    private val solidRedPaint = Paint()
    private val redOutlinePaint = Paint()
    private lateinit var image: Bitmap

    constructor(context: Context) : super(context) {
        init(context, null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(context, attrs, defStyle)
    }

    private fun init(ctx: Context, attrs: AttributeSet?, defStyle: Int) {
        solidRedPaint.color = Color.RED
        solidRedPaint.style = Paint.Style.FILL_AND_STROKE

        redOutlinePaint.color = Color.RED
        redOutlinePaint.style = Paint.Style.STROKE

        image = BitmapFactory.decodeResource(ctx.resources, R.drawable.arrow)
    }

    fun receiveMatrix(floats: FloatArray) {
        measuredMatrix.setValues(floats)
        invalidate()
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

        val testRect = Rect(350,350,410,410)
        canvas.drawRect(testRect, redOutlinePaint)

        canvas.drawBitmap(image, measuredMatrix, solidRedPaint)
        //canvas.drawBitmap(image, 40f, 40f, paint)

        //TODO!+ Find out why this is only shown in Portrait mode, when the other components ARE shown in Landscape mode.
        //TODO!- For testing.

    }
}