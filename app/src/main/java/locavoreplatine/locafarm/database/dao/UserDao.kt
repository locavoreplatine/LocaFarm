package locavoreplatine.locafarm.database.dao


import android.arch.persistence.room.*
import io.reactivex.Flowable
import io.reactivex.Single
import locavoreplatine.locafarm.database.dao.BaseDao
import locavoreplatine.locafarm.model.UserModel

@Dao
abstract class UserDao: BaseDao<UserModel> {
    @get:Query("SELECT * FROM UserModel")
    abstract val all: Single<List<UserModel>>

    @Transaction
    @Query("SELECT * FROM UserModel WHERE userId IN (:userIds)")
    abstract fun getMultipleByIds(userIds: IntArray): List<UserModel>

    @Query("SELECT count(*) FROM UserModel")
    abstract fun getUserCount(): Int

    @Query("SELECT * FROM UserModel WHERE userId=:userId")
    abstract fun getUserById(userId: Int?): Single<UserModel>

    @Transaction
    @Query("SELECT * FROM UserModel WHERE firstName LIKE '%' || :userName || '%' ORDER BY firstName COLLATE NOCASE ASC LIMIT 10")
    abstract fun findUserByName(userName: String?): Flowable<List<UserModel>>

    @Query("DELETE FROM UserModel")
    @Deprecated(message = "delete every entry of UserModel table use it at your own risk")
    abstract fun nukeUserTable()
}