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
data class Post(val title: String, val content: String)


fun getRecommendations(userId: String, callback: (List<String>?, String?) -> Unit) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val urlString = "https://us-central1-mlbfanengagementapp.cloudfunctions.net/recommend_content_cloud_function1?user_id=$userId" // Replace with your actual API endpoint
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

data class RecommendationResponse(val recommendations: List<String>?, val error: String?) // Data class

@Composable
fun MainScreen(navController: NavController) {
    val context = LocalContext.current
    var error by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var testRecommendations by remember { mutableStateOf(emptyList<String?>()) } // State for test recommendations

    LaunchedEffect(Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "6BQHSRCYU54WTOX" // Get current user ID or use test ID

        getRecommendations(userId) { recommendations, err -> // Your existing API call
            if (err != null) {
                error = err
                Toast.makeText(context, "Error: $err", Toast.LENGTH_SHORT).show()
            } else if (recommendations != null) {
                testRecommendations = recommendations // Store the API results in testRecommendations
            } else {
                error = "No recommendations"
                Toast.makeText(context, "No recommendations received", Toast.LENGTH_SHORT).show()
            }
            isLoading = false
        }
    }

    Scaffold(
        topBar = { HomeAppBar() },
        bottomBar = { BottomNavigationBar() }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Text(text = "Recommended Posts", style = MaterialTheme.typography.titleSmall)

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else if (error != null) {
                Text(text = error!!, color = Color.Red)
            } else if (testRecommendations.isEmpty()) { // Check testRecommendations for empty state
                Text(text = "No recommendations available")
            } else {
                Column {
                    testRecommendations.forEach { post -> // Iterate through testRecommendations
                        Card(
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                        ) {
                            Column(Modifier.padding(16.dp)) {
                                Text(text = post ?: "", style = MaterialTheme.typography.bodyMedium) // Handle null posts
                                // Keep it simple - No HomeMainContent or anything else here
                            }
                        }
                    }
                }
            }
        }
    }
}


// Function to generate random posts with long content
fun generateRandomPosts(count: Int): List<Post> {
    val message = " Good Morning "
    val randomTitles = listOf("Post 1", "Post 2", "Post 3", "Post 4", "Post 5")

    return List(count) { index ->
        Post(
            title = randomTitles[index % randomTitles.size],
            content = message + " " + message + " " + message
        )
    }
}


@Composable
fun ImageGrid() {
    val images = listOf(
        ImageData("https://unsplash.com/photos/a-bunch-of-baseballs-that-are-white-and-red-2A6FVXOiJ1w"),
        ImageData("https://unsplash.com/photos/a-bunch-of-baseballs-that-are-white-and-red-2A6FVXOiJ1w"),
        ImageData("https://unsplash.com/photos/a-bunch-of-baseballs-that-are-white-and-red-2A6FVXOiJ1w")
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items = images) { imageData ->
            ImageCard(imageUrl = imageData.imageUrl) // Access image URL from ImageData
        }
    }
}


@Composable
fun middlecontent(navController: NavController)
{
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    val user = FirebaseAuth.getInstance().currentUser
    var username by remember { mutableStateOf("") } // Store username in a mutableState

    //call userid say hello


}

