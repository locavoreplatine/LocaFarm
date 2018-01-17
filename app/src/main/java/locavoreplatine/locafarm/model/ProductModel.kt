package locavoreplatine.locafarm.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by toulouse on 17/01/18.
 */
@Entity
data class ProductModel(
        var name : String
){
    @PrimaryKey(autoGenerate = true)
    var productId :Long = 0
}