package locavoreplatine.locafarm.viewModel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import locavoreplatine.locafarm.database.AppDatabase
import locavoreplatine.locafarm.model.FarmModel
import locavoreplatine.locafarm.model.UserModel
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * Created by sparow on 1/11/18.
 */


class FinderViewModel(application: Application) : AndroidViewModel(application) {

    var userDao = AppDatabase.getInstance(application).userDao()
    var farmDao = AppDatabase.getInstance(application).farmDao()

    private lateinit var users: Flowable<List<UserModel>>
    private lateinit var farms: Flowable<List<FarmModel>>

    fun findUsersNames(query: String) {
        doAsync {
            users = userDao.findUserByName(query)
        }
    }

    fun findFarmsNames(query: String) {
        doAsync {
            farms = farmDao.findFarmByName(query)
        }
    }

    fun getUsers(): Flowable<List<UserModel>> {
        return users
    }

    fun getFarms(): Flowable<List<FarmModel>> {
        return farms
    }

}

















