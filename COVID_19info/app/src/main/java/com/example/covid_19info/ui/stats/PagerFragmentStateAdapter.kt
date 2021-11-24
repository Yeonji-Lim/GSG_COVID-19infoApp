package com.example.covid_19info.ui.stats

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

//class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
//    var fragments : ArrayList<Fragment> = ArrayList()
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PagerViewHolder((parent))
//    override fun createFragment(position: Int): Fragment {
//        return fragments[position]
//    }
//
//    override fun getItemCount(): Int = fragments.size
//
//    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
//        //holder.chart.setImageResource(item[position])
//    }
//
//    inner class PagerViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder
//        (LayoutInflater.from(parent.context).inflate(R.layout.chart_fragment, parent, false)){
//
////        val idol = itemView.imageView_idol!!
//            val chart = itemView.findViewById<LineChart>(R.id.lineChart)
//        }
//}

class PagerFragmentStateAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity){
    var fragments : ArrayList<Fragment> = ArrayList()

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    fun addFragment(fragment: Fragment){
        fragments.add(fragment)
        notifyItemInserted(fragments.size - 1)
    }

    fun removeFragment(){
        fragments.removeLast()
        notifyItemRemoved(fragments.size)
    }
}