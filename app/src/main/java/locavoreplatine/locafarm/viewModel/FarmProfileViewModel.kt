package locavoreplatine.locafarm.viewModel

import android.arch.lifecycle.AndroidViewModel
import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import locavoreplatine.locafarm.database.AppDatabase
import locavoreplatine.locafarm.model.FarmModel
import locavoreplatine.locafarm.model.FarmProductMM
import locavoreplatine.locafarm.model.ProductModel
import org.jetbrains.anko.doAsync

class FarmProfileViewModel(application: Application) : AndroidViewModel(application) {

    private var farmDao = AppDatabase.getInstance(application).farmDao()
    private var farmProductDao = AppDatabase.getInstance(application).farmProductDao()

    private lateinit var farm: FarmModel
    private lateinit var farmProduct: List<ProductModel>
    private lateinit var allFarm: List<FarmModel>

    fun setFarm(id: Int) {
        doAsync {
            Log.e("farmViewModel", "${farmDao.all().blockingGet().size}")
            Log.e("farmViewModel", "${farmDao.findFarmByName("farm nora").blockingFirst()[0].name}")
            farm = farmDao.findFarmByName("farm nora").blockingFirst()[0]
            farmProduct = farmProductDao.productByFarm(farm.farmId)
            //farm = farmDao.getFarmById(id).blockingGet()
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

}