package locavoreplatine.locafarm.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Index

/**
 * Created by toulouse on 31/01/18.
 */


@Entity(tableName = "Favorites",
        primaryKeys = ["userId", "farmId"],
        foreignKeys = [
            (ForeignKey(entity = FarmModel::class, parentColumns = ["farmId"], childColumns = ["farmId"], onDelete = ForeignKey.CASCADE)),
            (ForeignKey(entity = UserModel::class, parentColumns = ["userId"], childColumns = ["userId"], onDelete = ForeignKey.CASCADE))
        ],
        indices = [
            (Index(value = ["farmId"])),
            (Index(value = ["userId"]))
        ]
)
data class Favorites (var farmId: Long, var userId: Long)