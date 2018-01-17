package locavoreplatine.locafarm.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.ForeignKey.CASCADE
import android.arch.persistence.room.Index


/**
 * Created by toulouse on 17/01/18.
 */


@Entity(tableName = "FarmProductMM",
        primaryKeys = ["farmId", "productId"],
        foreignKeys = [
            (ForeignKey(entity = FarmModel::class, parentColumns = ["farmId"], childColumns = ["farmId"], onDelete = CASCADE)),
            (ForeignKey(entity = ProductModel::class, parentColumns = ["productId"], childColumns = ["productId"], onDelete = CASCADE))
        ],
        indices = [
            (Index(value = ["farmId"])),
            (Index(value = ["productId"]))
        ]
)
data class FarmProductMM(var farmId: Long, var productId: Long)