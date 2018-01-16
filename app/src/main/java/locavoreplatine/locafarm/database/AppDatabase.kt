package locavoreplatine.locafarm.database

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import locavoreplatine.locafarm.model.FarmModel
import locavoreplatine.locafarm.model.UserModel
import locavoreplatine.locafarm.util.PopulateDatabase
import org.jetbrains.anko.doAsync

@Database(entities = [(UserModel::class), (FarmModel::class)], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun farmDao(): FarmDao

    companion object {

        var TEST_MODE = false
        private val databaseName = "sensors-database"

        private var db: AppDatabase? = null


        @Volatile private var INSTANCE: AppDatabase? = null


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
                                        }
                                    }
                                }
                            })
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