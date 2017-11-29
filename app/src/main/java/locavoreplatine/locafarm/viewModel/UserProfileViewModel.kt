package locavoreplatine.locafarm.viewModel

import android.arch.lifecycle.AndroidViewModel
import android.app.Application
import locavoreplatine.locafarm.database.AppDatabase

class UserProfileViewModel(application: Application) : AndroidViewModel(application) {

    var userDao = AppDatabase.getInstance(application).userDao()

    fun dropUserTable(){
        userDao.nukeUserTable()
    }

}