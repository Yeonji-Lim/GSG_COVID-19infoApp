package com.example.covid_19info.ui.stats

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.covid_19info.R
import com.example.covid_19info.data.QuarantinesAPI
import com.example.covid_19info.data.model.QuarantinStat
import com.example.covid_19info.databinding.FragmentStatBarChartBinding
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [StatBarChart.newInstance] factory method to
 * create an instance of this fragment.
 */
class StatBarChart : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentStatBarChartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStatBarChartBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //확진자 데이터 로드
        val quarantine = QuarantinesAPI.create()
        val today = Calendar.getInstance()
        today.time = Date()
        val start = Calendar.getInstance()
        start.time = Date()
        start.add(Calendar.DATE, -7)
        val df = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        Log.d("stat", df.format(today.time))

        quarantine.getQuarantineStat(page = 1,
            startCreateDt = df.format(start.time),
            endCreateDt = df.format(today.time)).enqueue(object: Callback<QuarantinStat> {
            override fun onResponse(
                call: Call<QuarantinStat>,
                response: Response<QuarantinStat>
            ){
                chartSetting()
                Log.d("stat", response.toString())
                //차트 업데이트
                response.body()?.let { updateCahrt(it) }

                response.body()?.body?.items?.item?.get(0)?.decideCnt
            }
            override fun onFailure(call: Call<QuarantinStat>, t: Throwable) {
                //TODO("Not yet implemented")
                Log.d("stat", t.toString())
            }
        })
    }

    //바 차트 업데이트
    private fun updateCahrt(stat: QuarantinStat){

        //엔트리 생성
        var entries: ArrayList<BarEntry> = ArrayList()
        var cnt = stat.body.items.item!!.size
        for(i: Int in (cnt-2) downTo 0 ){
            val num = stat.body.items.item[i].decideCnt-
                    stat.body.items.item[i+1].decideCnt
            entries.add(BarEntry(cnt - i -1.toFloat(), num.toFloat()))
        }

        var barDataSet = BarDataSet(entries, "")
        barDataSet.color = Color.rgb(88,190,201)

        val data = BarData(barDataSet)
        binding.barChart.data = data


//        binding.barChart.axisLeft.setDrawGridLines(false)
//        binding.barChart.axisRight.setDrawGridLines(false)

        binding.barChart.invalidate()

    }
    private fun chartSetting(){
        binding.barChart.run{
            //zoom 잠금
            setScaleEnabled(false)

            //마커 뷰 설정
            var markerview = StatBarMarkerView(context, R.layout.stat_marker_view)
            marker = markerview

            //격자구조 삭제
            setDrawGridBackground(false)

            //x축 설정
            xAxis.run{
                position = XAxis.XAxisPosition.BOTTOM

                setDrawAxisLine(true)
                setDrawGridLines(false)

                valueFormatter = MyXAxisFormatter()
            }
            //y축
            axisLeft.run{
                setDrawGridLines(false)
            }

            axisRight.run{
                setDrawGridLines(false)
            }



            //우하단 description label삭제
            description.isEnabled = false
            //범례 삭제
            legend.isEnabled = false
            
            setDrawValueAboveBar(true)


        }
    }
    inner class MyXAxisFormatter : ValueFormatter(){
        private val days = arrayOf("6일전","5일전","4일전","3일전","2일전","1일전","0일전")
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return days.getOrNull(value.toInt()-1) ?: value.toString()
        }
    }

    companion object {


        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            StatBarChart().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}