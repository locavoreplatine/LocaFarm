package locavoreplatine.locafarm.database

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import locavoreplatine.locafarm.model.UserModel

@Dao
interface UserDao {
    @get:Query("SELECT * FROM UserModel")
    val all: LiveData<List<UserModel>>

    @Query("SELECT * FROM UserModel WHERE userId IN (:userIds)")
    fun getMultipleByIds(userIds: IntArray): List<UserModel>

    @Query("SELECT count(*) FROM UserModel")
    fun getUserCount(): Int

    @Query("SELECT * FROM UserModel WHERE userId=:userId")
    fun getUserById(userId: Int?): LiveData<UserModel>

    @Insert(onConflict = REPLACE)
    fun insertOne(user: UserModel)

    @Update(onConflict = REPLACE)
    fun update(user: UserModel)

    @Query("DELETE FROM UserModel")
    @Deprecated(message = "delete every entry of UserModel table use it at your own risk")
    fun nukeUserTable()

    @Delete
    fun delete(user: UserModel)

}