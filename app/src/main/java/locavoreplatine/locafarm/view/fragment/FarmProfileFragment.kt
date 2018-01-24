package locavoreplatine.locafarm.view.fragment

import android.arch.lifecycle.LifecycleOwner
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_farm.*
import kotlinx.android.synthetic.main.fragment_farm_detail.*
import locavoreplatine.locafarm.R
import locavoreplatine.locafarm.view.viewAdapter.FarmRecyclerViewAdapter
import locavoreplatine.locafarm.viewModel.FarmProfileViewModel

class FarmProfileFragment : Fragment(), LifecycleOwner{

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_farm,container,false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val farmProfileViewModel = FarmProfileViewModel(activity!!.application)
        farmProfileViewModel.setFarm(arguments?.getLong("id")!!)
        val farm = farmProfileViewModel.getFarm()
        val product = farmProfileViewModel.getProduct()
        val recyclerViewAdapter = FarmRecyclerViewAdapter(product)
        val horizontalLayoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        fragment_farm_recycler_view.layoutManager=horizontalLayoutManager
        fragment_farm_recycler_view.adapter=recyclerViewAdapter
        fragment_farm_image.setImageResource(R.drawable.full_logo)
        fragment_farm_ratingbar.numStars=farm.rating
        fragment_farm_tv_farmname.text=farm.name
        fragment_farm_tv_addr.text=farm.addr
        fragment_farm_tv_producername.text=farm.producerName
        fragment_farm_tv_desc.text=farm.description
    }
}