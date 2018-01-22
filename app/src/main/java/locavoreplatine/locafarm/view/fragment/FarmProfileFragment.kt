package locavoreplatine.locafarm.view.fragment

import android.arch.lifecycle.LifecycleOwner
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_farm_profile.*
import locavoreplatine.locafarm.R
import locavoreplatine.locafarm.viewModel.FarmProfileViewModel

class FarmProfileFragment : Fragment(), LifecycleOwner{

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_farm_profile,container,false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val farmProfileViewModel = FarmProfileViewModel(activity!!.application)
        farmProfileViewModel.setFarm(arguments?.getInt("id")!!)
        val user = farmProfileViewModel.getFarm();
            tv1.text=user.name
            tv2.text=user.description
            tv3.text=user.isBio.toString()
            tv4.text=user.Latitude.toString()
            tv5.text=user.Longitude.toString()
    }
}