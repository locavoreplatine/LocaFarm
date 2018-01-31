package locavoreplatine.locafarm.database.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import io.reactivex.Flowable
import locavoreplatine.locafarm.model.FarmModel
import locavoreplatine.locafarm.model.Favorites
import locavoreplatine.locafarm.model.ProductModel

/**
 * Created by toulouse on 31/01/18.
 */
@Dao
abstract class FavoritesDao : BaseDao<Favorites>{

    @Query("SELECT FarmModel.* FROM FarmModel\n" +
            "INNER JOIN Favorites ON FarmModel.farmId=Favorites.farmId\n" +
            "WHERE Favorites.userId=:userId")
    abstract fun favoritesObservable(userId: Long): Flowable<List<FarmModel>>

    @Query("SELECT FarmModel.* FROM FarmModel\n" +
            "INNER JOIN Favorites ON FarmModel.farmId=Favorites.farmId\n" +
            "WHERE Favorites.userId=:userId")
    abstract fun favoritesInstant(userId: Long): List<FarmModel>

    @Query("SELECT count(*) FROM Favorites")
    abstract fun count(): Int

}