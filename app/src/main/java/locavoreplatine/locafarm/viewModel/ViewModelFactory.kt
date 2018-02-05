package locavoreplatine.locafarm.viewModel

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

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
        return when {
            modelClass.isAssignableFrom(FarmProfileViewModel::class.java) -> FarmProfileViewModel(application = application) as T
            modelClass.isAssignableFrom(FinderViewModel::class.java) -> FinderViewModel(application = application) as T
            modelClass.isAssignableFrom(FavViewModel::class.java) -> FavViewModel(application = application) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }

    }
}