package locavoreplatine.locafarm.database

import android.arch.persistence.room.*
import io.reactivex.Single
import locavoreplatine.locafarm.model.FarmModel

@Dao
interface FarmDao{
    @get:Query("SELECT * FROM FarmModel")
    val all: Single<List<FarmModel>>

    @Query("SELECT * FROM FarmModel WHERE farmId IN (:farmIds)")
    fun getMultipleFarmByIds(farmIds: IntArray): List<FarmModel>

    @Query("SELECT count(*) FROM FarmModel")
    fun getFarmCount(): Int

    @Query("SELECT * FROM FarmModel WHERE farmId=:farmId")
    fun getFarmById(farmId: Int?): Single<FarmModel>

    @Query("SELECT * FROM FarmModel WHERE name LIKE '%' || :arg0 || '%'")
    fun findFarmByName(farmName: String?): Single<List<FarmModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOne(user: FarmModel)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(user: FarmModel)

    @Query("DELETE FROM FarmModel")
    @Deprecated(message = "delete every entry of FarmModel table use it at your own risk")
    fun nukeUserTable()

    @Delete
    fun delete(user: FarmModel)

}