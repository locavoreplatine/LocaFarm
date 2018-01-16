package locavoreplatine.locafarm.view.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import locavoreplatine.locafarm.viewModel.ViewModelFactory
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import kotlinx.android.synthetic.main.activity_main_content.*
import locavoreplatine.locafarm.viewModel.FinderViewModel
import locavoreplatine.locafarm.di.Injection
import locavoreplatine.locafarm.viewModel.UserProfileViewModel
import locavoreplatine.locafarm.R
import locavoreplatine.locafarm.database.AppDatabase
import locavoreplatine.locafarm.model.FarmModel
import locavoreplatine.locafarm.model.UserModel
import locavoreplatine.locafarm.util.replaceFragment
import locavoreplatine.locafarm.view.fragment.UserProfileFragment
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.bundleOf
import org.jetbrains.anko.info


class MainActivity : AppCompatActivity(), AnkoLogger {

    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var finderViewModel: FinderViewModel
    private lateinit var userProfileViewModel: UserProfileViewModel


//    private lateinit var disposable: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        setSupportActionBar(toolbar)
        //todo remove when put in production
//        AppDatabase.TEST_MODE=true
        viewModelFactory = Injection.provideViewModelFactory(application)
        userProfileViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserProfileViewModel::class.java)
        finderViewModel = ViewModelProviders.of(this, viewModelFactory).get(FinderViewModel::class.java)


        //todo remove when put in production
        val testUserProfilFragment = UserProfileFragment()
        testUserProfilFragment.arguments = bundleOf(Pair("id",0))

        replaceFragment(testUserProfilFragment,main_activityfragment_container.id)
    }

//    //For testing
//    private fun getSampleArrayList(): ArrayList<Any> {
//        val items: ArrayList<Any> = ArrayList()
//        items.addAll(AppDatabase.getInstance(application).userDao().all.blockingGet())
//        info(AppDatabase.getInstance(application).userDao().all.blockingGet())
//            items.add(UserModel("Dany Targaryen", "Valyria","mail@locafarm.com","password"))
//            items.add(UserModel("Rob Stark", "Winterfell","mail@locafarm.com","password"))
//            items.add(FarmModel("farm santa","farm01",10.0,10.0,1))
//            items.add(UserModel("Jon Snow", "Castle Black","mail@locafarm.com","password"))
//            items.add(FarmModel("farm nora","farm01",20.0,20.0,1))
//            items.add(UserModel("Tyrion Lanister", "King's Landing","mail@locafarm.com","password"))
//            items.add(UserModel("Dany Targaryen", "Valyria","mail@locafarm.com","password"))
//            items.add(UserModel("Rob Stark", "Winterfell","mail@locafarm.com","password"))
//            items.add(FarmModel("farm santa","farm01",10.0,10.0,1))
//            items.add(UserModel("Jon Snow", "Castle Black","mail@locafarm.com","password"))
//            items.add(FarmModel("farm nora","farm01",20.0,20.0,1))
//            items.add(UserModel("Tyrion Lanister", "King's Landing","mail@locafarm.com","password"))
//            items.add("Default")
//        return ArrayList(items.sortedWith(compareBy({ it.javaClass.toString()})))
//    }
}
