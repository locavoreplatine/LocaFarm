package locavoreplatine.locafarm.database

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import locavoreplatine.locafarm.model.FarmModel

@Dao
interface FarmDao{
    @get:Query("SELECT * FROM FarmModel")
    val all: LiveData<List<FarmModel>>

    @Query("SELECT * FROM FarmModel WHERE farmId IN (:farmIds)")
    fun getMultipleByIds(farmIds: IntArray): List<FarmModel>

    @Query("SELECT count(*) FROM FarmModel")
    fun getUserCount(): Int

    @Query("SELECT * FROM FarmModel WHERE farmId=:farmId")
    fun getUserById(farmId: Int?): LiveData<FarmModel>

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