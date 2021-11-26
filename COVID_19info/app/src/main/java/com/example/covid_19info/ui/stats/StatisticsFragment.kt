package com.example.covid_19info.ui.stats

import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.covid_19info.R
import com.example.covid_19info.data.QuarantinesAPI
import com.example.covid_19info.data.VaccinatedAPI
import com.example.covid_19info.data.model.QuarantinStat
import com.example.covid_19info.data.model.VaccinatedNation
import com.example.covid_19info.databinding.StatisticsFragmentBinding
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
 * Use the [StatisticsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StatisticsFragment : Fragment() {


    private lateinit var binding: StatisticsFragmentBinding
    private var param1: String? = null
    private var param2: String? = null
    var isvaccinated = true
    lateinit var pager: ViewPager2
    lateinit var pageadapter: PagerFragmentStateAdapter
    lateinit var pageadapter1: PagerFragmentStateAdapter

    fun Int.dpToPx(displayMetrics: DisplayMetrics): Int = (this * displayMetrics.density).toInt()

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

        //차트 뷰 초기화
        setChartView()

        //백신버튼 리스너
        binding.vaccinatedBtn.setOnClickListener {
            if(!isvaccinated){
                isvaccinated = true
                //이름 변경
                binding.statMainTitle.text = getString(R.string.vaccinated_title_text)
                binding.statPrimaryTitle.text = getString(R.string.vaccinated_box1_text)
                binding.statSecondaryTitle.text = getString(R.string.vaccinated_box2_text)
                binding.statThirdTitle.text = getString(R.string.vaccinated_box3_text)
                binding.vaccinatedBtn.setBackgroundResource(R.drawable.curved_rectangle_gray)
                //binding.vaccinatedBtn.setBackgroundColor(Color.rgb(94,94,94))
                binding.vaccinatedBtn.setTextColor(Color.WHITE)
                binding.infectedBtn.setBackgroundResource(R.drawable.curved_rectangle)
                binding.infectedBtn.setTextColor(Color.BLACK)

                //차트 변경
                pager.adapter = pageadapter1

                //총합 데이터 출력
                updateNationVaccinated()
            }
        }
        //확진자 버튼 리스너
        binding.infectedBtn.setOnClickListener {
            if(isvaccinated){
                isvaccinated = false
                //이름 변경
                binding.statMainTitle.text = getString(R.string.quarantine_title_text)
                binding.statPrimaryTitle.text = getString(R.string.quarantine_box1_text)
                binding.statSecondaryTitle.text = getString(R.string.quarantine_box2_text)
                binding.statThirdTitle.text = getString(R.string.quarantine_box3_text)
                binding.infectedBtn.setBackgroundResource(R.drawable.curved_rectangle_gray)
                binding.infectedBtn.setTextColor(Color.WHITE)
                binding.vaccinatedBtn.setBackgroundResource(R.drawable.curved_rectangle)
                binding.vaccinatedBtn.setTextColor(Color.BLACK)

                //차트 변경
                pager.adapter = pageadapter

                //총합 데이터 출력
                updateVaccinatedData()
            }
        }
    }

    private fun updateNationVaccinated(){
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
                binding.statPrimaryInc.text = getString(R.string.nation_data,
                    response.body()?.body?.items?.item?.get(0)?.firstCnt
                )
                binding.statSecondaryInc.text = getString(R.string.nation_data,
                    response.body()?.body?.items?.item?.get(0)?.secondCnt
                )
                binding.statThirdInc.text = getString(R.string.nation_data,
                    response.body()?.body?.items?.item?.get(0)?.thirdCnt
                )
            }override fun onFailure(call: Call<VaccinatedNation>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
    private fun updateVaccinatedData(){
        val quarantine = QuarantinesAPI.create()
        val today = Calendar.getInstance()
        today.time = Date()
        val start = Calendar.getInstance()
        start.time = Date()
        start.add(Calendar.DATE, -3)
        val df = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        Log.d("stat", df.format(today.time))

        quarantine.getQuarantineStat(page = 1,
            startCreateDt = df.format(start.time),
            endCreateDt = df.format(today.time)).enqueue(object: Callback<QuarantinStat> {
            override fun onResponse(
                call: Call<QuarantinStat>,
                response: Response<QuarantinStat>
            ){
                Log.d("stat", response.toString())
                binding.statPrimaryFigure.text = getString(R.string.nation_data,
                    response.body()?.body?.items?.item?.get(0)?.decideCnt
                )
                binding.statSecondaryFigure.text = getString(R.string.nation_data,
                    response.body()?.body?.items?.item?.get(0)?.clearCnt
                )
                binding.statThirdFigure.text = getString(R.string.nation_data,
                    response.body()?.body?.items?.item?.get(0)?.deathCnt
                )

                //전일대비 증가
                binding.statPrimaryInc.text = getString(R.string.nation_data,
                    response.body()?.body?.items?.item?.get(0)?.decideCnt?.minus(
                        response.body()?.body?.items?.item?.get(1)?.decideCnt!!
                    )
                )
                binding.statSecondaryInc.text = getString(R.string.nation_data,
                    response.body()?.body?.items?.item?.get(0)?.clearCnt?.minus(
                        response.body()?.body?.items?.item?.get(1)?.clearCnt!!
                    )
                )
                binding.statThirdInc.text = getString(R.string.nation_data,
                    response.body()?.body?.items?.item?.get(0)?.deathCnt?.minus(
                        response.body()?.body?.items?.item?.get(1)?.deathCnt!!
                    )
                )
            }
            override fun onFailure(call: Call<QuarantinStat>, t: Throwable) {
                //TODO("Not yet implemented")
                Log.d("stat", t.toString())
            }
        })
    }

    //아래 차트 부분 초기화
    private fun setChartView(){



        //확진자 뷰페이저 구현부
        pageadapter = PagerFragmentStateAdapter(requireActivity())
        pageadapter.addFragment(StatBarChart.newInstance("", ""))
        pageadapter.addFragment(StatLineChart.newInstance("quarantine", ""))
        pageadapter.addFragment(StatSidoQuarantine.newInstance("", ""))

        //백신 뷰페이저
        pageadapter1 = PagerFragmentStateAdapter(requireActivity())
        pageadapter1.addFragment(StatLineChart.newInstance("", ""))
        pageadapter1.addFragment(StatSidoVaccinated.newInstance("", ""))

        pager = binding.pager
        pager.adapter = pageadapter1
        pager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        pager.apply {
            clipToPadding = false   // allow full width shown with padding
            clipChildren = false    // allow left/right item is not clipped
            offscreenPageLimit = 2  // make sure left/right item is rendered
        }
        //그림자 잘림 해결
        (pager.getChildAt(0) as ViewGroup).clipChildren = false

        // 좌/우 노출되는 크기를 크게하려면 offsetPx 증가
        val offsetPx = 35.dpToPx(resources.displayMetrics)
        pager.setPadding(offsetPx, 5.dpToPx(resources.displayMetrics), offsetPx, offsetPx)

        // 페이지간 마진 크게하려면 pageMarginPx 증가
        val pageMarginPx = 15.dpToPx(resources.displayMetrics)
        val marginTransformer = MarginPageTransformer(pageMarginPx)
        pager.setPageTransformer(marginTransformer)
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