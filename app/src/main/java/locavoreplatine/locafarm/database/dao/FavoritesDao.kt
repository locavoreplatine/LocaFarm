package locavoreplatine.locafarm.database.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import android.arch.persistence.room.Transaction
import io.reactivex.Flowable
import io.reactivex.Single
import locavoreplatine.locafarm.model.FarmModel
import locavoreplatine.locafarm.model.Favorites
import locavoreplatine.locafarm.model.ProductModel

/**
 * Created by toulouse on 31/01/18.
 */
@Dao
abstract class FavoritesDao : BaseDao<Favorites>{

    @Transaction
    @Query("SELECT FarmModel.* FROM FarmModel\n" +
            "INNER JOIN Favorites ON FarmModel.farmId=Favorites.farmId\n")
    abstract fun favoritesObservable(): Flowable<List<FarmModel>>

    @Transaction
    @Query("SELECT FarmModel.* FROM FarmModel\n" +
            "INNER JOIN Favorites ON FarmModel.farmId=Favorites.farmId\n")
    abstract fun favoritesSingle(): Single<List<FarmModel>>


    @Query("SELECT count(*) FROM Favorites")
    abstract fun count(): Int

}