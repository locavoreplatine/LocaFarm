package locavoreplatine.locafarm.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class UserModel(
        var firstName : String,
        var lastName : String,
        var email : String,
        var password : String
){
    @PrimaryKey(autoGenerate = true)
    var userId :Long = 0
}