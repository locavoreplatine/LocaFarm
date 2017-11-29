package locavoreplatine.locafarm.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import locavoreplatine.locafarm.model.FarmModel
import locavoreplatine.locafarm.model.UserModel

@Database(entities = arrayOf(UserModel::class,FarmModel::class), version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun farmDao() : FarmDao

    companion object {

        var TEST_MODE = false
        private val databaseName = "sensors-database"

        private var db: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (db == null) {
                if (TEST_MODE) {
                    db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).allowMainThreadQueries().build()
                } else {
                    db = Room.databaseBuilder(context, AppDatabase::class.java, databaseName).build()
                }
            }
            return db!!
        }

        private fun close() {
            db?.close()
        }

    }
}