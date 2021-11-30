package com.example.covid_19info.ui.stats

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.Parcelable
import android.provider.Settings.Global.getString
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.TextView
import com.example.covid_19info.R
import com.github.mikephil.charting.components.IMarker
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.tickaroo.tikxml.annotation.TextContent
import org.w3c.dom.Text
import java.time.LocalDate


class StatLineMarkerView : MarkerView, IMarker {

    lateinit private var tvContent: TextView

    constructor(context: Context?, layoutResource: Int) : super(context, layoutResource){
        tvContent = findViewById(R.id.marker_text)
    }

    // draw override를 사용해 marker의 위치 조정 (bar의 상단 중앙)
//    override fun draw(canvas: Canvas?) {
//        canvas!!.translate(-(width / 2).toFloat(), -height.toFloat() )
//
//        super.draw(canvas)
//    }

    override fun getOffset(): MPPointF {
        return MPPointF(-(width / 2).toFloat(), -height.toFloat())
    }

    // entry를 content의 텍스트에 지정
    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        Log.d("XYMarkerView", "refreshContent: " + e.toString());

        tvContent.text = context.getString(R.string.stat_bar_marker_text,
            LocalDate.now().minusDays(49-e?.x?.toInt()?.toLong()!!).toString(),
            e.y.toInt()
        )
        super.refreshContent(e, highlight)
    }

}