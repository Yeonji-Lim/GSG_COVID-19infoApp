package com.example.covid_19info.ui.stats

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.example.covid_19info.R
import com.example.covid_19info.databinding.StatisticsFragmentBinding

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

        //뷰페이저 구현부
        binding.pager.adapter=ViewPagerAdapter(getStaticList())
        binding.pager.orientation = ViewPager2.ORIENTATION_HORIZONTAL


        val infectbtn = binding.infectedBtn
        infectbtn.setOnClickListener {
            if(!isvaccinated){
                isvaccinated = true
                //이름 변경
                binding.statMainTitle.text = getString(R.string.vaccinated_title_text)
                binding.statPrimaryTitle.text = getString(R.string.vaccinated_box1_text)
                binding.statSecondaryTitle.text = getString(R.string.vaccinated_box2_text)
                binding.statThirdTitle.text = getString(R.string.vaccinated_box3_text)
            }
        }
        binding.vaccinatedBtn.setOnClickListener {
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

    //뷰페이저 내용 들어가는 부분
    private fun getStaticList():ArrayList<Int>{
        return arrayListOf<Int>(R.drawable.ic_menu_camera,R.drawable.ic_menu_gallery )
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