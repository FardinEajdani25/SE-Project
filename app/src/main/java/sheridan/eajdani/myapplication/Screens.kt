package sheridan.eajdani.myapplication

import androidx.compose.animation.core.animateFloatAsState
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
import sheridan.eajdani.myapplication.Database.Task
import sheridan.eajdani.myapplication.Database.UserDao
import sheridan.eajdani.myapplication.Database.UserEntity
import java.util.Date

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
        composable("track_screen") {
            TrackingScreen(navController, userDao)
        }
    }
}
fun TotalTime(date1 : Date, date2: Date ) : Long{

    val time1 = date1.time
    val time2 = date2.time

    val differenceInMillis = time2 - time1

    val differenceInSeconds = differenceInMillis / 1000

    return differenceInSeconds

}

@Composable
fun TrackingScreen(navController: NavController, userDao: UserDao){
    var userEntity  by remember { mutableStateOf(UserEntity(name = "", password = "", privilegeLevel = 1, tasks = mutableListOf(Task(taskId = 0, taskName = "", isCompleted = false, startTime = null, endTime = null)))) }
    var isTask1done by remember { mutableStateOf(false) }
    var isTask2done by remember { mutableStateOf(false) }
    var isTask3done by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        LaunchedEffect(Unit){

            userEntity =  userDao.getUserByName("user")!!


            }
            userEntity.tasks!!.forEach { task ->
                if (!task.isCompleted) {
                    if (task.startTime != null) {
                        Text(text = "${task.taskName} : In Progress" )
                    } else {

                        Text(text = "${task.taskName} : Not Started")
                    }
                }
                else{

                    Text(text = "${task.taskName}: Task completed at ${task.endTime}")
                    Text(text = "Total Time : ${TotalTime(task.startTime!!, task.endTime!!)} seconds")
                }
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
    var userEntity  by remember { mutableStateOf(UserEntity(name = "", password = "", privilegeLevel = 1, tasks = null)) }
    var visibilityList4 by remember { mutableStateOf(ArrayList<Float>()) }
    visibilityList4.add(1.0f)
    visibilityList4.add(1.0f)
    var visibilityList1 by remember { mutableStateOf(ArrayList<Float>()) }
    visibilityList1.add(1.0f)
    visibilityList1.add(1.0f)
    var visibilityList2 by remember { mutableStateOf(ArrayList<Float>()) }
    visibilityList2.add(0.0f)
    visibilityList2.add(0.0f)
    var visibility3 by remember { mutableStateOf(0.0f) }
    var visibility5 by remember { mutableStateOf(0.0f) }

    var isTaskStarted by remember { mutableStateOf(false) }
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
        if(userEntity.tasks == null){}
        else {
            userEntity.tasks!!.forEach { task ->
                if (task.isCompleted == false) {


                    Row(modifier = Modifier.alpha(visibilityList4[task.taskId.toInt()])) {

                        Text(text = task.taskName, modifier = Modifier.padding(16.dp))
                        if(task.startTime == null){
                        Button(onClick = {

                            visibilityList1[task.taskId.toInt()] = 0.0f
                            visibilityList2[task.taskId.toInt()] = 1.0f
                            visibility3 = 0.0f
                            visibility5 = 1.0f
                            task.startTime = Date()
                            GlobalScope.launch(Dispatchers.IO) {

                                userDao.updateEntity(userEntity)

                            }

                        }, modifier = Modifier.alpha(visibilityList1[task.taskId.toInt()])) {
                            Text(text = "Start Task")
                             }
                        } else{
                            visibilityList1[task.taskId.toInt()] = 0.0f
                            visibilityList2[task.taskId.toInt()] = 1.0f
                            visibility3 = 0.0f
                            visibility5 = 1.0f
                        }
                        Button(onClick = {

                            visibility3 = 1.0f
                            visibility5 = 0.0f
                            visibilityList4[task.taskId.toInt()] = 0.0f
                            task.endTime = Date()
                            task.isCompleted = true
                            GlobalScope.launch(Dispatchers.IO) {

                                userDao.updateEntity(userEntity)

                            }

                        }, modifier = Modifier.alpha(visibilityList2[task.taskId.toInt()])) {
                            Text(text = "End Task")

                        }


                    }
                }

            }
        }
        Text(text = "Task Started", modifier = Modifier.alpha( visibility5) )
        Text(text = "Task Completed", modifier = Modifier.alpha( visibility3) )
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
    var isTask1Assigned by remember { mutableStateOf(false) }
    var isTask2Assigned by remember { mutableStateOf(false) }
    var isTask3Assigned by remember { mutableStateOf(false) }
    var doDishes by remember { mutableStateOf(false) }
    var doGrocery by remember { mutableStateOf(false) }
    var doStudying by remember { mutableStateOf(false) }
    var showAssignableTask by remember { mutableStateOf(false) }
    var user by remember { mutableStateOf(UserEntity(
        name = "user",
        password = "user",
        privilegeLevel = 1,
        tasks = null
    )) }
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
        Button(onClick = {

            navController.navigate("track_screen")
        }){ Text(text = "Track Activity")}
        Button(onClick ={
            doDishes = false
            doGrocery = false
            doStudying = false
            GlobalScope.launch(Dispatchers.IO) {

                user = userDao.getUserByName("user")!!

                user.tasks!!.forEach { task ->

                    if (task.taskId.toInt() == 0) {

                        isTask1Assigned = true
                        if (task.isCompleted == true) {
                            isTask1Assigned = false
                        }
                    }
                    if (task.taskId.toInt() == 1) {

                        isTask2Assigned = true
                        if (task.isCompleted == true) {
                            isTask2Assigned = false
                        }

                    }
                    if (task.taskId.toInt() == 2) {

                        isTask3Assigned = true
                        if (task.isCompleted == true) {
                            isTask3Assigned = false
                        }
                    }
                }

            }
            showAssignableTask = true
            assignTasksVisibility = 1.0f
            visibility = 0.0f
        }, modifier = Modifier.alpha(visibility)){
            Text("Assign Task")

        }
        Button(onClick = {
            var tasks : MutableList<Task> = mutableListOf(Task(taskId = 0, taskName = "", isCompleted = false, startTime = null, endTime = null))
            tasks.clear()
            GlobalScope.launch(Dispatchers.IO) {

                user = userDao.getUserByName("user")!!
                if(user.tasks!!.isNotEmpty()){

                    user.tasks = tasks

                    userDao.updateEntity(user)
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
                if(!isTask1Assigned) {
                    Row() {
                        Checkbox(
                            checked = doDishes,
                            onCheckedChange = { doDishes = it },
                            modifier = Modifier.padding(16.dp)
                        )
                        Text("Do the Dishes", modifier = Modifier.padding(20.dp))

                    }
                }
                if(!isTask2Assigned) {
                    Row() {
                        Checkbox(
                            checked = doGrocery,
                            onCheckedChange = { doGrocery = it },
                            modifier = Modifier.padding(16.dp)
                        )
                        Text("Buy Grocery", modifier = Modifier.padding(20.dp))

                    }
                }
                if(!isTask3Assigned) {
                    Row() {
                        Checkbox(
                            checked = doStudying,
                            onCheckedChange = { doStudying = it },
                            modifier = Modifier.padding(16.dp)
                        )
                        Text("Study", modifier = Modifier.padding(20.dp))

                    }
                }
                Button(onClick = {
                    GlobalScope.launch(Dispatchers.IO) {


                        var tasks : MutableList<Task> = mutableListOf(Task(taskId = 0, taskName = "", isCompleted = false, startTime = null, endTime = null))
                        tasks.clear()
                        visibility = 1.0f
                        assignTasksVisibility = 0.0f

                        if(doDishes){

                            tasks.add(Task(taskId = 0, taskName = "Do the Dishes", isCompleted = false, startTime = null, endTime = null))
                        }
                        if(doGrocery){

                            tasks.add(Task(taskId = 1, taskName = "Do Groceries", isCompleted = false, startTime = null, endTime = null))
                        }
                        if(doStudying){

                            tasks.add(Task(taskId = 2, taskName = "Do Studying", isCompleted = false, startTime = null, endTime = null))
                        }
                         //user = userDao.getUserByName("user")!!
                        var taskfound = false
                        tasks.forEach { task ->
                            taskfound = false
                            if(user.tasks!!.size != 0) {
                                user.tasks!!.forEach { userTask ->
                                    if(userTask.taskId == task.taskId){
                                        userTask.isCompleted = false
                                        userTask.startTime = null
                                        userTask.endTime = null
                                        taskfound = true
                                    }
                                }
                            }
                            if(taskfound == false) {
                                user.tasks!!.add(task)
                            }
                        }
                            userDao.updateEntity(user)
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
    val user = UserEntity(
        name = "user",
        password = "user",
        privilegeLevel = 1,
        tasks = null
    )

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