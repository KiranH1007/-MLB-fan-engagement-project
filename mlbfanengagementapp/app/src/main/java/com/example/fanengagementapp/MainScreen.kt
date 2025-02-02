package com.example.fanengagementapp.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

import android.widget.Toast
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp

import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage

import com.example.fanengagementapp.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


import com.google.gson.Gson
import kotlinx.coroutines.*
import java.net.HttpURLConnection
import java.net.URL


data class ImageData(val imageUrl: String)


@Composable
fun MyApp() {
    val navController = rememberNavController()  // Create NavController HERE

    // Use the navController in your app's structure
    MainScreen(navController) // Pass it to MainScreen
}

data class RecommendationResponse(val recommendations: List<String>?, val error: String?) // Data class


@Composable
fun MainScreen(navController: NavController) {
    val context = LocalContext.current
    var error by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var testRecommendations by remember { mutableStateOf(emptyList<String?>()) }

    LaunchedEffect(Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "6BQHSRCYU54WTOX"

        getRecommendations(userId) { recommendations, err ->
            if (err != null) {
                error = err
                Toast.makeText(context, "Error: $err", Toast.LENGTH_SHORT).show()
            } else if (recommendations != null) {
                testRecommendations = recommendations
            } else {
                error = "No recommendations"
                Toast.makeText(context, "No recommendations received", Toast.LENGTH_SHORT).show()
            }
            isLoading = false
        }
    }

    Scaffold(
        topBar = { HomeAppBar(navController) },
        bottomBar = { BottomNavigationBar() }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            item {
                Text(text = "Recommended Posts", style = MaterialTheme.typography.titleSmall)
            }

            if (isLoading) {
                item {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            } else if (error != null) {
                item { Text(text = error!!, color = Color.Red) }
            } else if (testRecommendations.isEmpty()) {
                item { Text(text = "No recommendations available") }
            } else {
                items(testRecommendations) { post ->
                    PostItem(post = post)
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeAppBar(navController: NavController) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    var showMenu by remember { mutableStateOf(false) }
    var showProfile by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Text(
                "MLB Fan Hub",
                fontFamily = FontFamily.Cursive,
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp
            )
        },
        actions = {
            IconButton(onClick = { showMenu = true }) {
                Icon(Icons.Filled.AccountCircle, contentDescription = "Profile Menu")
            }

            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                DropdownMenuItem(text = { Text("Profile") }, onClick = {
                    showProfile = true
                    showMenu = false
                })

                DropdownMenuItem(
                    text = { Text("Logout") },
                    onClick = {
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                auth.signOut()
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(context, "Logout Successful!", Toast.LENGTH_SHORT).show()
                                    navController.navigate("login_screen") {
                                        popUpTo("main_screen") { inclusive = true }
                                    }
                                }
                            } catch (e: Exception) {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(context, "Logout Failed: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                        showMenu = false
                    })
            }
        }
    )

    if (showProfile) {
        AlertDialog(
            onDismissRequest = { showProfile = false },
            title = { Text("Profile") },
            text = {
                Column {
                    // Placeholder data for the prototype
                    Image( // Or Icon if you prefer
                        painter = painterResource(id = R.drawable.person), // Your placeholder
                        contentDescription = "Profile Image",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                    )
                    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "6BQHSRCYU54WTOX"
                    Text("Username: $userId") // Replace with dynamic data later
                    Text("Favorite Teams: Team A, Team B") // Replace with dynamic data
                    Text("Followed Players: Player X, Player Y") // Replace with dynamic data
                }
            },
            confirmButton = {
                Button(onClick = { showProfile = false }) {
                    Text("Close")
                }
            }
        )
    }
}

@Composable
fun BottomNavigationBar() {
    // Create a bottom app bar
    BottomAppBar {
        // Create a row to hold the icons
        Row(
            modifier = Modifier.fillMaxWidth(), // Fill the width of the parent
            horizontalArrangement = Arrangement.SpaceEvenly // Arrange the icons evenly
        ) {
            // Create an icon button for home
            IconButton(onClick = { // TODO open home with all posts
                 }) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = painterResource(R.drawable.home),
                        contentDescription = "Home Icon",
                        modifier = Modifier.size(25.dp)
                    )
                }
            }
            // Create an icon button for search
            IconButton(onClick = { // TODO open search with all posts
            }) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = painterResource(R.drawable.search),
                        contentDescription = "Search Icon",
                        modifier = Modifier.size(25.dp)
                    )
                }
            }
            // Create an icon button for add post
            IconButton(onClick = { // TODO open add post from gallery
            }) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = painterResource(R.drawable.add_post),
                        contentDescription = "Add Icon",
                        modifier = Modifier.size(25.dp)
                    )
                }
            }
            // Create an icon button for favourites
            IconButton(onClick = { // TODO open favourites with saved posts
                }) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        // favourite team or player or content
                        painter = painterResource(R.drawable.heart),
                        contentDescription = "Media Icon",
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}


