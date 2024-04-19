package sheridan.eajdani.myapplication.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(entities = [UserEntity::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    companion object{
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE user_database_temp (id INTEGER PRIMARY KEY NOT NULL, name TEXT NOT NULL, password TEXT NOT NULL, privilegeLevel INTEGER NOT NULL, tasks TEXT)")

                // Drop the existing table
                database.execSQL("DROP TABLE user_database")

                // Rename the temporary table to the original table name
                database.execSQL("ALTER TABLE user_database_temp RENAME TO user_database")
            }
        }

        private var database: UserDatabase? = null

        fun getDatabase(context: Context): UserDatabase {
            return database ?: synchronized(this) {
                val instance = buildDatabase(context)
                database = instance
                instance
            }
        }
        private fun buildDatabase(context: Context): UserDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                UserDatabase::class.java,
                "user_database"
            ).addMigrations(MIGRATION_1_2)
                .build()
        }
    }
}