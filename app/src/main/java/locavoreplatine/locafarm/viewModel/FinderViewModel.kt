package locavoreplatine.locafarm.viewModel

import android.app.Application
import android.arch.lifecycle.*
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import locavoreplatine.locafarm.database.AppDatabase
import locavoreplatine.locafarm.model.FarmModel
import locavoreplatine.locafarm.model.UserModel
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * Created by sparow on 1/11/18.
 */


class FinderViewModel(application: Application) : AndroidViewModel(application) {

    private val userDao = AppDatabase.getInstance(application).userDao()
    private val farmDao = AppDatabase.getInstance(application).farmDao()

    private lateinit var users: LiveData<List<UserModel>>
    private lateinit var farms: LiveData<List<FarmModel>>


    fun findUsersNames(query: String) {

            users = LiveDataReactiveStreams.fromPublisher(userDao.findUserByName(query)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread()))
    }

    fun findFarmsNames(query: String) {

            farms = LiveDataReactiveStreams.fromPublisher(farmDao.findFarmByName(query)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread()))
    }

    fun getUsers(): LiveData<List<UserModel>> {
        return users
    }

    fun getFarms(): LiveData<List<FarmModel>> {
        return farms
    }

}

















