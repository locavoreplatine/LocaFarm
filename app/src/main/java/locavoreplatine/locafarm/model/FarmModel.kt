package locavoreplatine.locafarm.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

@Entity
data class FarmModel(
        var name : String,
        var description : String?,
        var Latitude : Double,
        var Longitude : Double,
        var isBio : Int,
        var producerName: String,
        var addr: String,
        var rating: Int = 0,
        var votingNumber: Int = 0,
        @PrimaryKey(autoGenerate = true)
        var farmId :Long = 0
//        @Ignore var products: List<String>, //TODO change list type and make many to many table
//        @Ignore var commentaries: List<String> //TODO change list type and make many to many table
){

}