@Composable
fun HomeMainContent() {
    Column(Modifier.fillMaxWidth()) { // Ensure it fills the card's width
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row {
                IconButton(onClick = { /* TODO: Handle Like */ }) {
                    Icon(painterResource(R.drawable.like), contentDescription = "Like Icon", modifier = Modifier.size(20.dp))
                }

                IconButton(onClick = { /* TODO: Handle Comment */ }) {
                    Icon(painterResource(R.drawable.comment), contentDescription = "Comment Icon", modifier = Modifier.size(20.dp))
                }

                IconButton(onClick = { /* TODO: Handle Share */ }) {
                    Icon(painterResource(R.drawable.share), contentDescription = "Share Icon", modifier = Modifier.size(20.dp))
                }
            }

            IconButton(onClick = { /* TODO: Handle Save */ }) {
                Icon(painterResource(R.drawable.save_icon), contentDescription = "Save Icon", modifier = Modifier.size(20.dp))
            }
        }

        // Example data (replace with your actual data)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp, top = 1.dp, bottom = 4.dp)
        ) {
            Text("2993 likes", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Text("view all 138 comments", fontWeight = FontWeight.Thin, fontSize = 13.sp)
            Text("9 minutes ago", fontWeight = FontWeight.Thin, fontSize = 13.sp)
        }
    }
}

fun getRecommendations(userId: String, callback: (List<String>?, String?) -> Unit) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val urlString = "https://5a242042-2b8c-43d0-92ca-2bf8a2788bb7.mock.pstmn.io/user_id" //"https://us-central1-mlbfanengagementapp.cloudfunctions.net/recommend_content_cloud_function1?user_id=$userId" // Replace with your actual API endpoint
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"

            val inputStream = connection.inputStream
            val response = inputStream.bufferedReader().use { it.readText() }

            val gson = Gson()
            val recommendationResponse = gson.fromJson(response, RecommendationResponse::class.java) // Use your data class

            if (recommendationResponse != null && recommendationResponse.recommendations != null) {
                callback(recommendationResponse.recommendations, null) // Success! Pass the list of strings.
            } else {
                callback(null, "Error parsing JSON or no recommendations found")
            }

            connection.disconnect()

        } catch (e: Exception) {
            callback(null, e.message) // Handle exceptions
        }
    }
}


@Composable
fun PostItem(post: String?) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(text = post ?: "", style = MaterialTheme.typography.bodyMedium)

            // Placeholder for Video/Article
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp) // Set a desired height for the placeholder
                    .background(Color.LightGray) // Optional: Add a background color
            ) {
                // You can add placeholder content here if needed, e.g., an icon or text
                // Example:
                // Icon(painterResource(id = R.drawable.ic_placeholder), contentDescription = "Placeholder")
                Text("Video/Article Placeholder", modifier = Modifier.align(Alignment.Center))
            }

            HomeMainContent() // Reactions for each post
        }
    }
}


























































