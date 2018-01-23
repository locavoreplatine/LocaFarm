package locavoreplatine.locafarm.view.fragment

import android.arch.lifecycle.LifecycleOwner
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.fragment_farm.*
import locavoreplatine.locafarm.R
import locavoreplatine.locafarm.view.activity.MainActivity
import locavoreplatine.locafarm.view.viewAdapter.FarmRecyclerViewAdapter
import locavoreplatine.locafarm.viewModel.FarmProfileViewModel

class FarmProfileFragment : Fragment(), LifecycleOwner{

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_farm,container,false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val farmProfileViewModel = FarmProfileViewModel(activity!!.application)
        farmProfileViewModel.setFarm(arguments?.getInt("id")!!)
        val farm = farmProfileViewModel.getFarm()
        val product = farmProfileViewModel.getProduct()
        val recyclerViewAdapter = FarmRecyclerViewAdapter(product)
        val horizontalLayoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        fragment_farm_recycler_view.layoutManager=horizontalLayoutManager
        fragment_farm_recycler_view.adapter=recyclerViewAdapter
        fragment_farm_image.setImageResource(R.drawable.cover)

    }
}