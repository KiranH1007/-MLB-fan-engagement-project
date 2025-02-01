//package com.example.fanengagementapp
//
//import android.annotation.SuppressLint
//import android.content.Context
//import android.content.Intent
//import android.os.Bundle
//import android.widget.Toast
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.horizontalScroll
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.AddCircle
//import androidx.compose.material.icons.filled.MoreVert
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.geometry.Offset
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.platform.LocalConfiguration
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.platform.LocalSoftwareKeyboardController
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontFamily
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.PasswordVisualTransformation
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.*
//import coil3.compose.AsyncImage
//import com.example.fanengagementapp.ui.theme.FanengagementappTheme
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
//import com.google.firebase.auth.FirebaseAuthUserCollisionException
//import com.google.firebase.auth.FirebaseAuthWeakPasswordException
//
//class MainActivity : ComponentActivity() {
//
//    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        //Firebase.initializeApp(this) // Initialize Firebase
//        setContent {
//            FanengagementappTheme {
//                Scaffold {
//                    Surface(
//                        modifier = Modifier.fillMaxSize(),
//                        color = MaterialTheme.colorScheme.background
//                    ) {
//                        Navigation()
//                    }
//                }
//            }
//        }
//    }
//
//}
//
//@Composable
//fun Navigation() {
//    val navController = rememberNavController()
//    NavHost(navController = navController, startDestination = "login_screen") {
//        composable("login_screen") { LoginScreen(navController) }
//        composable("signup_screen") { SignupScreen(navController) }
//        composable("user_profile_screen") { UserProfileScreen(navController) }
//        composable("UserProfileIcon") { UserProfileIcon(navController) }
//        composable("user_profile_navigation_screen") { UserProfileNavigationScreen(navController) }
//    }
//}
//
//@Composable
//fun LoginScreen(navController: NavHostController) {
//    var usernameOrEmail by remember { mutableStateOf("narikhire1007@gmail.com") }
//    var password by remember { mutableStateOf("Narik@1") }
//    val keyboardController = LocalSoftwareKeyboardController.current
//    val context = LocalContext.current
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        Text(text = "MLB Fan Engagement", color = Color.Black, style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
//        Spacer(modifier = Modifier.height(16.dp))
//
//        TextField(
//            value = usernameOrEmail,
//            onValueChange = { usernameOrEmail = it },
//            label = { Text("Username/Email") },
//            modifier = Modifier.fillMaxWidth(),
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//
//        TextField(
//            value = password,
//            onValueChange = { password = it },
//            label = { Text("Password") },
//            modifier = Modifier.fillMaxWidth(),
//            visualTransformation = PasswordVisualTransformation()
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Button(onClick = {
//            val auth = FirebaseAuth.getInstance()
//            auth.signInWithEmailAndPassword(usernameOrEmail, password)
//                .addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        navController.navigate("user_profile_screen")
//                        //navController.navigate("posts_screen")
//
//                        Toast.makeText(context, "Login Successful!", Toast.LENGTH_SHORT).show()
//                    } else {
//                        handleFirebaseException(task.exception, context)
//                    }
//                }
//            keyboardController?.hide()
//        }) {
//            Text("Login")
//        }
//        Spacer(modifier = Modifier.height(16.dp))
//
//        TextButton(onClick = { navController.navigate("signup_screen") }) {
//            Text("Don't have an account? Sign Up")
//        }
//    }
//}
//
//@Composable
//fun SignupScreen(navController: NavHostController) {
//    var username by remember { mutableStateOf("") }
//    var email by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//    val keyboardController = LocalSoftwareKeyboardController.current
//    val context = LocalContext.current
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        Text(text = "Sign Up", style = MaterialTheme.typography.headlineMedium)
//        Spacer(modifier = Modifier.height(16.dp))
//
//        TextField(
//            value = username,
//            onValueChange = { username = it },
//            label = { Text("Username") },
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//
//        TextField(
//            value = email,
//            onValueChange = { email = it },
//            label = { Text("Email") },
//            modifier = Modifier.fillMaxWidth(),
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//
//        TextField(
//            value = password,
//            onValueChange = { password = it },
//            label = { Text("Password") },
//            modifier = Modifier.fillMaxWidth(),
//            visualTransformation = PasswordVisualTransformation()
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Button(onClick = {
//            val auth = FirebaseAuth.getInstance()
//            auth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        // Sign-up successful
//                        navController.navigate("user_profile_screen")
//                        //navController.navigate("posts_screen")
//                        Toast.makeText(context, "Login Successful!", Toast.LENGTH_SHORT).show()
//
//                    } else {
//                        handleFirebaseException(task.exception, context)
//                    }
//                }
//            keyboardController?.hide()
//        }) {
//            Text("Sign Up")
//        }
//        Spacer(modifier = Modifier.height(16.dp))
//
//        TextButton(onClick = { navController.navigate("login_screen") }) {
//            Text("Already have an account? Login")
//        }
//    }
//
//}
//
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun UserProfileScreen(navController: NavHostController) {
//    Scaffold(
//        topBar = { HomeAppBar() },
//        bottomBar = { BottomNavigationBar() })
//    { paddingValues ->
//
//        Column(
//            modifier = Modifier
//                .padding(paddingValues)
//                .fillMaxSize()
//                .verticalScroll(rememberScrollState())
//        ) {
//            Column {
//
//                //Story
//                Row(
//                    modifier = Modifier
//                        .padding(
//                            top = 16.dp,
//                            bottom = 16.dp,
//                            start = 0.dp,
//                            end = 0.dp
//                        )
//                        .horizontalScroll(rememberScrollState()),
//                    horizontalArrangement = Arrangement.spacedBy(16.dp)
//                ) {
//                    YourStory(
//                        imageUrl = "https://unsplash.com/photos/a-bunch-of-baseballs-that-are-white-and-red-2A6FVXOiJ1w",
//                        name = "baseball"
//                    )
//                    Story(
//                        imageUrl = "https://images.unsplash.com/photos/man-pitching-the-baseball-1duhgFAikoA",
//                        name = "player1"
//                    )
//                    Story(
//                        imageUrl = "https://unsplash.com/photos/selective-focus-photography-of-person-holding-baseball-bat-UVxd5b-_tw8",
//                        name = "player2"
//                    )
//                    Story(
//                        imageUrl = "https://unsplash.com/photos/a-baseball-player-sliding-into-a-base-during-a-game-fNkFsG6zjMs",
//                        name = "player3"
//                    )
//
//
//                }
//
//                HomeMainContent()
//
//
//            }
//
//
//        }
//
//    }
//}
//
//@Composable
//fun HomeMainContent(){
//    // Create a surface with padding on the top and bottom
//    Surface(modifier = Modifier.padding(top=6.dp, bottom = 6.dp)) {
//        // Create a column to hold the content
//        Column {
//            // Create a row to hold the profile picture, username, and more options icon
//            Row(
//                Modifier
//                    .fillMaxWidth() // Fill the width of the parent
//                    .padding(start = 6.dp, end = 6.dp, bottom = 8.dp), // Add padding around the row
//                verticalAlignment = Alignment.CenterVertically // Center the children vertically
//            ) {
//                // Create a column to hold the profile picture
//                Column(
//                    Modifier
//                        .weight(1f) // Take up 1/8 of the row's width
//                        .padding(end = 16.dp) // Add padding to the end of the column
//                ) {
//                    // Load the profile picture asynchronously
//                    AsyncImage(
//                        model = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8UHJvZmlsZSUyMHBpY3R1cmV8ZW58MHx8MHx8fDA%3D",
//                        contentDescription = null, // No content
//                        modifier = Modifier
//                            .size(30.dp) // Set the size of the image to 30dp
//                            .clip(CircleShape) // Clip the image to a circle shape
//                            .border( // Add a border to the image
//                                width = 2.dp, // The width of the border is 2dp
//                                brush = Brush.linearGradient( // The color of the border is a linear gradient
//                                    colors = listOf(Color.Yellow, Color.Red), // The colors of the gradient
//                                    start = Offset(0f, 0f), // The start point of the gradient
//                                    end = Offset(70f, 70f) // The end point of the gradient
//                                ),
//                                shape = CircleShape // The shape of the border is a circle
//                            )
//                            .clickable(onClick = {}), // Make the image clickable
//                        contentScale = ContentScale.Crop // Crop the image to fill the size of the ImageView
//                    )
//                }
//
//                // Create a column to hold the username and audio source
//                Column(
//                    Modifier
//                        .weight(6f) // Take up 6/8 of the row's width
//                        .padding(end = 16.dp) // Add padding to the end of the column
//                ) {
//                    // Display the username
//                    Text(
//                        text = "narik", // The username
//                        fontWeight = FontWeight.Normal, // The font weight is normal
//                        fontSize = 11.sp // The font size is 11sp
//                    )
//                    // Display the audio source
//                    Text(
//                        text = "narik  •  Original audio", // The audio source
//                        fontWeight = FontWeight.Normal, // The font weight is normal
//                        fontSize = 10.sp // The font size is 10sp
//                    )
//                }
//                // Create a box to hold the more options icon
//                Box(
//                    modifier = Modifier.weight(1f), // Take up 1/8 of the row's width
//                    contentAlignment = Alignment.CenterEnd // Center the icon at the end of the box
//                ) {
//                    // Display the more options icon
//                    Icon(Icons.Default.MoreVert,
//                        contentDescription = null, // No content description is provided
//                        modifier = Modifier.size(26.dp)) // Set the size of the icon to 26dp
//                }
//            }
//
//            //Image / Video
//            Row(
//                modifier = Modifier
//                    .horizontalScroll(rememberScrollState()) // Enable horizontal scrolling
//                    .fillMaxWidth(), // Fill the width of the parent
//
//            ) {
//                // Replace with your list of image URLs
//                val images = listOf(
//                    "https://unsplash.com/photos/a-bunch-of-baseballs-that-are-white-and-red-2A6FVXOiJ1w",
//                    "https://unsplash.com/photos/a-bunch-of-baseballs-that-are-white-and-red-2A6FVXOiJ1w",
//                    "https://unsplash.com/photos/a-bunch-of-baseballs-that-are-white-and-red-2A6FVXOiJ1w"
//                )
//
//                // Call the ImageScrollWithTextOverlay function with the list of images
//                ImageScrollWithTextOverlay(images)
//            }
//
//            // Content (Reactions)
//            Row(
//                modifier = Modifier.fillMaxWidth(), // Fill the width of the parent
//                horizontalArrangement = Arrangement.SpaceBetween // Arrange the children with space between them
//            ) {
//                // Create a row to hold the reaction icons
//                Row {
//                    // Create an icon button for likes
//                    IconButton(onClick = {  }) {
//                        Icon(
//                            painter = painterResource(R.drawable.heart), // Use the heart icon resource
//                            contentDescription = "Like Icon", // Provide a description for accessibility
//                            modifier = Modifier.size(20.dp) // Set the size of the icon to 20dp
//                        )
//                    }
//
//                    // Create an icon button for comments
//                    IconButton(onClick = {  }) {
//                        Icon(
//                            painter = painterResource(R.drawable.comment), // Use the comment icon resource
//                            contentDescription = "Comment Icon", // Provide a description for accessibility
//                            modifier = Modifier.size(20.dp) // Set the size of the icon to 20dp
//                        )
//                    }
//
//                    // Create an icon button for share
//                    IconButton(onClick = {  }) {
//                        Icon(
//                            painter = painterResource(R.drawable.share), // Use the share icon resource
//                            contentDescription = "Share Icon", // Provide a description for accessibility
//                            modifier = Modifier.size(20.dp) // Set the size of the icon to 20dp
//                        )
//                    }
//                }
//
//                // Create an icon button for save
//                IconButton(onClick = {  }) {
//                    Icon(
//                        painter = painterResource(R.drawable.save_icon), // Use the save icon resource
//                        contentDescription = "Save Icon", // Provide a description for accessibility
//                        modifier = Modifier.size(20.dp) // Set the size of the icon to 20dp
//                    )
//                }
//            }
//
//// Create a column to hold the likes, comments, and timestamp
//            Column(modifier = Modifier.padding(
//                start = 10.dp,
//                end = 10.dp,
//                top = 1.dp,
//                bottom =4.dp
//            )) {
//                // Display the number of likes
//                Row{
//                    Text(
//                        text = "2993 likes",
//                        fontWeight = FontWeight.Bold, // The font weight is bold
//                        fontSize = 15.sp // The font size is 15sp
//                    )
//                }
//
//                // Display the comments
//                Row{
//                    Text(
//                        text = "narik ", // The username
//                        fontWeight = FontWeight.Bold, // The font weight is bold
//                        fontSize = 15.sp // The font size is 15sp
//                    )
//                    Text(
//                        text = "Discover new people", // The comment
//                        fontWeight = FontWeight.Normal, // The font weight is normal
//                        fontSize = 13.sp // The font size is 13sp
//                    )
//                }
//
//                // Display the total number of comments
//                Row{
//                    Text(text = "view all 138 comments",
//                        fontWeight = FontWeight.Thin, // The font weight is thin
//                        fontSize = 13.sp // The font size is 13sp
//                    )
//                }
//
//                // Display the timestamp
//                Row{
//                    Text(text = "9 minutes ago",
//                        fontWeight = FontWeight.Thin, // The font weight is thin
//                        fontSize = 13.sp // The font size is 13sp
//                    )
//                }
//            }
//
//
//
//        }
//    }
//}
//
//@Composable
//fun UserProfileNavigationScreen(navController: NavHostController) {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(text = "Profile", style = MaterialTheme.typography.headlineMedium)
//    }
//        // User Profile Icon (already implemented)
//}
//
//@Composable
//fun UserProfileIcon(navController: NavHostController) {
//    IconButton(onClick = {
//        navController.navigate("user_profile_screen")
//    }) {
//        Icon(
//            painter = painterResource(id = R.drawable.person), // Replace with actual image
//            contentDescription = "User Profile"
//        )
//    }
//}
//
//@Composable
//fun ImageScrollWithTextOverlay(images: List<String>) {
//    // Get the screen width from the local configuration
//    val screenWidth = LocalConfiguration.current.screenWidthDp
//
//    // Loop over each image in the list
//    images.forEachIndexed { index, imageUrl ->
//        // Create a box for each image
//        Box(
//            modifier = Modifier
//                .width(screenWidth.dp) // Set the width of the box to the screen width
//                .aspectRatio(1f) // Set the aspect ratio of the box to 1 (for a square box)
//        ) {
//            // Load the image asynchronously
//AsyncImage(
//    model = imageUrl, // The URL of the image
//    contentDescription = null, // No content description is provided
//    modifier = Modifier.fillMaxWidth(), // Fill the width of the parent
//    contentScale = ContentScale.FillWidth // Scale the image to fill the width of the ImageView
//)
//
//            // Display the image index and total number of images
//            Text(
//                text = "${index + 1}/${images.size}", // The text to display
//                color = Color.White, // The color of the text is white
//                modifier = Modifier
//                    .align(Alignment.TopEnd) // Align the text to the top end of the box
//                    .padding(4.dp) // Add padding around the text
//            )
//        }
//    }
//}
//
//// Opt-in to use experimental Material 3 API
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun HomeAppBar(){
//    // Create a top app bar
//    TopAppBar(
//        // Set the title of the app bar
//        title = {
//            Text(
//                "MLB Fan Engagement", // The text to display
//                fontFamily = FontFamily.Cursive, // Use the custom font family "lobster"
//                fontWeight = FontWeight.Bold, // Set the font weight to bold
//                fontSize = 25.sp, // Set the font size to 25sp
//            )
//        },
//        // Define the actions to include in the app bar
//        actions = {
//            // Create an icon button with a heart icon
//            IconButton(onClick = {  }) {
//                Icon(
//                    painter = painterResource(R.drawable.heart), // Use the heart icon resource
//                    contentDescription = "Heart Icon", // Provide a description for accessibility
//                    modifier = Modifier.size(24.dp) // Set the size of the icon to 24dp
//                )
//            }
//            // Create an icon button with a messenger icon
//            IconButton(onClick = {  }) {
//                Icon(
//                    painter = painterResource(R.drawable.chat), // Use the messenger icon resource
//                    contentDescription = "Messenger Icon", // Provide a description for accessibility
//                    modifier = Modifier.size(24.dp) // Set the size of the icon to 24dp
//                )
//            }
//        }
//    )
//}
//
//// Your Story Section
//@Composable
//fun YourStory(imageUrl: String, name: String) {
//    // Create a column to hold the story image and name
//    Column(
//        horizontalAlignment = Alignment.CenterHorizontally, // Center the children horizontally
//        verticalArrangement = Arrangement.spacedBy(8.dp) // Space the children vertically by 8dp
//    ) {
//        // Create a box to hold the story image and the add icon
//        Box(
//            modifier = Modifier
//                .size(70.dp) // Set the size of the box to 70dp
//                .clickable(onClick = {}) // Make the box clickable
//        ) {
//            // Load the story image asynchronously
//            AsyncImage(
//                model = imageUrl, // The URL of the image
//                contentDescription = null, // No content description is provided
//                modifier = Modifier.clip(CircleShape), // Clip the image to a circle shape
//                contentScale = ContentScale.Crop // Crop the image to fill the size of the ImageView
//            )
//            // Display the add icon
//            Icon(
//                Icons.Default.AddCircle, // The add icon
//                contentDescription = null, // No content description is provided
//                modifier = Modifier
//                    .align(Alignment.BottomEnd), // Align the icon to the bottom end of the box
//                tint = Color(0xFF2196F3) // Set the color of the icon to blue
//            )
//        }
//        // Display the name of the story
//        Text(
//            text = name, // The name of the story
//            fontWeight = FontWeight.Normal, // The font weight is normal
//            fontSize = 13.sp // The font size is 13sp
//        )
//    }
//}
//
//// Other Story Section
//@Composable
//fun Story(imageUrl: String, name: String) {
//    // Create a column to hold the story image and name
//    Column(
//        horizontalAlignment = Alignment.CenterHorizontally, // Center the children horizontally
//        verticalArrangement = Arrangement.spacedBy(8.dp) // Space the children vertically by 8dp
//    ) {
//        // Load the story image asynchronously
//        AsyncImage(
//            model = imageUrl, // The URL of the image
//            contentDescription = null, // No content description is provided
//            modifier = Modifier
//                .size(70.dp) // Set the size of the image to 70dp
//                .clip(CircleShape) // Clip the image to a circle shape
//                .border( // Add a border to the image
//                    width = 2.dp, // The width of the border is 2dp
//                    brush = Brush.linearGradient( // The color of the border is a linear gradient
//                        colors = listOf(Color.Yellow, Color.Red), // The colors of the gradient
//                        start = Offset(0f, 0f), // The start point of the gradient
//                        end = Offset(70f, 70f) // The end point of the gradient
//                    ),
//                    shape = CircleShape // The shape of the border is a circle
//                )
//                .clickable(onClick = {}), // Make the image clickable
//            contentScale = ContentScale.Crop // Crop the image to fill the size of the ImageView
//        )
//        // Display the name of the story
//        Text(
//            text = name, // The name of the story
//            fontWeight = FontWeight.Normal, // The font weight is normal
//            fontSize = 13.sp // The font size is 13sp
//        )
//    }
//}
//
//@Composable
//fun BottomNavigationBar() {
//    // Create a bottom app bar
//    BottomAppBar {
//        // Create a row to hold the icons
//        Row(
//            modifier = Modifier.fillMaxWidth(), // Fill the width of the parent
//            horizontalArrangement = Arrangement.SpaceEvenly // Arrange the icons evenly
//        ) {
//            // Create an icon button for home
//            IconButton(onClick = { /* do something */ }) {
//                Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                    Icon(
//                        painter = painterResource(R.drawable.home),
//                        contentDescription = "Home Icon",
//                        modifier = Modifier.size(25.dp)
//                    )
//                }
//            }
//            // Create an icon button for search
//            IconButton(onClick = { /* do something */ }) {
//                Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                    Icon(
//                        painter = painterResource(R.drawable.search),
//                        contentDescription = "Search Icon",
//                        modifier = Modifier.size(25.dp)
//                    )
//                }
//            }
//            // Create an icon button for add
//            IconButton(onClick = { /* do something */ }) {
//                Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                    Icon(
//                        painter = painterResource(R.drawable.add_post),
//                        contentDescription = "Add Icon",
//                        modifier = Modifier.size(25.dp)
//                    )
//                }
//            }
//            // Create an icon button for media
//            IconButton(onClick = { /* do something */ }) {
//                Column(
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    Icon(
//                        painter = painterResource(R.drawable.video),
//                        contentDescription = "Media Icon",
//                        modifier = Modifier.size(28.dp)
//                    )
//                }
//            }
//            // Create an icon button for profile picture
//            IconButton(onClick = { /* do something */ }) {
//                Column(
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    AsyncImage(
//                        model = "https://unsplash.com/photos/a-bunch-of-baseballs-that-are-white-and-red-2A6FVXOiJ1w",
//                        contentDescription = null,
//                        modifier = Modifier
//                            .size(27.dp)
//                            .clip(CircleShape) // Clip the image to a circle shape
//                            .border(
//                                width = 2.dp,
//                                brush = Brush.linearGradient(
//                                    colors = listOf(Color.Yellow, Color.Red),
//                                    start = Offset(0f, 0f),
//                                    end = Offset(70f, 70f)
//                                ),
//                                shape = CircleShape
//                            )
//                            .clickable(onClick = {}), // Make the image clickable
//                        contentScale = ContentScale.Crop // Crop the image to fill the size of the ImageView
//                    )
//                }
//            }
//        }
//    }
//}
//
//
//private fun handleFirebaseException(exception: Exception?, context: Context) {
//    val errorMessage = when (exception) {
//        is FirebaseAuthInvalidCredentialsException -> "Invalid email or password."
//        is FirebaseAuthUserCollisionException -> "Email already in use."
//        is FirebaseAuthWeakPasswordException -> "Password is too weak."
//        else -> "An unknown error occurred."
//    }
//    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
//}
