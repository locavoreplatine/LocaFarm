package locavoreplatine.locafarm.database

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import locavoreplatine.locafarm.database.dao.FarmDao
import locavoreplatine.locafarm.database.dao.FarmProductDao
import locavoreplatine.locafarm.database.dao.ProductDao
import locavoreplatine.locafarm.database.dao.UserDao
import locavoreplatine.locafarm.model.FarmModel
import locavoreplatine.locafarm.model.FarmProductMM
import locavoreplatine.locafarm.model.ProductModel
import locavoreplatine.locafarm.model.UserModel
import locavoreplatine.locafarm.util.PopulateDatabase
import locavoreplatine.locafarm.util.random
import org.jetbrains.anko.doAsync

@Database(entities = [(UserModel::class), (FarmModel::class), (ProductModel::class), (FarmProductMM::class)], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun farmDao(): FarmDao
    abstract fun productDao(): ProductDao
    abstract fun farmProductDao() : FarmProductDao

    companion object {

        var TEST_MODE = true
        private const val databaseName = "locafarm-database"

        private var db: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (db == null) {
                db = if (TEST_MODE) {
                    Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
                            .addCallback(object : Callback() {
                                override fun onCreate(db: SupportSQLiteDatabase) {
                                    super.onCreate(db)
                                    doAsync {
                                        getInstance(context).run {
                                            farmDao().insert(*PopulateDatabase.getSampleFarms())
                                            userDao().insert(*PopulateDatabase.getSampleUsers())
                                            productDao().insert(*PopulateDatabase.getSampleProduct())
                                            val farmList = farmDao().all().blockingGet()
                                            val productList = productDao().all().blockingGet()

                                            farmList.forEach {
                                                val randomVal1 = (0..(productList.size-1)).random()
                                                val randomVal2 = (0..(productList.size-1)).random()
                                                var tmpMM = FarmProductMM(it.farmId,productList[randomVal1].productId)
                                                farmProductDao().insert(tmpMM)
                                                if(randomVal1!=randomVal2){
                                                    tmpMM = FarmProductMM(it.farmId,productList[randomVal2].productId)
                                                    farmProductDao().insert(tmpMM)
                                                }
                                            }

                                        }
                                    }
                                }
                            })
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build()
                } else {
                    Room.databaseBuilder(context, AppDatabase::class.java, databaseName)
                            .addCallback(object : Callback() {
                                override fun onCreate(db: SupportSQLiteDatabase) {
                                    super.onCreate(db)
                                    doAsync {
                                        getInstance(context).run {
                                            farmDao().insert(*PopulateDatabase.getSampleFarms())
                                            userDao().insert(*PopulateDatabase.getSampleUsers())
                                        }
                                    }
                                }
                            })
                            .build()
                }
            }
            return db!!
        }

        private fun close() {
            db?.close()
        }

    }
}