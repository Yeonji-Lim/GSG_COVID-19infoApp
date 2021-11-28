package com.example.covid_19info.ui.stats

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.covid_19info.R
import com.example.covid_19info.data.QuarantinesAPI
import com.example.covid_19info.data.VaccinatedAPI
import com.example.covid_19info.data.model.QuarantinStat
import com.example.covid_19info.data.model.QuarantinStatSido
import com.example.covid_19info.data.model.VaccinatedInfo
import com.example.covid_19info.databinding.FragmentStatLineChartBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.ChartData
import com.github.mikephil.charting.data.Entry
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlin.collections.ArrayList


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [StatLineChart.newInstance] factory method to
 * create an instance of this fragment.
 */
class StatLineChart : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentStatLineChartBinding
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
        binding = FragmentStatLineChartBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //라인차트 세팅
        setLinechartSettint()

        if(param1=="quarantine"){
            //확진자 데이터 로드
            binding.lineText.text = "누적 감염 현황"
            val quarantine = QuarantinesAPI.create()
            val today = Calendar.getInstance()
            today.time = Date()
            val start = Calendar.getInstance()
            start.time = Date()
            start.add(Calendar.DATE, -30)
            val df = SimpleDateFormat("yyyyMMdd", Locale.getDefault())

            quarantine.getQuarantineStat(page = 1,
                perPage = 19,
                startCreateDt = df.format(start.time),
                endCreateDt = df.format(today.time)).enqueue(object: Callback<QuarantinStat> {
                override fun onResponse(
                    call: Call<QuarantinStat>,
                    response: Response<QuarantinStat>
                ){
                    Log.d("qua", response.toString())
                    //업데이트
                    response.body()?.let { updateInfo(it) }
                }
                override fun onFailure(call: Call<QuarantinStat>, t: Throwable) {
                    //TODO("Not yet implemented")
                    Log.d("stat", call.request().toString())
                    Log.d("stat", t.toString())
                }
            })
        }
        else{
            //백신 데이터 로드
            val day = 50
            val vaccinated = VaccinatedAPI.createsido()
            val today = Calendar.getInstance()
            today.time = Date()
            today.add(Calendar.DATE, -day)
            val df = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            vaccinated.getVaccinatedSidoData(page = 1,
                perPage = day*18,
                date = df.format(today.time)).enqueue(object: Callback<VaccinatedInfo> {
                override fun onResponse(
                    call: Call<VaccinatedInfo>,
                    response: Response<VaccinatedInfo>
                ){
                    Log.d("vac", response.toString())
                    Log.d("vac", response.body()?.perPage.toString())
                    //업데이트
                    response.body()?.let { updateInfo(it) }
                }
                override fun onFailure(call: Call<VaccinatedInfo>, t: Throwable) {
                    Log.d("stat", t.toString())
                }
            })
        }
    }

    private fun setLinechartSettint(){
        binding.lineChart.run {
            //zoom 잠금
            setScaleEnabled(false)

            //마커 뷰 설정
            var markerview = StatMarkerView(context, R.layout.stat_marker_view)
            marker = markerview

            //격자구조 삭제
            setDrawGridBackground(false)

            //x축 설정
            xAxis.run{
                position = XAxis.XAxisPosition.BOTTOM

                setDrawAxisLine(true)
                setDrawGridLines(false)

//                valueFormatter = MyXAxisFormatter()
            }

            //우하단 description label삭제
            description.isEnabled = false
            //범례 삭제
            legend.isEnabled = false
            //원 삭제

//            setDrawValueAboveBar(true)
        }
    }

    private fun updateInfo(vaccinatedInfo: VaccinatedInfo){
        var vaccinated = vaccinatedInfo.data
        val entries1 = ArrayList<Entry>()
        val entries2 = ArrayList<Entry>()
        for(i: Int in 0..vaccinated.size-1 step(18)){
            entries1.add(Entry(i.toFloat(), vaccinated[i].accumulatedFirstCnt.toFloat()))
            entries2.add(Entry(i.toFloat(), vaccinated[i].accumulatedSecondCnt.toFloat()))
        }

        var data = LineData(LineDataSet(entries1,"1차"))
        data.addDataSet(LineDataSet(entries2, "2차"))

        //원 삭제
        for(d in data.dataSets){
            (d as LineDataSet).setDrawCircles(false)
            d.setDrawValues(false)
        }

        binding.lineChart.data = data
        binding.lineChart.invalidate()
    }

    private fun updateInfo(quarantinStat: QuarantinStat){
        var quarantines = quarantinStat.body.items.item
        val entries1 = ArrayList<Entry>()
        val entries2 = ArrayList<Entry>()
        val cnt = quarantines!!.size-1
        //println("크기 테스트"+quarantines?.size?.minus(1)!!)
        for(i: Int in 0..cnt step(1)){
            entries1.add(Entry(i.toFloat(), quarantines[cnt-i].decideCnt.toFloat()))
            entries2.add(Entry(i.toFloat(), quarantines[cnt-i].clearCnt.toFloat()))
        }

        var data = LineData(LineDataSet(entries1,"확진"))
        data.addDataSet(LineDataSet(entries2, "격리해제"))

        //원 삭제
        for(d in data.dataSets){
            (d as LineDataSet).setDrawCircles(false)
            d.setDrawValues(false)
        }

        binding.lineChart.data = data

        binding.lineChart.invalidate()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment StatLineChart.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            StatLineChart().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}