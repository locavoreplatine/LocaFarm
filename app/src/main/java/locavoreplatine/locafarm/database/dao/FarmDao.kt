package locavoreplatine.locafarm.database.dao

import android.arch.persistence.room.*
import io.reactivex.Flowable
import io.reactivex.Single
import locavoreplatine.locafarm.database.dao.BaseDao
import locavoreplatine.locafarm.model.FarmModel

@Dao
abstract class FarmDao: BaseDao<FarmModel> {
    @Query("SELECT * FROM FarmModel")
    abstract fun all(): Single<List<FarmModel>>

//    @Transaction
//    @Query("SELECT * FROM FarmModel WHERE farmId IN (:farmIds)")
//    abstract fun getMultipleFarmByIds(farmIds: IntArray): List<FarmModel>

    @Query("SELECT count(*) FROM FarmModel")
    abstract fun getFarmCount(): Int

    @Query("SELECT * FROM FarmModel WHERE farmId=:farmId")
    abstract fun getFarmById(farmId: Long?): Single<FarmModel>

    @Transaction
    @Query("SELECT * FROM FarmModel WHERE name LIKE '%' || :farmName || '%' ORDER BY name COLLATE NOCASE ASC LIMIT 10")
    abstract fun findFarmByName(farmName: String?): Flowable<List<FarmModel>>

    @Query("DELETE FROM FarmModel")
    @Deprecated(message = "delete every entry of FarmModel table use it at your own risk")
    abstract fun nukeUserTable()

}