@file:Suppress("UNCHECKED_CAST")

package locavoreplatine.locafarm.viewModel

/**
 * Created by sparow on 12/01/18.
 */

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider


/**
 * Factory for ViewModels
 */
class ViewModelFactory(private val mAppplication: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FinderViewModel::class.java)) {
            return FinderViewModel(mAppplication) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}