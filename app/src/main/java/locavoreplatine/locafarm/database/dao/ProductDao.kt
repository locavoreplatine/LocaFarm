package locavoreplatine.locafarm.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import io.reactivex.Single
import locavoreplatine.locafarm.model.ProductModel

/**
 * Created by toulouse on 22/01/18.
 */

@Dao
abstract class ProductDao: BaseDao<ProductModel> {
    @Query("SELECT * FROM ProductModel")
    abstract fun all(): Single<List<ProductModel>>
}