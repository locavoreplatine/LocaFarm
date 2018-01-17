package locavoreplatine.locafarm.viewModel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.util.Log
import locavoreplatine.locafarm.database.AppDatabase
import locavoreplatine.locafarm.model.UserModel
import org.jetbrains.anko.doAsync

class UserProfileViewModel(application: Application) : AndroidViewModel(application) {

    private var userDao = AppDatabase.getInstance(application).userDao()

    private lateinit var user: UserModel
    private lateinit var allUser: List<UserModel>

    fun setUser(id: Int) {
//        doAsync {
//            user=userDao.getUserById(id).blockingGet()
//        }
        doAsync {
            val t = userDao.all.blockingGet().size
            Log.e("setuser","$t")
            user=userDao.findUserByName("Rob Stark").blockingFirst().get(0)
        }
    }

    fun getUser():UserModel{
        while (!::user.isInitialized){

        }
        return user
    }

    fun deleteUser(){
        userDao.delete(user)
    }

    fun all():List<UserModel>{
        doAsync {
            allUser = userDao.all.blockingGet()
        }
        while (!::allUser.isInitialized){

        }
        return allUser
    }

}