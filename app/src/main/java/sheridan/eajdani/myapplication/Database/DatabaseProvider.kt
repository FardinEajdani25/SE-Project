package sheridan.eajdani.myapplication.Database


import android.content.Context
import androidx.room.Room

object DatabaseProvider {
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
        ).build()
    }
}