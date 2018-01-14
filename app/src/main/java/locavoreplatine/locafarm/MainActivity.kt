package locavoreplatine.locafarm

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import locavoreplatine.locafarm.viewModel.ViewModelFactory
import android.arch.lifecycle.ViewModelProviders
import locavoreplatine.locafarm.viewModel.FinderViewModel
import locavoreplatine.locafarm.di.Injection
import locavoreplatine.locafarm.view.FinderAdapter
import locavoreplatine.locafarm.viewModel.UserProfileViewModel
import android.support.v7.widget.RecyclerView




class MainActivity : AppCompatActivity() {

    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewFinderModel: FinderViewModel
    private lateinit var viewUserModel: UserProfileViewModel

    var recyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        setSupportActionBar(toolbar)
        viewModelFactory = Injection.provideViewModelFactory(application)
        viewUserModel = ViewModelProviders.of(this, viewModelFactory).get(UserProfileViewModel::class.java)
        viewFinderModel = ViewModelProviders.of(this, viewModelFactory).get(FinderViewModel::class.java)
    }

    private fun bindDataToAdapter() {
        // Bind adapter to recycler view object
//        recyclerView!!.adapter = FinderAdapter(getSampleArrayList())
    }
}
