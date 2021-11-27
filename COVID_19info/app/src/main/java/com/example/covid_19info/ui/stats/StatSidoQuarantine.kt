package com.example.covid_19info.ui.stats

import android.os.Bundle
import android.text.Html
import android.text.Html.FROM_HTML_MODE_LEGACY
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.example.covid_19info.R
import com.example.covid_19info.data.QuarantinesAPI
import com.example.covid_19info.data.VaccinatedAPI
import com.example.covid_19info.data.model.ItemQS
import com.example.covid_19info.data.model.QuarantinStatSido
import com.example.covid_19info.data.model.VaccinatedInfo
import com.example.covid_19info.databinding.FragmentStatSidoQuarantineBinding
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
 * Use the [StatSidoQuarantine.newInstance] factory method to
 * create an instance of this fragment.
 */
class StatSidoQuarantine : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentStatSidoQuarantineBinding

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
        binding = FragmentStatSidoQuarantineBinding.inflate(layoutInflater)

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
        start.add(Calendar.DATE, -1)
        val df = SimpleDateFormat("yyyyMMdd", Locale.getDefault())

        quarantine.getQuarantineStatSido(page = 1,
            perPage = 19,
            startCreateDt = df.format(start.time),
            endCreateDt = df.format(today.time)).enqueue(object: Callback<QuarantinStatSido> {
            override fun onResponse(
                call: Call<QuarantinStatSido>,
                response: Response<QuarantinStatSido>
            ){
                Log.d("qua", response.toString())
                //업데이트
                response.body()?.let { updateInfo(it) }
            }
            override fun onFailure(call: Call<QuarantinStatSido>, t: Throwable) {
                //TODO("Not yet implemented")
                Log.d("stat", call.request().toString())
                Log.d("stat", t.toString())
            }
        })
    }
    private fun updateInfo(quarantinStatSido: QuarantinStatSido) {

        //초기값 설정
        var data = quarantinStatSido.body.items.item
        Log.d("stat", data.toString())
        setdata(0, data)
        
        //리스너 설정
        var spinner = binding.quarantineSpinner
        spinner.onItemSelectedListener = object:
            AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                setdata(p2, data)
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
    }

    private fun setdata(p2: Int, data: List<ItemQS>){
        var lists = listOf<String>("합계", "서울", "부산", "대구", "광주", "인천",
            "대전", "울산", "세종", "경기", "강원", "충북", "충남", "전남", "전북",
            "경북", "경남", "제주", "검역")

        Log.d("stat", data.toString())
        for(index in data.indices){
            if(lists[p2]==data[index].gubun){

                var quarantine_text =  getString(R.string.quarantine_sido, "누적 확진자", data[index].defCnt)
                var styletex = Html.fromHtml(quarantine_text,FROM_HTML_MODE_LEGACY)
                binding.accumulateQuarantine.text = styletex

                quarantine_text = getString(R.string.quarantine_sido, "누적 격리해제", data[index].isolClearCnt)
                styletex = Html.fromHtml(quarantine_text,FROM_HTML_MODE_LEGACY)
                binding.accumulateClear.text = styletex

                quarantine_text =  getString(R.string.quarantine_sido, "누적 사망자", data[index].deathCnt)
                styletex = Html.fromHtml(quarantine_text,FROM_HTML_MODE_LEGACY)
                binding.accumulateDeath.text = styletex

                quarantine_text = getString(R.string.quarantine_sido_cur, "금일 확진자", data[index].incDec)
                styletex = Html.fromHtml(quarantine_text,FROM_HTML_MODE_LEGACY)
                binding.todayQuarantine.text = styletex

                quarantine_text = getString(R.string.quarantine_sido_cur, "현재 격리자", data[index].isolIngCnt)
                styletex = Html.fromHtml(quarantine_text,FROM_HTML_MODE_LEGACY)
                binding.todayClear.text  = styletex

                quarantine_text = getString(R.string.quarantine_sido_cur, "금일 사망자", data[index].deathCnt)
                styletex = Html.fromHtml(quarantine_text,FROM_HTML_MODE_LEGACY)
                binding.todayDeath.text  = styletex
                break
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment StatSidoQuarantine.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            StatSidoQuarantine().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}