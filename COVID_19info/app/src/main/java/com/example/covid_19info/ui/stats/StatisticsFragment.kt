package com.example.covid_19info.ui.stats

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.covid_19info.R
import com.example.covid_19info.data.VaccinatedAPI
import com.example.covid_19info.data.model.VaccinatedNation
import com.example.covid_19info.databinding.StatisticsFragmentBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [StatisticsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StatisticsFragment : Fragment() {


    private lateinit var binding: StatisticsFragmentBinding
    private var param1: String? = null
    private var param2: String? = null
    var isvaccinated = true

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
        //바인딩 연결
        binding = StatisticsFragmentBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //초기 데이터 로드
        updateNationVaccinated()


        binding.vaccinatedBtn.setOnClickListener {
            if(!isvaccinated){
                isvaccinated = true
                //이름 변경
                binding.statMainTitle.text = getString(R.string.vaccinated_title_text)
                binding.statPrimaryTitle.text = getString(R.string.vaccinated_box1_text)
                binding.statSecondaryTitle.text = getString(R.string.vaccinated_box2_text)
                binding.statThirdTitle.text = getString(R.string.vaccinated_box3_text)

                updateNationVaccinated()
            }
        }
        binding.infectedBtn.setOnClickListener {
            if(isvaccinated){
                isvaccinated = false
                //이름 변경
                binding.statMainTitle.text = getString(R.string.quarantine_title_text)
                binding.statPrimaryTitle.text = getString(R.string.quarantine_box1_text)
                binding.statSecondaryTitle.text = getString(R.string.quarantine_box2_text)
                binding.statThirdTitle.text = getString(R.string.quarantine_box3_text)
            }
        }
    }

    fun updateNationVaccinated(){
        //전국 백신 데이터 업데이트
        val vacapi = VaccinatedAPI.createNation()

        vacapi.getNationData().enqueue(object: Callback<VaccinatedNation> {
            override fun onResponse(
                call: Call<VaccinatedNation>,
                response: Response<VaccinatedNation>
            ){
                //총 시민수
                var total = 51667688f
                //전체 접종 현황
                binding.statPrimaryFigure.text = getString(R.string.nation_data_f,
                    response.body()?.body?.items?.item?.get(2)?.firstCnt?.div(total)?.times(100)
                )
                binding.statSecondaryFigure.text = getString(R.string.nation_data_f,
                    response.body()?.body?.items?.item?.get(2)?.secondCnt?.div(total)?.times(100)
                )
                binding.statThirdFigure.text = getString(R.string.nation_data_f,
                    response.body()?.body?.items?.item?.get(2)?.thirdCnt?.div(total)?.times(100)
                )

                //전일대비 증가
                binding.statPrimaryInc.text = getString(R.string.nation_data, response.body()?.body?.items?.item?.get(0)?.firstCnt)
                binding.statSecondaryInc.text = getString(R.string.nation_data, response.body()?.body?.items?.item?.get(0)?.secondCnt)
                binding.statThirdInc.text = getString(R.string.nation_data, response.body()?.body?.items?.item?.get(0)?.thirdCnt)
            }override fun onFailure(call: Call<VaccinatedNation>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    fun getVaccinatedData(){

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment StatisticsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            StatisticsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}