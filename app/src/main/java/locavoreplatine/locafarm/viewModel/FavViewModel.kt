package locavoreplatine.locafarm.viewModel

import android.app.Application
import android.arch.lifecycle.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import locavoreplatine.locafarm.database.AppDatabase
import locavoreplatine.locafarm.model.FarmModel
import org.jetbrains.anko.doAsync

/**
 * Created by sparow on 1/11/18.
 */


class FavViewModel(application: Application) : AndroidViewModel(application) {

    private val favDao = AppDatabase.getInstance(application).favoritesDao()

    private lateinit var farms: LiveData<List<FarmModel>>

    private lateinit var allFarm: List<FarmModel>

    fun findFav() {

            farms = LiveDataReactiveStreams.fromPublisher(favDao.favoritesObservable()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread()))
    }

    fun getFarms(): LiveData<List<FarmModel>> {
        return farms
    }


    fun all(): List<FarmModel> {
        doAsync {
            allFarm = favDao.all().blockingGet()
        }
        while (!::allFarm.isInitialized){

        }
        return allFarm
    }

}

















