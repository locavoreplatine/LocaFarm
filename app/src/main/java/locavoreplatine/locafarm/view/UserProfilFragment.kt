package locavoreplatine.locafarm.view

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_user_profile.*
import locavoreplatine.locafarm.R
import locavoreplatine.locafarm.viewModel.UserProfileViewModel

class UserProfilFragment : Fragment(), LifecycleOwner{

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view : View = inflater.inflate(R.layout.fragment_user_profile,container)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val userProfilViewModel = UserProfileViewModel(activity!!.application)
        userProfilViewModel.setUser(arguments?.getInt("id")!!);
        val user = userProfilViewModel.getUser();
        user.observe(this, Observer {
            Log.e(this.tag,"observer")
            tv1.text=it?.firstName
            tv2.text=it?.lastName
            tv3.text=it?.email
            tv4.text=it?.password
            tv5.text=it?.userId.toString()
        })
    }

}