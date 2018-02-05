package locavoreplatine.locafarm.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Index

/**
 * Created by toulouse on 31/01/18.
 */


@Entity(tableName = "Favorites",
        primaryKeys = ["farmId"],
        foreignKeys = [
            (ForeignKey(entity = FarmModel::class, parentColumns = ["farmId"], childColumns = ["farmId"], onDelete = ForeignKey.CASCADE))
        ],
        indices = [
            (Index(value = ["farmId"]))
        ]
)
data class Favorites (var farmId: Long)