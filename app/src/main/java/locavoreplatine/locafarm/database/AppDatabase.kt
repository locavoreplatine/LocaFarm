package locavoreplatine.locafarm.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import locavoreplatine.locafarm.model.UserModel

@Database(entities = arrayOf(UserModel::class), version = 1, exportSchema = true)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun UserModel(): UserDao

    companion object {

        var TEST_MODE = false
        private val databaseName = "sensors-database"

        private var db: AppDatabase? = null
        private var dbInstance: UserDao? = null

        fun getInstance(context: Context): UserDao {
            if (dbInstance == null) {
                if (TEST_MODE) {
                    db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).allowMainThreadQueries().build()
                    dbInstance = db?.UserModel()
                } else {
                    db = Room.databaseBuilder(context, AppDatabase::class.java, databaseName).build()
                    dbInstance = db?.UserModel()
                }
            }
            return dbInstance!!
        }

        private fun close() {
            db?.close()
        }

    }
}