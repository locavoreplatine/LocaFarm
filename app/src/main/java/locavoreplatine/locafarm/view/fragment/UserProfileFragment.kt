package locavoreplatine.locafarm.view.fragment

import android.arch.lifecycle.LifecycleOwner
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_user.*
import locavoreplatine.locafarm.R
import locavoreplatine.locafarm.viewModel.UserProfileViewModel

class UserProfileFragment : Fragment(), LifecycleOwner{

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_user,container,false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val userProfileViewModel = UserProfileViewModel(activity!!.application)
        userProfileViewModel.setUser(arguments?.getLong("userId")!!);
        val user = userProfileViewModel.getUser();
            tv1.text=user.firstName
            tv2.text=user.lastName
            tv3.text=user.email
            tv4.text=user.password
            tv5.text=user.userId.toString()
    }
}