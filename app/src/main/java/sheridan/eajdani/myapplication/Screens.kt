package sheridan.eajdani.myapplication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import sheridan.eajdani.myapplication.Database.UserDao
import sheridan.eajdani.myapplication.Database.UserEntity

var userName = ""
var password = ""

@Composable
fun Screens(userDao: UserDao) {
    val navController = rememberNavController()


    NavHost(
        navController = navController,
        startDestination = "login_screen"
    ) {
        composable("login_screen") {
            LoginScreen(navController, userDao)
        }
        composable("user_screen") {
            UserScreen(navController, userDao)
        }
        composable("admin_screen") {
            AdminScreen(navController, userDao)
        }
    }
}

@Composable
fun LoginScreen(navController: NavController, userDao: UserDao) {
    // Your main screen content

    // Simulate a condition (you can replace this with your actual condition)

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Call the function to create a textbox with label

        Text("Task Management App",style = MaterialTheme.typography.body1.copy(
            fontSize = 40.sp // Change the font size to your desired value
        ), modifier= Modifier
            .padding(50.dp))


        Text("Sign in",style = MaterialTheme.typography.body1.copy(
            fontSize = 20.sp // Change the font size to your desired value
        ), modifier= Modifier
            .padding(20.dp))

        TextBoxWithLabel("Username")
        TextBoxWithLabel("Password")
        SubmitButton(userDao, navController)

    }

}

@Composable
fun UserScreen(navController: NavController, userDao: UserDao) {
    var userEntity  by remember { mutableStateOf(UserEntity(name = "", password = "", privilegeLevel = 1, tasks = emptyList())) }
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )    {
        Text("Welcome User",style = MaterialTheme.typography.body1.copy(
            fontSize = 20.sp // Change the font size to your desired value
        ), modifier= Modifier
            .padding(20.dp))

        LaunchedEffect(Unit){

          userEntity =  userDao.getUserByName("user")!!
        }
        Text("Assigned Tasks By Admin" , modifier = Modifier.padding(16.dp))
        userEntity.tasks!!.forEach {

            Text(text = it, modifier = Modifier.padding(16.dp))
        }
        Button(onClick = {

            navController.navigate("login_screen")

        }) {
            Text("Logout")
        }

    }
}

@OptIn(DelicateCoroutinesApi::class)
@Composable
fun AdminScreen(navController: NavController, userDao: UserDao) {
    var assignTasksVisibility by remember { mutableStateOf(0.0f) }
    var visibility by remember { mutableStateOf(1.0f) }
    var doDishes by remember { mutableStateOf(false) }
    var doGrocery by remember { mutableStateOf(false) }
    var doStudying by remember { mutableStateOf(false) }
    var showAssignableTask by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome Admin",style = MaterialTheme.typography.body1.copy(
            fontSize = 20.sp // Change the font size to your desired value
        ), modifier= Modifier
            .padding(20.dp))
        Button(onClick ={
            doDishes = false
            doGrocery = false
            doStudying = false

            showAssignableTask = true
            assignTasksVisibility = 1.0f
            visibility = 0.0f
        }, modifier = Modifier.alpha(visibility)){
            Text("Assign Task")

        }
        Button(onClick = {
            var tasks : MutableList<String> = mutableListOf("")
            tasks.clear()
            GlobalScope.launch(Dispatchers.IO) {

                val userEntity = userDao.getUserByName("user")
                if(userEntity!!.tasks!!.isNotEmpty()){

                    userEntity!!.tasks = tasks

                    userDao.updateEntity(userEntity)
                }

            }

        }, modifier = Modifier.alpha(visibility)) {
            Text("Clear Tasks")
        }
        Button(onClick = {

            navController.navigate("login_screen")

        }) {
            Text("Logout")
        }
        if(showAssignableTask){

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .alpha(assignTasksVisibility)
            ) {
                Text("Select Tasks to Assign", modifier = Modifier.padding(16.dp))
                Row() {
                Checkbox(
                    checked = doDishes,
                    onCheckedChange = { doDishes = it },
                    modifier = Modifier.padding(16.dp)
                )
                    Text("Do the Dishes", modifier = Modifier.padding(20.dp))

            }
                Row() {
                    Checkbox(
                        checked = doGrocery,
                        onCheckedChange = { doGrocery = it },
                        modifier = Modifier.padding(16.dp)
                    )
                    Text("Buy Grocery", modifier = Modifier.padding(20.dp))

                }
                Row() {
                    Checkbox(
                        checked = doStudying,
                        onCheckedChange = { doStudying = it },
                        modifier = Modifier.padding(16.dp)
                    )
                    Text("Study", modifier = Modifier.padding(20.dp))

                }
                Button(onClick = {
                    val tasks : MutableList<String> = mutableListOf("")
                    tasks.clear()
                    visibility = 1.0f
                    assignTasksVisibility = 0.0f

                    if(doDishes){

                        tasks.add("Do the Dishes")
                    }
                    if(doGrocery){

                        tasks.add("Do Grocery")
                    }
                    if(doStudying){

                        tasks.add("Do Studying")
                    }
                    GlobalScope.launch(Dispatchers.IO) {

                     var userEntity =  userDao.getUserByName("user")

                        userEntity!!.tasks = tasks

                        userDao.updateEntity(userEntity)

                    }


                }) {


                    Text("Assign Checked Tasks")
                }
            }

        }
    }
}
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TextBoxWithLabel(dataName: String) {
    var textValue by remember { mutableStateOf("") }

    // Get the keyboard controller
    val keyboardController = LocalSoftwareKeyboardController.current

    // Create a textbox with a label
    TextField(
        value = textValue,
        onValueChange = {
            textValue = it

        },
        label = { Text("Enter your $dataName") },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        ),
        textStyle = LocalTextStyle.current.copy(color = Color.Black),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        singleLine = true,
        keyboardActions = KeyboardActions(
            onDone = {
                // Handle the action when the user presses "Done" on the keyboard
                keyboardController?.hide()
                // Print the text value to the console (you can replace this with your logic)
                println("Entered Text: $textValue")
                if(dataName == "Username") {

                    userName = textValue
                    println("Username :  $userName")
                }else{

                    password = textValue
                    println("Password :  $password")
                }
            }
        )
    )
}
@OptIn(DelicateCoroutinesApi::class)
@Composable
fun SubmitButton(userDao: UserDao, navController: NavController) {
    var visibility  by remember { mutableStateOf(0.0f) }
    var _isUser by remember { mutableStateOf(false) }
    var _isAdmin  by remember { mutableStateOf(false) }
    var databaseUsers by remember { mutableStateOf(emptyList<UserEntity>())}
        var isButtonClicked by remember { mutableStateOf(false) }
    Column {
        Button(onClick = {
            visibility = 0.0f
            _isUser = false
            _isAdmin = false
            println("Button clicked!")
            GlobalScope.launch(Dispatchers.IO) {
                databaseUsers = userDao.getAll()

            }

            databaseUsers.forEach {

                if(userName == it.name && password == it.password){
                    if(userName == "admin") {
                        _isAdmin = true
                    }else{
                        _isUser = true
                    }
                }

            }
            if(_isUser == false && _isAdmin == false){
                    visibility = 1.0f

            }else if(_isUser == true){

                navController.navigate("user_screen")

            }else{

                navController.navigate("admin_screen")

            }
        }) {
            Text("Submit")
        }
        Text("INVALID USERNAME OR PASSWORD", color = Color.Red, modifier = Modifier.alpha(visibility))

    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {

   // App()
}