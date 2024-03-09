@file:OptIn(ExperimentalComposeUiApi::class)

package sheridan.eajdani.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.room.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import sheridan.eajdani.myapplication.Database.UserDatabase
import sheridan.eajdani.myapplication.Database.UserEntity
import sheridan.eajdani.myapplication.ui.theme.MyApplicationTheme


class MainActivity : ComponentActivity() {
    private val userDatabase by lazy { UserDatabase.getDatabase(this)}
    private val userDao by lazy {userDatabase.userDao()}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //val database by lazy { UserDatabase.getDatabase(this) }
       // println("${this.applicationContext}")
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    Screens(userDao)
//
                }
            }
        }
    }
//    private fun insertData(userName: String, password: String) {
//       if(userName == "admin") {
//           GlobalScope.launch(Dispatchers.IO) {
//               //userDao.deleteAll()
//               userDao.insert(UserEntity(name = userName, password = password, privilegeLevel = 1))
//
//           }
//       }else if (userName == "user"){
//           GlobalScope.launch(Dispatchers.IO) {
//              // userDao.deleteAll()
//               userDao.insert(UserEntity(name = userName, password = password, privilegeLevel = 0))
//
//           }
//       }
//    }
    @Composable
    fun DisplayData() {
        var dataList by remember { mutableStateOf(emptyList<UserEntity>()) }

        // Observe data changes from the database using a Flow
        LaunchedEffect(Unit) {
            dataList = userDao.getAll()
        }
        // Display data in your Compose UI
        Column {
            dataList.forEach {
                Text(it.name)
                Text(it.password)
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        TextBoxWithLabel("Username")
        }
    }
