package locavoreplatine.locafarm.viewModel

import android.arch.lifecycle.AndroidViewModel
import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import locavoreplatine.locafarm.database.AppDatabase
import locavoreplatine.locafarm.model.FarmModel
import org.jetbrains.anko.doAsync

class FarmProfileViewModel(application: Application) : AndroidViewModel(application) {

    private var farmDao = AppDatabase.getInstance(application).farmDao()

    private lateinit var farm: FarmModel
    private lateinit var allFarm: List<FarmModel>

    fun setFarm(id: Int) {
        doAsync {
            //user= userDao.findUserByName("Rob Stark").blockingFirst()[0]
            farm = farmDao.findFarmByName("farm nora").blockingFirst()[0]
            //farm = farmDao.getFarmById(id).blockingGet()
        }
    }

    fun getFarm():FarmModel{
        while (!::farm.isInitialized){

        }
        return farm
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