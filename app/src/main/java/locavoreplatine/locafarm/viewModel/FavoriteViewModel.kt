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


class FavoriteViewModel(application: Application) : AndroidViewModel(application) {

    private val favoritesDao = AppDatabase.getInstance(application).favoritesDao()

    private lateinit var favoritesObservables: LiveData<List<FarmModel>>

    private lateinit var allFavorites: List<FarmModel>

    fun findFav() {

            favoritesObservables = LiveDataReactiveStreams.fromPublisher(favoritesDao.favoritesObservable()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread()))
    }

    fun getFarms(): LiveData<List<FarmModel>> {
        return favoritesObservables
    }


    fun favoritesSingle(): List<FarmModel> {
        doAsync {
            allFavorites = favoritesDao.favoritesSingle().blockingGet()
        }
        while (!::allFavorites.isInitialized){

        }
        return allFavorites
    }

}

















