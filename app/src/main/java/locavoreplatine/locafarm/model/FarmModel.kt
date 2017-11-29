package locavoreplatine.locafarm.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

@Entity
data class FarmModel(
        val name : String,
        val description : String?,
        val Latitude : Double,
        val Longitude : Double,
        val isBio : Int
//        @Ignore val products: List<String>, //TODO change list type and make many to many table
//        @Ignore val commentaries: List<String> //TODO change list type and make many to many table
){
    @PrimaryKey(autoGenerate = true)
    var farmId = 0
}