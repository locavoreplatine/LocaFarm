package locavoreplatine.locafarm.viewModel

import android.arch.lifecycle.AndroidViewModel
import android.app.Application
import locavoreplatine.locafarm.database.AppDatabase

class UserProfilViewModel(application: Application) : AndroidViewModel(application) {

    var userDao = AppDatabase.getInstance(application)

}