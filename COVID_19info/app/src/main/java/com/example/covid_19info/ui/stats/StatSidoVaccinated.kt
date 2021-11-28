package com.example.covid_19info.ui.stats

import android.os.Bundle
import android.text.Html
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.view.get
import androidx.core.view.iterator
import com.example.covid_19info.R
import com.example.covid_19info.data.VaccinatedAPI
import com.example.covid_19info.data.model.QuarantinStat
import com.example.covid_19info.data.model.VaccinatedData
import com.example.covid_19info.data.model.VaccinatedInfo
import com.example.covid_19info.databinding.FragmentStatSidoVaccinatedBinding
import com.google.android.gms.maps.model.Marker
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
 * Use the [StatSido.newInstance] factory method to
 * create an instance of this fragment.
 */
class StatSidoVaccinated : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentStatSidoVaccinatedBinding
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
        binding = FragmentStatSidoVaccinatedBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //백신 데이터 로드
        val vaccinated = VaccinatedAPI.createsido()
        val today = Calendar.getInstance()
        today.time = Date()
        today.add(Calendar.DATE, -1)
        val df = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        vaccinated.getVaccinatedSidoData(page = 1,
        perPage = 50,
        date = df.format(today.time)).enqueue(object: Callback<VaccinatedInfo>{
            override fun onResponse(
                call: Call<VaccinatedInfo>,
                response: Response<VaccinatedInfo>
            ){
                Log.d("vac", response.toString())
                //업데이트
                response.body()?.let { updateInfo(it) }
            }
            override fun onFailure(call: Call<VaccinatedInfo>, t: Throwable) {
                //TODO("Not yet implemented")
                Log.d("stat", t.toString())
            }
        })
    }

    private fun updateInfo(vaccinatedInfo: VaccinatedInfo){
        

        var vacData = vaccinatedInfo.data
        //총 시민수
        var total = 51667688f

        //초기값 설정
        setdata(0, vacData)
        
        //리스너 설정
        var spinner = binding.vaccinatedSpinner
        spinner.onItemSelectedListener = object:
        AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                setdata(p2, vacData)
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
        for(spinnerItem in spinner){
            spinnerItem.isSelected
        }
    }
    
    //데이터 설정
    private fun setdata(p2: Int, vacData: List<VaccinatedData>){
        var list = listOf(52980961f, 9911088f, 3438710f, 2446144f, 1471385f,
            3010476f, 1480777f, 1153901f, 360907f, 13807158f,
            1560172f, 1637897f, 2185575f, 1884455f, 1835392f,
            2691891f, 3407455f, 697578f)

        var vaccinated_text =  getString(R.string.accumulated_vaccinated, "1차 접종",
            vacData[p2].accumulatedFirstCnt.div(list[p2]).times(100), vacData[p2].firstCnt)
        var styletex = Html.fromHtml(vaccinated_text, Html.FROM_HTML_MODE_LEGACY)
        binding.firstVaccinated.text = styletex

        vaccinated_text = getString(R.string.accumulated_vaccinated, "2차 접종",
            vacData[p2].accumulatedSecondCnt.div(list[p2]).times(100), vacData[p2].secondCnt)
        styletex = Html.fromHtml(vaccinated_text, Html.FROM_HTML_MODE_LEGACY)
        binding.secondVaccinated.text = styletex

        vaccinated_text = getString(R.string.accumulated_vaccinated, "추가 접종",
        vacData[p2].accumulatedThirdCnt.div(list[p2]).times(100), vacData[p2].thirdCnt)
        styletex = Html.fromHtml(vaccinated_text, Html.FROM_HTML_MODE_LEGACY)
        binding.thirdVaccinated.text = styletex

        vaccinated_text = getString(R.string.accumulated_vaccinated1,
            vacData[p2].accumulatedSecondCnt, vacData[p2].secondCnt)
        styletex = Html.fromHtml(vaccinated_text, Html.FROM_HTML_MODE_LEGACY)
        binding.accumulateVaccinated.text = styletex
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment StatSido.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            StatSidoVaccinated().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}