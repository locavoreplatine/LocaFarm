package locavoreplatine.locafarm.database

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import locavoreplatine.locafarm.model.UserModel

@Dao
interface UserDao {
    @get:Query("SELECT * FROM user")
    val all: LiveData<List<UserModel>>

    @Query("SELECT * FROM user WHERE userId IN (:arg0)")
    fun getMultipleByIds(userIds: IntArray): List<UserModel>

    @Query("SELECT count(*) FROM user")
    fun getUserCount(): Int

    @Query("SELECT * FROM user WHERE userId=:arg0")
    fun getUserById(userId: Int?): LiveData<UserModel>

    @Insert(onConflict = REPLACE)
    fun insertOne(user: UserModel)

    @Update(onConflict = REPLACE)
    fun update(user: UserModel)

    @Query("DELETE FROM user")
    fun flushSensorData()

    @Delete
    fun delete(user: UserModel)
}