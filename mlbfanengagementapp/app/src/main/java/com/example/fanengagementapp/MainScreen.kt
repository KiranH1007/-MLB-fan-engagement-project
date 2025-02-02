package com.example.fanengagementapp.ui

import android.content.Intent
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
import android.widget.Toast
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fanengagementapp.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.google.gson.Gson
import kotlinx.coroutines.tasks.await
import java.net.HttpURLConnection
import java.net.URL

data class RecommendationResponse(val recommendations: List<String>?, val error: String?) // Data class

var username = ""

@Composable
fun MainScreen(navController: NavController) {
    val context = LocalContext.current
    var error by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var testRecommendations by remember { mutableStateOf(emptyList<String?>()) }

    var showAddPostDialog by remember { mutableStateOf(false) } // State for dialog visibility

    //var username by remember { mutableStateOf("") }
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    val db = FirebaseFirestore.getInstance()

    LaunchedEffect(Unit) {
        
        val userDoc = db.collection("users").document(currentUser?.uid?: "6BQHSRCYU54WTOX").get().await()

        if (userDoc.exists()) {
            username = userDoc.getString("username") ?: "" // Get username
        } else {
            error = "User document not found"
        }

        getRecommendations(username) { recommendations, err ->
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
        bottomBar = { BottomNavigationBar(navController,onAddPostClick = { showAddPostDialog = true }) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            item {
                Text(text = "Recommended Posts", style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center, modifier = Modifier.padding(16.dp))
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
    // Add Post Dialog
    if (showAddPostDialog) {
        AddPostDialog(onDismissRequest = { showAddPostDialog = false }) { newPostContent ->
            // In this simplified version, just print the content
            println("New Post Content: $newPostContent")
            showAddPostDialog = false // Close the dialog
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

                    Text("Username: $username") // Replace with dynamic data later
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
fun BottomNavigationBar(navController: NavController,onAddPostClick: () -> Unit) {
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
            IconButton(onClick =  { navController.navigate("search_screen")// TODO open search with all posts
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
            IconButton(onClick = onAddPostClick) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = painterResource(R.drawable.add_post),
                        contentDescription = "Add Icon",
                        modifier = Modifier.size(25.dp)
                    )
                }
            }
            // Create an icon button for favourites
            IconButton(onClick ={ navController.navigate("favorites_screen")// TODO open favourites with saved posts
                 } )
                 {
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



// Search Screen
@Composable
fun SearchScreen() {
    var searchText by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf(emptyList<String>()) } // Placeholder data

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Search") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(onClick = {
            // Perform search logic here (e.g., API call, filtering)
            // For prototype, just filter the placeholder data
            searchResults = listOf("Result 1", "Result 2", "Result 3").filter { it.contains(searchText, true) }
        }, modifier = Modifier.padding(8.dp)) {
            Text("Search")
        }

        LazyColumn {
            items(searchResults) { result ->
                Text(result, modifier = Modifier.padding(4.dp))
            }
        }
    }
}


// Favorites Screen
@Composable
fun FavoritesScreen() {
    // Placeholder data for favorite posts
    val favoritePosts = remember { mutableStateOf(listOf("Favorite Post 1", "Favorite Post 2")) }

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        item {
            Text("Favorite Posts", style = MaterialTheme.typography.headlineSmall)
        }
        items(favoritePosts.value) { post ->
            PostItem(post) // Reuse PostItem
        }
    }
}

@Composable
fun AddPostDialog(onDismissRequest: () -> Unit, onPostAdded: (String) -> Unit) {
    var postContent by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Create Post") },
        text = {
            OutlinedTextField(
                value = postContent,
                onValueChange = { postContent = it },
                label = { Text("What's on your mind?") }
            )
        },
        confirmButton = {
            Button(onClick = {
                onPostAdded(postContent)
            }) {
                Text("Post")
            }
        },
        dismissButton = {
            Button(onClick = onDismissRequest) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun HomeMainContent(post: String) {
    var isLiked by remember { mutableStateOf(false) }
    var isShared by remember { mutableStateOf(false) }
    var isSaved by remember { mutableStateOf(false) }
    var showCommentSection by remember { mutableStateOf(false) }
    var newComment by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row {
                IconButton(onClick = {
                    isLiked = !isLiked
                    if (isLiked) {
                        Toast.makeText(context, "Liked", Toast.LENGTH_SHORT).show()
                    }
                }) {
                    Icon(
                        painterResource(if (isLiked) R.drawable.like_filled else R.drawable.like),
                        contentDescription = "Like Icon",
                        modifier = Modifier.size(20.dp),
                        tint = if (isLiked) Color.Blue else Color.Black
                    )
                }

                IconButton(onClick = {
                    isShared = !isShared
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, "Check out this post: $post")
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Share Post"))
                }) {
                    Icon(
                        painterResource(R.drawable.share),
                        contentDescription = "Share Icon",
                        modifier = Modifier.size(20.dp)
                    )
                }

                IconButton(onClick = { showCommentSection = true }) {
                    Icon(
                        painterResource(R.drawable.comment),
                        contentDescription = "Comment Icon",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            IconButton(onClick = {
                isSaved = !isSaved
                if(isSaved) {
                    Toast.makeText(context, "Added to favourites", Toast.LENGTH_SHORT).show()
                }
                }) {
                Icon(
                    painterResource(if (isSaved) R.drawable.save_icon_filled else R.drawable.save_icon),
                    contentDescription = "Save Icon",
                    modifier = Modifier.size(20.dp),
                    tint = if (isSaved) Color.Red else Color.Black
                )
            }
        }

        if (showCommentSection) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(Color.LightGray)
            ) {
                OutlinedTextField(
                    value = newComment,
                    onValueChange = { newComment = it },
                    label = { Text("Add your comment") },
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {

                    Button(onClick = {
                        if (newComment.isNotBlank()) {
                            println("New comment: $newComment") // Replace with actual save logic
                            newComment = ""
                            showCommentSection = false
                            Toast.makeText(context, "Comment Posted", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Please write a comment", Toast.LENGTH_SHORT).show()
                        }

                    }, modifier = Modifier.padding(8.dp)) {
                        Text("Post the Comment")
                    }
                    Button(onClick = {
                        showCommentSection = false
                        newComment = ""
                    }, modifier = Modifier.padding(8.dp)) {
                        Text("Cancel")
                    }
                }
            }
        }
    }
}

fun getRecommendations(userId: String, callback: (List<String>?, String?) -> Unit) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            // Postman url for testing purposes
            //val urlString = "https://5a242042-2b8c-43d0-92ca-2bf8a2788bb7.mock.pstmn.io/user_id"
            val urlString = "https://us-central1-mlbfanengagementapp.cloudfunctions.net/recommend_content_cloud_function1?user_id=$userId"
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
            Text(
                text = post ?: "",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, fontSize = 18.sp) // Bold and larger
            )

            // Placeholder for Video/Article (remains the same)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color.LightGray)
            ) {
                Icon(painterResource(id = R.drawable.video), contentDescription = "Placeholder")
                Text("Video/Article Placeholder", modifier = Modifier.align(Alignment.Center))
            }

            HomeMainContent(post = post ?: "") // Reactions (remains the same)
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