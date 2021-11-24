package com.example.covid_19info.ui.stats

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        //바인딩 연결

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.statistics_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = StatisticsFragmentBinding.inflate(layoutInflater)


        binding.infectedBtn.setOnClickListener {
            if(!isvaccinated){
                isvaccinated = true
                //이름 변경
                binding.statMainTitle.text = R.string.vaccinated_title_text.toString()
                binding.statPrimaryTitle.text = R.string.vaccinated_box1_text.toString()
                binding.statSecondaryTitle.text = R.string.vaccinated_boc2_text.toString()
                binding.statThirdTitle.text = R.string.vaccinated_box3_text.toString()
                Log.d("stat", "2")
            }
        }
        binding.vaccinatedBtn.setOnClickListener {
            if(isvaccinated){
                isvaccinated = false
                //이름 변경
                binding.statMainTitle.text = R.string.quarantine_title_text.toString()
                binding.statPrimaryTitle.text = R.string.quarantine_box1_text.toString()
                binding.statSecondaryTitle.text = R.string.quarantine_box2_text.toString()
                binding.statThirdTitle.text = R.string.quarantine_box3_text.toString()
                Log.d("stat", "1")
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