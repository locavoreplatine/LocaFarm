package locavoreplatine.locafarm

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import locavoreplatine.locafarm.viewModel.ViewModelFactory
import android.arch.lifecycle.ViewModelProviders
import locavoreplatine.locafarm.viewModel.FinderViewModel
import locavoreplatine.locafarm.di.Injection
import locavoreplatine.locafarm.viewModel.UserProfileViewModel


class MainActivity : AppCompatActivity() {

    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewFinderModel: FinderViewModel
    private lateinit var viewUserModel: UserProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        setSupportActionBar(toolbar)
        viewModelFactory = Injection.provideViewModelFactory(application)
        viewUserModel = ViewModelProviders.of(this, viewModelFactory).get(UserProfileViewModel::class.java)
        viewFinderModel = ViewModelProviders.of(this, viewModelFactory).get(FinderViewModel::class.java)
    }
}