// Opt-in to use experimental Material 3 API
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeAppBar(){
    val context = LocalContext.current
    val (showMenu, setShowMenu) = remember { mutableStateOf(false) }
    // Create a top app bar
    TopAppBar(
        // Set the title of the app bar
        title = {
            Text(
                "MLB Fan Hub", // The text to display
                fontFamily = FontFamily.Cursive, // Use the custom font family "lobster"
                fontWeight = FontWeight.Bold, // Set the font weight to bold
                fontSize = 25.sp, // Set the font size to 25sp
            )
        },
        // Define the actions to include in the app bar
        actions = {
            // Create an icon button with a profile icon
            IconButton(onClick = { setShowMenu(true) }) {
                Icon(Icons.Filled.AccountCircle,contentDescription = "Profile Menu")
                // TODO if need of profile image can be added
            }
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { setShowMenu(false) }
            ) {
                DropdownMenuItem(text = { Text("Profile")},onClick = {

                    setShowMenu(false)
                })

                DropdownMenuItem(
                    text = { Text("Logout") },
                    onClick = {
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                FirebaseAuth.getInstance().signOut()
                                // Show a toast message on the main thread
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(context, "Logout Successful!", Toast.LENGTH_SHORT).show()
                                }
                            } catch (e: Exception) {
                                // Handle any sign-out errors
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(context, "Logout Failed: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                            } finally {
                                // Exit the application on the main thread
                                withContext(Dispatchers.Main) {
                                    // TODO exit the app but later change it
                                    //navController.popBackStack(navController.graph.startDestinationId, inclusive = true)
                                }
                            }
                        }
                        setShowMenu(false)
                    })
            }
        }
    )

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
fun HomeMainContent(){
    // Not necessary Create a surface with padding on the top and bottom how many likes comments
    Surface(modifier = Modifier.padding(top=6.dp, bottom = 6.dp)) {
        // Create a column to hold the content
        Column {
            // Create a row to hold the profile picture, username, and more options icon
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
                // Create a box to hold the more options icon
//                Box(
//                    modifier = Modifier.weight(1f), // Take up 1/8 of the row's width
//                    contentAlignment = Alignment.CenterEnd // Center the icon at the end of the box
//                ) {
//                    // Display the more options icon
//                    Icon(Icons.Default.MoreVert,
//                        contentDescription = null, // No content description is provided
//                        modifier = Modifier.size(26.dp)) // Set the size of the icon to 26dp
//                }
            //}

            //Image / Video
            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState()) // Enable horizontal scrolling
                    .fillMaxWidth(), // Fill the width of the parent

            ) {
                // Replace with your list of image URLs
                val images = listOf(
                    "https://unsplash.com/photos/a-bunch-of-baseballs-that-are-white-and-red-2A6FVXOiJ1w",
                    "https://unsplash.com/photos/a-bunch-of-baseballs-that-are-white-and-red-2A6FVXOiJ1w",
                    "https://unsplash.com/photos/a-bunch-of-baseballs-that-are-white-and-red-2A6FVXOiJ1w"
                )

                // Call the ImageScrollWithTextOverlay function with the list of images
                ImageScrollWithTextOverlay(images)
            }

            // Content (Reactions)
            Row(
                modifier = Modifier.fillMaxWidth(), // Fill the width of the parent
                horizontalArrangement = Arrangement.SpaceBetween // Arrange the children with space between them
            ) {
                // Create a row to hold the reaction icons
                Row {
                    // Create an icon button for likes
                    IconButton(onClick = {  }) {
                        Icon(
                            painter = painterResource(R.drawable.like), // Use the like icon resource
                            contentDescription = "Like Icon", // Provide a description for accessibility
                            modifier = Modifier.size(20.dp) // Set the size of the icon to 20dp
                        )
                    }

                    // Create an icon button for comments
                    IconButton(onClick = {  }) {
                        Icon(
                            painter = painterResource(R.drawable.comment), // Use the comment icon resource
                            contentDescription = "Comment Icon", // Provide a description for accessibility
                            modifier = Modifier.size(20.dp) // Set the size of the icon to 20dp
                        )
                    }

                    // Create an icon button for share
                    IconButton(onClick = {  }) {
                        Icon(
                            painter = painterResource(R.drawable.share), // Use the share icon resource
                            contentDescription = "Share Icon", // Provide a description for accessibility
                            modifier = Modifier.size(20.dp) // Set the size of the icon to 20dp
                        )
                    }
                }

                // Create an icon button for save
                IconButton(onClick = {  }) {
                    Icon(
                        painter = painterResource(R.drawable.save_icon), // Use the save icon resource
                        contentDescription = "Save Icon", // Provide a description for accessibility
                        modifier = Modifier.size(20.dp) // Set the size of the icon to 20dp
                    )
                }
            }

// Create a column to hold the likes, comments, and timestamp
            Column(modifier = Modifier.padding(
                start = 10.dp,
                end = 10.dp,
                top = 1.dp,
                bottom =4.dp
            )) {
                // Display the number of likes
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
            }



        }
    }
}

@Composable
fun ImageScrollWithTextOverlay(images: List<String>) {
    // Get the screen width from the local configuration
    val screenWidth = LocalConfiguration.current.screenWidthDp

    // Loop over each image in the list
   images.forEachIndexed { index, imageUrl ->
        // Create a box for each image
        Box(
            modifier = Modifier
                .width(screenWidth.dp) // Set the width of the box to the screen width
                .aspectRatio(1f) // Set the aspect ratio of the box to 1 (for a square box)
        ) {
            // Load the image asynchronously
AsyncImage(
    model = imageUrl, // The URL of the image
    contentDescription = null, // No content description is provided
    modifier = Modifier.fillMaxWidth(), // Fill the width of the parent
    contentScale = ContentScale.FillWidth // Scale the image to fill the width of the ImageView
)

            // Display the image index and total number of images
            Text(
                text = "${index + 1}/${images.size}", // The text to display
                color = Color.White, // The color of the text is white
                modifier = Modifier
                    .align(Alignment.TopEnd) // Align the text to the top end of the box
                    .padding(4.dp) // Add padding around the text
            )
        }
    }
}

@Composable
fun ImageCard(imageUrl: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = "Suggested Image",
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
        )
    }
}