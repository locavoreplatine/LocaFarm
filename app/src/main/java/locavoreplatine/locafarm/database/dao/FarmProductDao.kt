package locavoreplatine.locafarm.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import locavoreplatine.locafarm.model.FarmModel
import locavoreplatine.locafarm.model.FarmProductMM
import locavoreplatine.locafarm.model.ProductModel

/**
 * Created by toulouse on 17/01/18.
 */
@Dao
abstract class FarmProductDao : BaseDao<FarmProductMM>{
    @Query("SELECT FarmModel.* FROM FarmModel\n" +
            "INNER JOIN FarmProductMM ON FarmModel.farmId=FarmProductMM.farmId\n" +
            "WHERE FarmProductMM.productId=:productId")
    abstract fun farmByProduct(productId: Long): List<FarmModel>

    @Query("SELECT ProductModel.* FROM ProductModel\n" +
            "INNER JOIN FarmProductMM ON ProductModel.productId=FarmProductMM.productId\n" +
            "WHERE FarmProductMM.farmId=:farmId")
    abstract fun productByFarm(farmId: Long): List<ProductModel>
}