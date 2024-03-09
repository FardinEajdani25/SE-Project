package sheridan.eajdani.myapplication.Database


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_database")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val password: String,
    val privilegeLevel: Int,
    var tasks: List<String>?
    // privilegeLevel 0 = TaskManager, privilegeLevel 1 = Worker
)