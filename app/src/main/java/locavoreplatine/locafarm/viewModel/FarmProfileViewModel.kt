package locavoreplatine.locafarm.viewModel

import android.arch.lifecycle.AndroidViewModel
import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import locavoreplatine.locafarm.database.AppDatabase
import locavoreplatine.locafarm.model.FarmModel
import org.jetbrains.anko.doAsync

class FarmProfileViewModel(application: Application) : AndroidViewModel(application) {

    var farmDao = AppDatabase.getInstance(application).farmDao()

    var farm: MutableLiveData<FarmModel> = MutableLiveData()

    fun setFarm(id: Int) {
        doAsync {
            farm.postValue(farmDao.getFarmById(id).blockingGet())
        }
    }

    fun getFarm():LiveData<FarmModel>{
        return farm
    }

    fun deleteFarm(){
        farmDao.delete(farm.value!!)
    }

}