package locavoreplatine.locafarm

import locavoreplatine.locafarm.database.AppDatabase
import locavoreplatine.locafarm.model.FarmModel
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import locavoreplatine.locafarm.model.FarmProductMM
import locavoreplatine.locafarm.model.ProductModel
import org.junit.*
import org.junit.runner.RunWith


/**
 * Created by sparow on 2/19/2018.
 */

@RunWith(AndroidJUnit4::class)
class FarmInstrumentedTest {

    private lateinit var database: AppDatabase

    @Before
    fun initDb() {
        AppDatabase.TEST_MODE = true
        database = AppDatabase.getInstance(InstrumentationRegistry.getTargetContext())
    }

    @After
    fun closeDb() {
        //database.close()
    }

    @Test
    fun getFarmByIdOrNameTest() {
        database.farmDao().getFarmById(123456789)
                .test()
                .assertNoValues()

        database.farmDao().findFarmByName("123456789")
                .test()
                .assertNoValues()
    }

    @Test fun insertAndGetFarmTest() {

        database.farmDao().insert(FARM)
        val tmp =  database.farmDao().findFarmByName(FARM.name).blockingFirst().first()
        database.farmDao().getFarmById(tmp.farmId)
                .test()
                .assertValue {
                    it.farmId == tmp.farmId && it.name == FARM.name
                }

        Assert.assertEquals(database.farmDao().findFarmByName(FARM.name).blockingFirst().indexOf(FARM), 0)
        Assert.assertEquals(database.farmDao().findFarmByName("Le").blockingFirst().first().name, "Le drive fermier de Lomme")
    }

    @Test fun updateAndGetFarmTest() {

        database.farmDao().insert(FARM)
        val updatedFarm = FarmModel("mosmos","farm01",20.0,20.0,1,"mama","address",0,0)
        database.farmDao().insert(updatedFarm)
        val tmp =  database.farmDao().findFarmByName(updatedFarm.name).blockingFirst().first()
        database.farmDao().getFarmById(tmp.farmId)
                .test()
                .assertValue {
                    it.farmId == tmp.farmId && it.name == updatedFarm.name
                }

        Assert.assertEquals(database.farmDao().findFarmByName(updatedFarm.name).blockingFirst().indexOf(updatedFarm), 0)
    }

    @Test fun deleteAndGetFarmByIdTest() {

        database.farmDao().insert(FARM)
        database.farmDao().delete(FARM)
        database.farmDao().findFarmByName(FARM.name)
                .test()
                .assertNoValues()
    }

    @Test fun insertAndGetFarmCountTest() {
        database.farmDao().insert(FARM)
        Assert.assertEquals(database.farmDao().getFarmCount(), 4)
    }


    @Test fun getAllFarmTest() {

        database.farmDao().insert(FARM)
        val tmp =  database.farmDao().findFarmByName(FARM.name).blockingFirst().first()
        database.farmDao().all()
                .test()
                .assertValue {
                    val tmpFarm = it.first()
                    tmpFarm.farmId == tmp.farmId && tmpFarm.name == tmp.name
                }
        database.farmDao().all()
                .test()
                .assertNoErrors()

    }


    //Product
    @Test fun insertAndGetProductByFarmTest() {
        database.productDao().insert(PRODUCT)
        database.farmDao().insert(FARM)
        val tmp =  database.farmDao().findFarmByName(FARM.name).blockingFirst().first()
        database.farmProductDao().insert(FarmProductMM(tmp.farmId,31))

        Assert.assertEquals(database.farmProductDao().productByFarm(tmp.farmId).first().productId,31)
    }

    @Test fun insertProductAndGetFarmByProductTest() {
        database.productDao().insert(PRODUCT)
        database.farmDao().insert(FARM)
        val tmp =  database.farmDao().findFarmByName(FARM.name).blockingFirst().first()
        database.farmProductDao().insert(FarmProductMM(tmp.farmId,31))
        Assert.assertEquals(database.farmProductDao().farmByProduct(31).first().farmId,tmp.farmId)
    }


//    @Suppress("DEPRECATION")
//    @Test fun nukFarmsTest() {
//
//        database.farmDao().insert(FARM)
//        database.farmDao().nukeUserTable()
//        Assert.assertEquals(database.farmDao().getFarmCount(), 0)
//
//    }


    companion object {
        private val PRODUCT = ProductModel("sanoge")
        private val FARM = FarmModel("nora","farm01",20.0,20.0,1,"mama","address",0,0)
    }
}