package locavoreplatine.locafarm.util

import locavoreplatine.locafarm.model.FarmModel
import locavoreplatine.locafarm.model.ProductModel
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
            UserModel("Tyrion Lanister", "King's Landing","mail@locafarm.com","password"),
            UserModel("Ragnarok Nora", "Valyria","mail@locafarm.com","password"),
            UserModel("Sparow Coal", "Winterfell","mail@locafarm.com","password"),
            UserModel("Stallone Celia", "Castle Black","mail@locafarm.com","password"),
            UserModel("Laguertha Jugurtha", "Castle Black","mail@locafarm.com","password"),
            UserModel("Kaal Drogo", "King's Landing","mail@locafarm.com","password"))
        }

        @JvmStatic
        fun getSampleFarms(): Array<FarmModel> {
            return arrayOf(
            FarmModel("farm nora","farm01",50.64570390000001,3.0609391000000414,1,"nora","8 Rue de Jemmapes, Lille, France"),
            FarmModel("farm santa","farm02",10.0,10.0,0,"santa","2"),
            FarmModel("farm celia","farm03",20.0,20.0,1,"celia","3"),
            FarmModel("farm moncef","farm04",10.0,10.0,0,"moncef","4"),
            FarmModel("farm violetta","farm05",20.0,20.0,1,"violetta","5"),
            FarmModel("farm bouacem","farm06",10.0,10.0,0,"bouacem","6"),
            FarmModel("farm razouk","farm07",20.0,20.0,1,"razouk","7"),
            FarmModel("farm damien","farm08",10.0,10.0,0,"damien","8"))
        }

        fun getSampleProduct(): Array<ProductModel> {
            return arrayOf(
                    ProductModel("miel"),
                    ProductModel("oeuf"),
                    ProductModel("lait"),
                    ProductModel("fromage de chèvre"),
                    ProductModel("salade"),
                    ProductModel("viande de mouton"),
                    ProductModel("saumon"),
                    ProductModel("viande de boeuf"),
                    ProductModel("fromage de vache"),
                    ProductModel("fraise"),
                    ProductModel("framboise"),
                    ProductModel("cassis"),
                    ProductModel("myrtille"),
                    ProductModel("radis"),
                    ProductModel("beure"),
                    ProductModel("asperge"),
                    ProductModel("chataigne"),
                    ProductModel("maron"),
                    ProductModel("pomme"),
                    ProductModel("poire"),
                    ProductModel("viande de porc"),
                    ProductModel("courgette"),
                    ProductModel("bar"),
                    ProductModel("vin"),
                    ProductModel("eau de vie"),
                    ProductModel("jus de pomme"),
                    ProductModel("jus d'ananas"),
                    ProductModel("jus d'orange"),
                    ProductModel("orange"),
                    ProductModel("mandarine")
            )
        }
    }
}