/*** TODO profile building takes more time
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeAppBar(navController: NavController) {
val context = LocalContext.current
val auth = FirebaseAuth.getInstance()
val currentUser = auth.currentUser
val firestore = FirebaseFirestore.getInstance()
val storage = FirebaseStorage.getInstance()

var showMenu by remember { mutableStateOf(false) }
var showProfile by remember { mutableStateOf(false) }
var profileData by remember { mutableStateOf<UserProfile?>(null) }
var isLoading by remember { mutableStateOf(true) }
var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
if (uri != null) {
selectedImageUri = uri
uploadProfileImage(storage, currentUser!!.uid, uri) { imageUrl ->
profileData = profileData?.copy(imageUrl = imageUrl)
updateUserProfile(firestore, currentUser.uid, profileData!!)
}
}
}

LaunchedEffect(currentUser?.uid) {
if (currentUser != null) {
try {
val doc = firestore.collection("users").document(currentUser.uid).get().await()
if (doc.exists()) {
profileData = doc.toObject(UserProfile::class.java)
} else {
val newProfile = UserProfile(
uid = currentUser.uid,
username = currentUser.displayName ?: "User",
imageUrl = "",
favoriteTeams = emptyList(),
followedPlayers = emptyList()
)
firestore.collection("users").document(currentUser.uid).set(newProfile).await()
profileData = newProfile
}
} catch (e: Exception) {
Toast.makeText(context, "Error loading profile: ${e.message}", Toast.LENGTH_SHORT).show()
} finally {
isLoading = false
}
}
}

TopAppBar(
title = {
Text(
"MLB Fan Hub",
fontFamily = FontFamily.Cursive,
fontWeight = FontWeight.Bold,
fontSize = 25.sp
)
},
actions = {
IconButton(onClick = { showMenu = true }) {
Icon(Icons.Filled.AccountCircle, contentDescription = "Profile Menu")
}

DropdownMenu(
expanded = showMenu,
onDismissRequest = { showMenu = false }
) {
DropdownMenuItem(text = { Text("Profile") }, onClick = {
showProfile = true
showMenu = false
})

DropdownMenuItem(
text = { Text("Logout") },
onClick = {
CoroutineScope(Dispatchers.IO).launch {
try {
auth.signOut()
withContext(Dispatchers.Main) {
Toast.makeText(context, "Logout Successful!", Toast.LENGTH_SHORT).show()
navController.navigate("login_screen") {
popUpTo("main_screen") { inclusive = true }
}
}
} catch (e: Exception) {
withContext(Dispatchers.Main) {
Toast.makeText(context, "Logout Failed: ${e.message}", Toast.LENGTH_SHORT).show()
}
}
}
showMenu = false
})
}
}
)

if (showProfile) {
AlertDialog(
onDismissRequest = { showProfile = false },
title = { Text("Profile") },
text = {
if (isLoading) {
CircularProgressIndicator()
} else if (profileData != null) {
Column {
AsyncImage(
model = profileData.imageUrl ?: R.drawable.ic_launcher_background,
contentDescription = "Profile Image",
modifier = Modifier
.size(100.dp)
.clip(CircleShape)
.clickable { launcher.launch("image/*") },
contentScale = ContentScale.Crop
)
Text("Username: ${profileData.username}")
Text("Favorite Teams: ${profileData.favoriteTeams.joinToString(", ")}")
Text("Followed Players: ${profileData.followedPlayers.joinToString(", ")}")

}
} else {
Text("Error loading profile.")
}
},
confirmButton = {
Button(onClick = { showProfile = false }) {
Text("Close")
}
}
)
}
}


data class UserProfile(
val uid: String = "",
val username: String = "",
val imageUrl: String = "",
val favoriteTeams: List<String> = emptyList(),
val followedPlayers: List<String> = emptyList()
)


fun uploadProfileImage(storage: FirebaseStorage, userId: String, uri: Uri, callback: (String) -> Unit) {
val ref = storage.reference.child("profile_images/$userId")
val uploadTask = ref.putFile(uri)

uploadTask.continueWithTask { task ->
if (!task.isSuccessful) {
task.exception?.let {
throw it
}
}
ref.downloadUrl
}.addOnSuccessListener { downloadUri ->
callback(downloadUri.toString())
}.addOnFailureListener { exception ->
println("Error uploading image: $exception")
}
}

fun updateUserProfile(firestore: FirebaseFirestore, userId: String, profile: UserProfile) {
firestore.collection("users").document(userId)
.set(profile)
.addOnSuccessListener {
println("Document successfully written!")
}
.addOnFailureListener { e ->
println("Error writing document: $e")
}
}
***/