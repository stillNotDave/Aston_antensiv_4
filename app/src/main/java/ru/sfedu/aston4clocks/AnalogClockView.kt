package ru.sfedu.aston4clocks

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

private var mHeight = 0
private var mWidth = 0
private var mRadius = 0
private var mAngle = 0.0
private var mCentreX = 0
private var mCentreY = 0
private var mPadding = 0
private var mIsInit = false
private var mPaint: Paint? = null
private var mPath: Path? = null
//private val mNumbers: IntArray
private var mMinimum = 0
private var mHour = 0f
private var mMinute = 0f
private var mSecond = 0f
private var mHourHandSize = 0
private var mHandSize = 0

private var mRect: Rect? = null

class AnalogClockView
@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : View(context, attrs) {

    private fun init() {
        mHeight = height
        mWidth = width
        mPadding = 50
        mCentreX = mWidth / 2
        mCentreY = mHeight / 2
        mMinimum = Math.min(mHeight, mWidth)
        mRadius = mMinimum / 2 - mPadding
        mAngle = (Math.PI / 30 - Math.PI / 2).toFloat().toDouble()
        mPaint = Paint()
        mPath = Path()
        mRect = Rect()
        mHourHandSize = mRadius - mRadius / 2
        mHandSize = mRadius - mRadius / 4
        mIsInit = true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (!mIsInit) {
            init()
        }
        drawCircle(canvas)
        drawHands(canvas)
        drawNumerals(canvas)
        postInvalidateDelayed(500)
    }

    private fun drawCircle(canvas: Canvas?) {
        mPaint?.reset()
        setPaintAttributes(Color.BLACK, Paint.Style.STROKE, 10)
        canvas?.drawCircle(mCentreX.toFloat(), mCentreY.toFloat(), mRadius.toFloat(), mPaint!!)
    }

    private fun setPaintAttributes(colour: Int, stroke: Paint.Style, strokeWidth: Int) {
        mPaint?.reset()
        mPaint?.color = colour
        mPaint?.style = stroke
        mPaint?.strokeWidth = strokeWidth.toFloat()
        mPaint?.isAntiAlias = true
    }

    private fun drawHands(canvas: Canvas?) {
        val calendar = Calendar.getInstance()
        mHour = calendar[Calendar.HOUR_OF_DAY].toFloat()
        //convert to 12hour format from 24 hour format
        mHour = if (mHour > 12) mHour - 12 else mHour
        mMinute = calendar[Calendar.MINUTE].toFloat()
        mSecond = calendar[Calendar.SECOND].toFloat()
        drawHourHand(canvas, (mHour + mMinute / 60.0) * 5f)
        drawMinuteHand(canvas, mMinute)
        drawSecondsHand(canvas, mSecond)
    }

    private fun drawHourHand(canvas: Canvas?, location: Double) {
        mPaint!!.reset()
        setPaintAttributes(Color.BLACK, Paint.Style.STROKE, 10)
        mAngle = Math.PI * location / 30 - Math.PI / 2
        canvas?.drawLine(
            mCentreX.toFloat(), mCentreY.toFloat(),
            (mCentreX + cos(mAngle) * mHourHandSize).toFloat(),
            (mCentreY + sin(mAngle) * mHourHandSize).toFloat(), mPaint!!
        )
    }

    private fun drawMinuteHand(canvas: Canvas?, location: Float) {
        mPaint!!.reset()
        setPaintAttributes(Color.BLACK, Paint.Style.STROKE, 8)
        mAngle = Math.PI * location / 30 - Math.PI / 2
        canvas?.drawLine(
            mCentreX.toFloat(), mCentreY.toFloat(),
            (mCentreX + Math.cos(mAngle) * mHandSize).toFloat(),
            (mCentreY + Math.sin(mAngle) * mHourHandSize).toFloat(),
            mPaint!!
        )
    }

    private fun drawSecondsHand(canvas: Canvas?, location: Float) {
        mPaint!!.reset()
        setPaintAttributes(Color.RED, Paint.Style.STROKE, 8)
        mAngle = Math.PI * location / 30 - Math.PI / 2
        canvas?.drawLine(
            mCentreX.toFloat(), mCentreY.toFloat(),
            (mCentreX + Math.cos(mAngle) * mHandSize).toFloat(),
            (mCentreY + Math.sin(mAngle) * mHourHandSize).toFloat(), mPaint!!
        )
    }

    private fun drawNumerals(canvas: Canvas?) {
        mPaint!!.textSize = 80f
        val mNumbers = intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
        for (number in mNumbers) {
            val num = number.toString()
            mPaint!!.getTextBounds(num, 0, num.length, mRect)
            val angle = Math.PI / 6 * (number - 3)
            val x = (mCentreX + cos(angle) * mRadius - mRect!!.width() / 2).toInt()
            val y = (mCentreY + sin(angle) * mRadius + mRect!!.height() / 2).toInt()
            canvas?.drawText(num, x.toFloat(), y.toFloat(), mPaint!!)
        }
    }

}