package locavoreplatine.locafarm.viewModel

import android.app.Application
import android.arch.lifecycle.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import locavoreplatine.locafarm.database.AppDatabase
import locavoreplatine.locafarm.model.FarmModel

/**
 * Created by sparow on 1/11/18.
 */


class FinderViewModel(application: Application) : AndroidViewModel(application) {

    private val farmDao = AppDatabase.getInstance(application).farmDao()

    private lateinit var farms: LiveData<List<FarmModel>>

    fun findFarmsNames(query: String) {

            farms = LiveDataReactiveStreams.fromPublisher(farmDao.findFarmByName(query)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread()))
    }

    fun getFarms(): LiveData<List<FarmModel>> {
        return farms
    }

}

















