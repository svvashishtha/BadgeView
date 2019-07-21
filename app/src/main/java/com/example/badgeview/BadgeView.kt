package com.example.badgeview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.support.v4.content.res.ResourcesCompat
import android.util.AttributeSet
import android.util.Log
import android.view.View
import kotlin.math.abs
import kotlin.math.min


class BadgeView : View {

    private var circle1: Paint? = null
    private var circle2: Paint? = null
    private var textPaint: Paint? = null
    private var trapezium: Paint? = null
    private var x1: Float = 0.toFloat()
    private var y1: Float = 0.toFloat()
    private var textHeightHalf: Float = 0.toFloat()
    private var textWidthHalf: Float = 0.toFloat()
    private var radius1: Float = 0.toFloat()
    private var radius2: Float = 0.toFloat()
    private var trapezium1Path: Path? = null
    private var trapezium2Path: Path? = null
    private var viewWidth: Int = 0
    private var mRank: String? = ""
    private var viewHeight: Int = 0

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        circle1 = Paint()
        circle1?.let {
            it.style = Paint.Style.FILL
            it.isAntiAlias = true
            it.color = resources.getColor(R.color.sushi_yellow_500)
        }
        circle2 = Paint()

        circle2?.let {
            it.style = Paint.Style.STROKE

            it.isAntiAlias = true
            it.color = resources.getColor(R.color.sushi_yellow_400)
        }
        textPaint = Paint()
        textPaint?.let {
            it.style = Paint.Style.FILL
            it.isAntiAlias = true
            it.color = resources.getColor(R.color.sushi_yellow_800)
            it.textAlign = Paint.Align.CENTER
        }
        val tf = ResourcesCompat.getFont(context, R.font.okra_semibold)
        textPaint?.typeface = tf
        if (attrs != null) {
            val a = context.obtainStyledAttributes(
                attrs,
                R.styleable.BadgeView, 0, 0
            )
            mRank = a.getString(R.styleable.BadgeView_rank)
            if (mRank == null) {
                mRank = ""
            }
            a.recycle()
        }

        trapezium = Paint()
        trapezium?.let {
            it.style = Paint.Style.FILL
            it.isAntiAlias = true
            it.color = resources.getColor(R.color.sushi_purple_200)
        }
        trapezium1Path = Path()


        trapezium2Path = Path()


    }


    fun setRank(url: String) {
        mRank = url
        if (mRank == null) {
            mRank = ""
        }
        invalidate()


    }


    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
        init(attrs)
    }

    override fun onDraw(canvas: Canvas) {

        super.onDraw(canvas)
        drawTrapezium(canvas)
        drawCircles(canvas)
        drawtext(canvas)

    }

    private fun drawtext(canvas: Canvas) {
        setTextSize(viewWidth)
        textPaint?.let { textPaint ->
            canvas.drawText(mRank!!, x1, y1 + textHeightHalf, textPaint)
        }
    }

    private fun drawCircles(canvas: Canvas) {
        circle1?.let { circle1 ->
            canvas.drawCircle(x1, y1, radius1, circle1)
        }
        circle2.let { circle2 ->
            canvas.drawCircle(x1, y1, radius2, circle2)
        }
    }

    private fun drawTrapezium(canvas: Canvas) {
        trapezium?.let { trapezium ->
            trapezium1Path?.let { trapezium1Path ->
                canvas.drawPath(trapezium1Path, trapezium)
            }
            trapezium2Path?.let { trapezium2Path ->
                canvas.drawPath(trapezium2Path, trapezium)
            }
        }
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        when (MeasureSpec.getMode(widthMeasureSpec)) {
            MeasureSpec.EXACTLY -> {
                viewWidth = measuredWidth
                viewHeight = (viewWidth * 1.3).toInt()
            }
            MeasureSpec.AT_MOST -> {
                viewHeight = min(measuredHeight, measuredWidth)
                viewWidth = (viewHeight / 1.3).toInt()
            }
            MeasureSpec.UNSPECIFIED -> {
                viewWidth = resources.getDimension(R.dimen.dimen_30dp).toInt()
                viewHeight = (viewWidth * 1.3).toInt()
            }
        }
        x1 = viewWidth.toFloat()

        x1 /= 2
        y1 = viewHeight - x1
        radius1 = (viewWidth * 0.8 / 2).toFloat()
        radius2 = (radius1 * 0.8).toFloat()
        defineTrapeziumPath(x1, viewWidth, viewHeight)
        defineCircleStrokeWidth(viewWidth)
        setMeasuredDimension(viewWidth, viewHeight)
    }

    private fun defineCircleStrokeWidth(viewWidth: Int) {
        circle2?.strokeWidth = (viewWidth * 0.05).toFloat()
    }

    private fun setTextSize(viewWidth: Int) {
        textPaint?.textSize = viewWidth.toFloat() / 2
        val textRect = Rect()
        textPaint?.getTextBounds(mRank, 0, mRank!!.length, textRect)
        textHeightHalf = (textRect.bottom - textRect.top shr 1).toFloat()
        textWidthHalf = (abs(textRect.right - textRect.left) shr 1).toFloat()
    }

    private fun defineTrapeziumPath(circleRadius: Float, viewWidth: Int, viewHeight: Int) {

        //left trapezium
        val factor = (0.25 * viewWidth).toInt()
        val trapeziumWidth = viewWidth.toFloat() / 3
        trapezium1Path?.moveTo(circleRadius - factor, viewHeight - circleRadius)
        trapezium1Path?.lineTo(circleRadius + trapeziumWidth - factor, viewHeight - circleRadius)
        trapezium1Path?.lineTo(trapeziumWidth, 0f)
        trapezium1Path?.lineTo(0f, 0f)
        trapezium1Path?.lineTo(circleRadius - factor, viewHeight - circleRadius)


        //right trapezium
        trapezium2Path?.moveTo(circleRadius + factor, viewHeight - circleRadius)
        trapezium2Path?.lineTo(circleRadius + factor - trapeziumWidth, viewHeight - circleRadius)
        trapezium2Path?.lineTo(viewWidth - trapeziumWidth, 0f)
        trapezium2Path?.lineTo(viewWidth.toFloat(), 0f)
        trapezium2Path?.lineTo(circleRadius + factor, viewHeight - circleRadius)

    }


}
