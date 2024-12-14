package com.example.someproject

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.BottomNavigation
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBar
//import androidx.compose.material3.BottomNavigationItem
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.someproject.activity.CarsActivity
import com.example.someproject.activity.LoginActivity
import com.example.someproject.activity.RegisterActivity
import com.example.someproject.activity.RentsActivity
import com.example.someproject.ui.theme.SomeProjectTheme

class MainActivity : ComponentActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE)

        setContent {
            MainScreen()
        }
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun MainScreen() {
        val jwtTokenExists = remember { mutableStateOf(sharedPreferences.contains("jwt_token")) }

        val items = listOf(
            "Register" to RegisterActivity::class.java,
            "Login" to LoginActivity::class.java,
            "Cars" to CarsActivity::class.java,
            "Rents" to RentsActivity::class.java
        )

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            content = {
                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Welcome to the Main Activity!")

                    Spacer(modifier = Modifier.height(16.dp))

                    items.forEach { (title, targetActivity) ->
                        Button(onClick = { startActivity(Intent(this@MainActivity, targetActivity)) }) {
                            Text("Go to $title Activity")
                        }
                    }
                }
            },
            bottomBar = {
                NavigationBar (
                    modifier = Modifier.height(56.dp)////////////////////////////////////////////////
                ) {
                    if (jwtTokenExists.value) {
                        NavigationBarItem(
                            icon = { Text(text = "Cars") },
                            selected = false,
                            onClick = {
                                startActivity(Intent(this@MainActivity, CarsActivity::class.java))
                                finishAffinity()
                            }
                        )

                        NavigationBarItem(
                            icon = { Text(text = "Rents") },
                            selected = false,
                            onClick = {
                                startActivity(Intent(this@MainActivity, RentsActivity::class.java))
                                finishAffinity()
                            }
                        )

                        NavigationBarItem(
                            icon = { Text(text = "Logout") },
                            selected = false,
                            onClick = {
                                val editor = sharedPreferences.edit()
                                editor.remove("jwt_token")
                                editor.apply()
                                jwtTokenExists.value = false

                                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                                finishAffinity()
                            }
                        )
                    } else {
                        NavigationBarItem(
                            icon = { Text(text = "Register") },
                            selected = false,
                            onClick = {
                                startActivity(Intent(this@MainActivity, RegisterActivity::class.java))
                                finishAffinity()
                            }
                        )

                        NavigationBarItem(
                            icon = { Text(text = "Login") },
                            selected = false,
                            onClick = {
                                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                                finishAffinity()
                            }
                        )
                    }
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SomeProjectTheme {
//        Greeting("Android")
    }
}
