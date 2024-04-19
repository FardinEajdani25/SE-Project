package sheridan.eajdani.myapplication.Database


import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "user_database")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val password: String,
    val privilegeLevel: Int,
    // privilegeLevel 0 = TaskManager, privilegeLevel 1 = Worker

    var tasks: MutableList<Task>?
)

data class Task(
    @PrimaryKey val taskId: Long,
    val taskName: String,
    var isCompleted: Boolean,
    var startTime: Date?,
    var endTime: Date?
)
