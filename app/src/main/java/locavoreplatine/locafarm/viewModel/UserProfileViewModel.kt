package locavoreplatine.locafarm.viewModel

import android.arch.lifecycle.AndroidViewModel
import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import locavoreplatine.locafarm.database.AppDatabase
import locavoreplatine.locafarm.model.UserModel
import org.jetbrains.anko.doAsync

class UserProfileViewModel(application: Application) : AndroidViewModel(application) {

    var userDao = AppDatabase.getInstance(application).userDao()

    var user: MutableLiveData<UserModel> = MutableLiveData()

    fun setUser(id: Int) {
        doAsync {
            user.postValue(userDao.getUserById(id).value)
        }
    }

    fun getUser():LiveData<UserModel>{
        return user
    }

    fun deleteUser(){
        userDao.delete(user.value!!)
    }

}