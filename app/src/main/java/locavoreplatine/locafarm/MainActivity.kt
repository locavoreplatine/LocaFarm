package locavoreplatine.locafarm

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import locavoreplatine.locafarm.viewModel.ViewModelFactory
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.widget.LinearLayoutManager
import locavoreplatine.locafarm.viewModel.FinderViewModel
import locavoreplatine.locafarm.di.Injection
import locavoreplatine.locafarm.view.FinderRecyclerViewAdapter
import locavoreplatine.locafarm.viewModel.UserProfileViewModel
import kotlinx.android.synthetic.main.activity_main_content.*
import android.support.v7.widget.DefaultItemAnimator
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info


class MainActivity : AppCompatActivity(), AnkoLogger {

    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewFinderModel: FinderViewModel
    private lateinit var viewUserModel: UserProfileViewModel

//    private lateinit var disposable: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        setSupportActionBar(toolbar)
        viewModelFactory = Injection.provideViewModelFactory(application)
        viewUserModel = ViewModelProviders.of(this, viewModelFactory).get(UserProfileViewModel::class.java)
        viewFinderModel = ViewModelProviders.of(this, viewModelFactory).get(FinderViewModel::class.java)

        //RecyclerView
        finder_recycler_view.layoutManager = LinearLayoutManager(this)
        finder_recycler_view.hasFixedSize()
        finder_recycler_view.itemAnimator = DefaultItemAnimator()
        // Bind adapter to recycler view object
        finder_recycler_view.adapter = FinderRecyclerViewAdapter(getSampleArrayList())
    }

    //For testing
    private fun getSampleArrayList(): ArrayList<Any> {
        val items: ArrayList<Any> = ArrayList()
        items.addAll(viewUserModel.userDao.all.blockingGet())
        info(viewUserModel.userDao.all.blockingGet())
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
        return ArrayList(items.sortedWith(compareBy({ it.javaClass.toString()})))
    }
}
