package locavoreplatine.locafarm.viewModel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import io.reactivex.Flowable
import locavoreplatine.locafarm.database.AppDatabase
import locavoreplatine.locafarm.model.FarmModel
import locavoreplatine.locafarm.model.Favorites
import locavoreplatine.locafarm.model.UserModel
import org.jetbrains.anko.doAsync

/**
 * Created by toulouse on 31/01/18.
 */
class FavoritesViewModel (application: Application) : AndroidViewModel(application) {
    private var favoritesDao = AppDatabase.getInstance(application).favoritesDao()
    private var userDao = AppDatabase.getInstance(application).userDao()
    private lateinit var user: UserModel
    private lateinit var favorites : Flowable<List<FarmModel>>

    fun setUser(userId: Long){
        //TODO replace with real userId when available
        doAsync {
            user = userDao.all().blockingGet().get(0)
            favorites = favoritesDao.favoritesObservable(user.userId)
        }
    }

    fun getFavorites(): Flowable<List<FarmModel>> {
        while (!::favorites.isInitialized){

        }
        return favorites
    }

    fun addFavorite(farmId : Long){
        while (!::user.isInitialized){

        }
        doAsync {
            val favorites = Favorites(farmId,user.userId)
            favoritesDao.insert(favorites)
        }
    }

    fun deleteFavorite(farmId: Long){
        while (!::user.isInitialized){

        }
        doAsync {
            val favorites = Favorites(farmId,user.userId)
            favoritesDao.delete(favorites)
        }
    }
}