package locavoreplatine.locafarm.util

import locavoreplatine.locafarm.model.FarmModel
import locavoreplatine.locafarm.model.UserModel

/**
 * Created by sparow on 14/01/18.
 */


class PopulateDatabase {
    companion object {
        @JvmStatic
        fun getSampleUsers(): Array<UserModel> {
            return arrayOf(
            UserModel("Dany Targaryen", "Valyria","mail@locafarm.com","password"),
            UserModel("Rob Stark", "Winterfell","mail@locafarm.com","password"),
            UserModel("Jon Snow", "Castle Black","mail@locafarm.com","password"),
            UserModel("Tyrion Lanister", "King's Landing","mail@locafarm.com","password"))
        }

        @JvmStatic
        fun getSampleFarms(): Array<FarmModel> {
            return arrayOf(
            FarmModel("farm nora","farm01",20.0,20.0,1),
            FarmModel("farm santa","farm02",10.0,10.0,1),
            FarmModel("farm celia","farm03",20.0,20.0,1),
            FarmModel("farm moncef","farm04",10.0,10.0,1))
        }
    }
}