package locavoreplatine.locafarm

import locavoreplatine.locafarm.database.AppDatabase
import locavoreplatine.locafarm.model.FarmModel
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.junit.After
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Before


/**
 * Created by sparow on 2/19/2018.
 */

@RunWith(AndroidJUnit4::class)
class FarmDaoTest {

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
        // When inserting a new user in the data source
        database.farmDao().insert(FARM)

        // When subscribing to the emissions of the user
        database.farmDao().getFarmById(FARM.farmId)
                .test()
                // assertValue asserts that there was only one emission of the user
                .assertValue {
                    println(it.toString())
                    it.farmId == FARM.farmId && it.name == FARM.name
                }

        Assert.assertEquals(database.farmDao().findFarmByName(FARM.name).blockingFirst().indexOf(FARM), 0)
        Assert.assertEquals(database.farmDao().findFarmByName("Le").blockingFirst().first().name, "Le drive fermier de Lomme")
    }

    @Test fun updateAndGetFarmTest() {
        // Given that we have a user in the data source
        database.farmDao().insert(FARM)

        // When we are updating the name of the user
        val updatedFarm= FarmModel("mosmos","farm01",20.0,20.0,1,"mama","address",0,0,1)
        database.farmDao().update(updatedFarm)

        // When subscribing to the emissions of the user
        database.farmDao().getFarmById(FARM.farmId)
                .test()
                // assertValue asserts that there was only one emission of the user
                .assertValue { it.farmId == FARM.farmId && it.name == "mosmos" }

        Assert.assertEquals(database.farmDao().findFarmByName(updatedFarm.name).blockingFirst().indexOf(updatedFarm), 0)
    }

    @Test fun deleteAndGetFarmByIdTest() {
        // Given that we have a user in the data source
        database.farmDao().insert(FARM)

        //When we are deleting all users
        database.farmDao().delete(FARM)
        // When subscribing to the emissions of the user
        database.farmDao().getFarmById(FARM.farmId)
                .test()
                // check that there's no user emitted
                .assertNoValues()

        database.farmDao().findFarmByName(FARM.name)
                .test()
                // check that there's no user emitted
                .assertNoValues()
    }


    @Test fun insertAndGetFarmCountTest() {
        Assert.assertEquals(database.farmDao().getFarmCount(), 3)
        // Given that we have a user in the data source
        database.farmDao().insert(FARM)
        // When subscribing to the emissions of the user
        Assert.assertEquals(database.farmDao().getFarmCount(), 4)
    }


    @Test fun getAllFarmTest() {

        // Given that we have a user in the data source
        database.farmDao().insert(FARM)

        // When subscribing to the emissions of the user
        database.farmDao().all()
                .test()
                .assertValue {
                    val tmp = it.first()
                    tmp.farmId == FARM.farmId && tmp.name == FARM.name
                }

        database.farmDao().all()
                .test()
                .assertNoErrors()

    }

    @Suppress("DEPRECATION")
    @Test fun nukFarmsTest() {

        // Given that we have a user in the data source
        database.farmDao().insert(FARM)

        // When subscribing to the emissions of the user
        database.farmDao().nukeUserTable()

        Assert.assertEquals(database.farmDao().getFarmCount(), 0)

    }

    companion object {
        private val FARM = FarmModel("nora","farm01",20.0,20.0,1,"mama","address",0,0,1)
    }
}