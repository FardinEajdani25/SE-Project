package sheridan.eajdani.myapplication.Database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: UserEntity)

    @Query("SELECT * FROM user_database")
    suspend fun getAll(): List<UserEntity>

    @Query("DELETE FROM user_database")
    suspend fun deleteAll()

    @Query("SELECT * FROM user_database WHERE name = :userName LIMIT 1")
    suspend fun getUserByName(userName: String): UserEntity?

    @Update
    suspend fun updateEntity(entity: UserEntity)
}