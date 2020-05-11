package com.example.student.covidstats.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.example.student.covidstats.db.CountryDetailEntity


var GRAPH_TYPE_ACTIVE = "ACTIVE"
var GRAPH_TYPE_CONFIRMED = "CONFIRMED"
var GRAPH_TYPE_DEATHS = "DEATHS"

class GraphView(context: Context, attr: AttributeSet): View(context, attr) {

    private var mData: List<CountryDetailEntity>? = null
    private var mGraphType: String? = null

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if(this.mData == null || this.mData!!.isEmpty() || this.mGraphType == null){
            return
        }

        when(mGraphType) {
            GRAPH_TYPE_ACTIVE -> drawGraph(canvas, mData!!.map{it.active}, bluePaint, mData!![0].date, mData!!.last().date)
            GRAPH_TYPE_CONFIRMED -> drawGraph(canvas, mData!!.map{it.confirmed}, greenPaint, mData!![0].date, mData!!.last().date)
            GRAPH_TYPE_DEATHS -> drawGraph(canvas, mData!!.map{it.deaths}, redPaint, mData!![0].date, mData!!.last().date)
        }
    }

    private fun drawGraph(canvas: Canvas?, dataset: List<Int>, paint: Paint, firstDate: String, lastDate: String) {
        canvas?.apply {
            var step = width.toFloat() / dataset.size
            val maxValue = dataset.max()!!
            var maxPlotHeight = height.toFloat() - 80F

            var loopStep = when {
                dataset.size < 60 -> 1
                dataset.size < 90 -> 1
                dataset.size < 120 -> 1
                else -> 2
            }
            var previousValue = maxPlotHeight
            for (index in 0 until dataset.size step loopStep) {
                var scaledValue =
                    scaleBetween(dataset[index], 0F, maxPlotHeight, 0, maxValue)
                drawLine(
                    step * index, previousValue,
                    step * (index + 1),
                    maxPlotHeight - scaledValue,
                    paint
                )
                previousValue = maxPlotHeight - scaledValue
            }
            drawText(firstDate, 0F, height.toFloat(), blackPaint)
            drawText(lastDate, width.toFloat() - 260F, height.toFloat(), blackPaint)


            var stopX: Float
            var stopY: Float
            val height = canvas.height - 350F

            val gridSize = 20
            val gridSpacing = (width/gridSize).toFloat()
            val boardSize = gridSize * gridSpacing

            val xOffset = ((width - boardSize) / 2)
            val yOffset = (height - boardSize) / 2

            for (i in 0 until gridSize) {
                stopX = xOffset + i * gridSpacing
                stopY = yOffset + boardSize
                canvas.drawLine(stopX, yOffset, stopX, stopY, black)
            }

            for (i in 0 until gridSize) {
                stopX = xOffset + boardSize
                stopY = yOffset + i * gridSpacing
                canvas.drawLine(xOffset, stopY, stopX, stopY, black)
            }
        }
    }

    private val bluePaint = Paint(0).apply {
        color = Color.rgb(0, 0, 255);
        strokeWidth = 8F;
    }

    private val redPaint = Paint(0).apply {
        color = Color.rgb(255, 0, 0);
        strokeWidth = 8F;
    }

    private val greenPaint = Paint(0).apply {
        color = Color.rgb(0, 255, 0);
        strokeWidth = 8F;

    }

    private val black = Paint(0).apply {
        color = Color.rgb(0, 0, 0);
        strokeWidth = 1F;

    }

    private val blackPaint = Paint(0).apply {
        color = Color.BLACK
        style = Paint.Style.FILL
        strokeWidth = 10F;
        textSize = 50F
    }

    fun setParameters(data: List<CountryDetailEntity>, graphType: String) {
        this.mData = data
        this.mGraphType = graphType
    }

    private fun scaleBetween(unscaledNum:Int, minAllowed:Float, maxAllowed:Float, min:Int, max:Int): Float {
        return (maxAllowed - minAllowed) * (unscaledNum - min) / (max - min) + minAllowed;
    }
}