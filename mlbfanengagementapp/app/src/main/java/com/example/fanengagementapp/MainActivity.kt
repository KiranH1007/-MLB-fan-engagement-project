package com.example.fanengagementapp



import android.annotation.SuppressLint

import android.os.Bundle

import androidx.activity.ComponentActivity

import androidx.activity.compose.setContent

import androidx.compose.foundation.Image

import androidx.compose.foundation.background

import androidx.compose.foundation.layout.*

import androidx.compose.material3.*

import androidx.compose.runtime.*

import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier

import androidx.compose.ui.graphics.Color

import androidx.compose.ui.platform.LocalSoftwareKeyboardController

import androidx.compose.ui.res.painterResource

import androidx.compose.ui.text.input.PasswordVisualTransformation

import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.unit.dp

import androidx.navigation.NavHostController

import androidx.navigation.compose.*
import androidx.navigation.compose.rememberNavController

import com.example.fanengagementapp.ui.theme.FanengagementappTheme





class MainActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContent {

            FanengagementappTheme {

                Scaffold {

                    Surface(

                        modifier = Modifier.fillMaxSize(),

                        color = MaterialTheme.colorScheme.background

                    ) {

                        Navigation()

                    }

                }

            }

        }

    }

}



@Composable

fun Navigation() {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login_screen") {

        composable("login_screen") { LoginScreen(navController) }

        composable("signup_screen") { SignupScreen(navController) }

    }

}



@Composable

fun LoginScreen(navController: NavHostController) {

    var username by remember { mutableStateOf("") }

    var password by remember { mutableStateOf("") }

    val keyboardController = LocalSoftwareKeyboardController.current



    Column(

        modifier = Modifier

            .fillMaxSize()

            .padding(16.dp),

        horizontalAlignment = Alignment.CenterHorizontally,

        verticalArrangement = Arrangement.Center

    ) {

        Text(text = "MLB Fan Engagement",color = Color.Black, style = MaterialTheme.typography.headlineMedium , textAlign = TextAlign.Center)

        Spacer(modifier = Modifier.height(16.dp))



        TextField(

            value = username,

            onValueChange = { username = it },

            label = { Text("Username/Email") },

            modifier = Modifier.fillMaxWidth()

        )

        Spacer(modifier = Modifier.height(8.dp))



        TextField(

            value = password,

            onValueChange = { password = it },

            label = { Text("Password") },

            modifier = Modifier.fillMaxWidth(),

            visualTransformation = PasswordVisualTransformation()

        )

        Spacer(modifier = Modifier.height(16.dp))



        Button(onClick = {

// Simple login logic (replace with actual authentication)

            if (username == "user" && password == "password") {

// Navigate to a success screen or home screen

// navController.navigate("home_screen")

            } else {

// Show an error message (e.g., Snackbar)

            }

            keyboardController?.hide() // Hide the keyboard after login attempt

        }) {

            Text("Login")

        }

        Spacer(modifier = Modifier.height(16.dp))



        TextButton(onClick = { navController.navigate("signup_screen") }) {

            Text("Don't have an account? Sign Up")

        }

    }

}



@Composable

fun SignupScreen(navController: NavHostController) {

    var username by remember { mutableStateOf("") }

    var email by remember { mutableStateOf("") }

    var password by remember { mutableStateOf("") }

    val keyboardController = LocalSoftwareKeyboardController.current



    Column(

        modifier = Modifier

            .fillMaxSize()

            .padding(16.dp),

        horizontalAlignment = Alignment.CenterHorizontally,

        verticalArrangement = Arrangement.Center

    ) {

        Text(text = "Sign Up", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))



        TextField(

            value = username,

            onValueChange = { username = it },

            label = { Text("Username") },

            modifier = Modifier.fillMaxWidth()

        )

        Spacer(modifier = Modifier.height(8.dp))



        TextField(

            value = email,

            onValueChange = { email = it },

            label = { Text("Email") },

            modifier = Modifier.fillMaxWidth()

        )

        Spacer(modifier = Modifier.height(8.dp))



        TextField(

            value = password,

            onValueChange = { password = it },

            label = { Text("Password") },

            modifier = Modifier.fillMaxWidth(),

            visualTransformation = PasswordVisualTransformation()

        )

        Spacer(modifier = Modifier.height(16.dp))



        Button(onClick = {

// Handle signup logic here (e.g., API call, data validation)

            keyboardController?.hide() // Hide the keyboard after signup attempt

        }) {

            Text("Sign Up")

        }

        Spacer(modifier = Modifier.height(16.dp))



        TextButton(onClick = { navController.navigate("login_screen") }) {

            Text("Already have an account? Login")

        }

    }

}

