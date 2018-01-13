package locavoreplatine.locafarm.viewModel

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.annotation.SuppressLint

/**
 * Created by sparow on 12/01/18.
 */


@Suppress("UNCHECKED_CAST")
/**
 * Factory for ViewModels
 */
class ViewModelFactory(private val application: Application): ViewModelProvider.Factory {


//    private lateinit var mApplication: Application

//    private fun ViewModelFactory(application: Application) {
//        mApplication = application
//    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserProfileViewModel::class.java)) {
            return UserProfileViewModel(application = application) as T
        }else if (modelClass.isAssignableFrom(FinderViewModel::class.java)) {
            return FinderViewModel(application = application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }
}