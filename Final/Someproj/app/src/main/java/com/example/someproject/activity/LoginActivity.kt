package com.example.someproject.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.someproject.MainActivity
import com.example.someproject.model.LoginRequest
import com.example.someproject.model.LoginResponse
import com.example.someproject.service.ApiClient
import com.example.someproject.service.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : ComponentActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE)

        setContent {
            LoginScreen()
        }
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun LoginScreen() {
        val jwtTokenExists = remember { mutableStateOf(sharedPreferences.contains("jwt_token")) }

        // State variables to hold user input
        var email = remember { mutableStateOf("") }
        var password = remember { mutableStateOf("") }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Email input field
                    TextField(
                        value = email.value,
                        onValueChange = { email.value = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Password input field
                    TextField(
                        value = password.value,
                        onValueChange = { password.value = it },
                        label = { Text("Password") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Login button
                    Button(onClick = {
                        if (email.value.isNotEmpty() && password.value.isNotEmpty()) {
                            // Call the login function
                            loginUser(email.value, password.value)
                        } else {
                            Toast.makeText(
                                this@LoginActivity,
                                "Please fill all fields",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }) {
                        Text("Login")
                    }
                }
            },
            bottomBar = {
                NavigationBar  (
                    modifier = Modifier.height(56.dp)////////////////////////////////////////////////
                ) {
                    if (jwtTokenExists.value) {
                        NavigationBarItem(
                            icon = { Text(text = "Cars") },
                            selected = false,
                            onClick = {
                                startActivity(Intent(this@LoginActivity, CarsActivity::class.java))
                                finishAffinity()
                            }
                        )

                        NavigationBarItem(
                            icon = { Text(text = "Rents") },
                            selected = false,
                            onClick = {
                                startActivity(Intent(this@LoginActivity, RentsActivity::class.java))
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

                                startActivity(Intent(this@LoginActivity, LoginActivity::class.java))
                                finishAffinity()
                            }
                        )
                    } else {
                        NavigationBarItem(
                            icon = { Text(text = "Register") },
                            selected = false,
                            onClick = {
                                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
                                finishAffinity()
                            }
                        )

                        NavigationBarItem(
                            icon = { Text(text = "Login") },
                            selected = false,
                            onClick = {
                                startActivity(Intent(this@LoginActivity, LoginActivity::class.java))
                                finishAffinity()
                            }
                        )
                    }
                }
            }
        )
    }

    // Function to log the user in via Retrofit and save JWT token to SharedPreferences
    private fun loginUser(email: String, password: String) {
        val loginRequest = LoginRequest(email, password)

        ApiClient.getRetrofitInstance(this).create(ApiService::class.java)
            .login(loginRequest)
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        val loginResponse = response.body()!!

                        // Save the JWT token to SharedPreferences
                        saveJwtToken(loginResponse.token)

                        // Navigate to the next activity (e.g., HomeActivity)
                        navigateToHome()
                    } else {
                        Toast.makeText(this@LoginActivity, "Login Failed", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(this@LoginActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    // Save the JWT token in SharedPreferences
    private fun saveJwtToken(token: String) {
        val editor = sharedPreferences.edit()
        editor.putString("jwt_token", token)
        editor.apply()
    }

    // Navigate to HomeActivity after successful login
    private fun navigateToHome() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        finish() // Close the login activity
    }
}
