package locavoreplatine.locafarm.di

import android.app.Application
import locavoreplatine.locafarm.viewModel.ViewModelFactory

/**
 * Created by sparow on 13/01/18.
 */

object Injection {

    fun provideViewModelFactory(application: Application): ViewModelFactory {
        return ViewModelFactory(application)
    }
}