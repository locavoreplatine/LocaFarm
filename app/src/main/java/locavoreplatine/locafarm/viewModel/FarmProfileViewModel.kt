package locavoreplatine.locafarm.viewModel

import android.arch.lifecycle.AndroidViewModel
import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import locavoreplatine.locafarm.database.AppDatabase
import locavoreplatine.locafarm.model.*
import org.jetbrains.anko.doAsync

class FarmProfileViewModel(application: Application) : AndroidViewModel(application) {
    private var farmDao = AppDatabase.getInstance(application).farmDao()
    private var farmProductDao = AppDatabase.getInstance(application).farmProductDao()
    private var userDao = AppDatabase.getInstance(application).userDao()
    private val favoritesDao = AppDatabase.getInstance(application).favoritesDao()
    private lateinit var farm: FarmModel
    private lateinit var farmProduct: List<ProductModel>
    private lateinit var allFarm: List<FarmModel>
    private lateinit var user: UserModel
    private var isFavorites = MutableLiveData<Boolean>()

    fun init(userId : Long, farmId : Long){
        setFarm(farmId)
        setUser(userId)
        while (!::farm.isInitialized && !::user.isInitialized){

        }
        doAsync {
            var list =  emptyList<FarmModel>()
            list = favoritesDao.favoritesInstant(user.userId)
            isFavorites.postValue(list.contains(farm))
        }
    }

    private fun setFarm(id: Long) {
        doAsync {
            farm = farmDao.getFarmById(id).blockingGet()
            farmProduct = farmProductDao.productByFarm(farm.farmId)
        }
    }

    private fun setUser(id: Long) {
        //TODO replace with real userId when available
        doAsync {
            user = userDao.all().blockingGet().get(0)
        }
    }

    fun getFarm():FarmModel{
        while (!::farm.isInitialized){

        }
        return farm
    }

    fun getProduct(): List<ProductModel>{
        while (!::farmProduct.isInitialized){

        }
        return farmProduct
    }

    fun deleteFarm(){
        farmDao.delete(farm)
    }

    fun all(): List<FarmModel> {
        doAsync {
            allFarm = farmDao.all().blockingGet()
        }
        while (!::allFarm.isInitialized){

        }
        return allFarm
    }

    fun isFavorite(): LiveData<Boolean> {
        return isFavorites
    }

    fun addFavorite(){
        while (!::farm.isInitialized && !::user.isInitialized){

        }
        val favorites = Favorites(farm.farmId,user.userId)
        Log.e("Vm", "${favorites.farmId} ${favorites.userId}")
        favoritesDao.insert(favorites)
        Log.e("Vm", "${favoritesDao.count()}")
        isFavorites.postValue(true)
    }

    fun removeFavorite(){
        while (!::farm.isInitialized && !::user.isInitialized){

        }
        val favorites = Favorites(farm.farmId,user.userId)
        favoritesDao.delete(favorites)
        Log.e("Vm", "${favoritesDao.count()}")
        isFavorites.postValue(false)
    }

}