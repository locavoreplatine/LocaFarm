package locavoreplatine.locafarm.database

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import locavoreplatine.locafarm.database.dao.*
import locavoreplatine.locafarm.model.*
import locavoreplatine.locafarm.util.PopulateDatabase
import locavoreplatine.locafarm.util.random
import org.jetbrains.anko.doAsync

@Database(entities = [(FarmModel::class), (ProductModel::class), (FarmProductMM::class),(Favorites::class)], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun farmDao(): FarmDao
    abstract fun productDao(): ProductDao
    abstract fun farmProductDao() : FarmProductDao
    abstract fun favoritesDao() : FavoritesDao

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
                                            productDao().insert(*PopulateDatabase.getSampleProduct())
                                            val farmList = farmDao().all().blockingGet()
                                            val productList = productDao().all().blockingGet()
                                            farmList.forEach {
                                                for (i in (0..10)){
                                                    val randomVal = (0..(productList.size-1)).random()
                                                    val tmpMM = FarmProductMM(it.farmId,productList[randomVal].productId)
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
