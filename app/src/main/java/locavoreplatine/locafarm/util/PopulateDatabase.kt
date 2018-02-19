package locavoreplatine.locafarm.util

import locavoreplatine.locafarm.model.FarmModel
import locavoreplatine.locafarm.model.FarmProductMM
import locavoreplatine.locafarm.model.ProductModel

/**
 * Created by sparow on 14/01/18.
 */


class PopulateDatabase {
    companion object {

        @JvmStatic
        fun getSampleFarms(): Array<FarmModel> {
            return arrayOf(

                    FarmModel("Le drive fermier de Lomme", "7 agriculteurs et l'exploitation du lycée Horticole de Lomme ont décidé de se regrouper pour monter le projet du Drive Fermier de Lomme.", 50.649149, 2.993104, 0, "Coopérative", "77 rue de la Mitterie Nord/Lomme (59160),France",0,0),
                    FarmModel("La Flamanderie - Ferme DEMAN", "La ferme est une bâtisse ancienne en briques rouges. Le point de vente est en front à rue. Toute la découpe et les préparations sont faites sur place dans notre atelier de transformation", 50.5711717, 3.051176, 0, "Gonzague et Jean-Roch Deman ", " 3 rue Etienne Dolet Impasse de l'Eglise 59175 TEMPLEMARS",0,0),
                    FarmModel("Le rayon fruits et légumes du Panier VertAu Panier Vert", "Venez découvrir, seul ou en groupe, une exploitation agricole et son environnement humain, animal et naturel", 50.7138968,  3.0001634000000195, 1, "Christine Delecluse", "21 Rue de Comines 59890 QUESNOY SUR DEULE",0,0)
            )
        }

        @JvmStatic
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