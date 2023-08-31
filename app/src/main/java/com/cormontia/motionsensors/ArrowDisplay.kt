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
import android.util.Log
import android.view.View

/**
 * TODO: document your custom view class.
 */
class ArrowDisplay : View {

    private val measuredMatrix1 = Matrix()
    private val measuredMatrix2 = Matrix()
    private val measuredMatrix3 = Matrix()
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

    fun receiveMatrices(floats1: FloatArray, floats2: FloatArray, floats3: FloatArray) {
        measuredMatrix1.setValues(floats1)
        measuredMatrix2.setValues(floats2)
        measuredMatrix3.setValues(floats3)
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


        val invert1 = measuredMatrix1.invert(measuredMatrix1)
        //Log.i("ArrowDisplay", "Invert #1: $invert1")
        //measuredMatrix1.postTranslate(100f, 100f)
        canvas.drawBitmap(image, measuredMatrix1, solidRedPaint)

        val invert2 = measuredMatrix2.invert(measuredMatrix2)
        //Log.i("ArrowDisplay", "Invert #2: $invert2")
        //measuredMatrix2.postTranslate(200f, 100f)
        canvas.drawBitmap(image, measuredMatrix2, solidRedPaint)

        val invert3 = measuredMatrix3.invert(measuredMatrix3)
        //Log.i("ArrowDisplay", "Invert #3: $invert3")
        //measuredMatrix3.postTranslate(300f, 100f)
        canvas.drawBitmap(image, measuredMatrix3, solidRedPaint)
    }
}
