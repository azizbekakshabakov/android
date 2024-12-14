package com.example.someproject.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.someproject.service.ApiClient
import com.example.someproject.service.ApiService
import com.example.someproject.model.RegisterRequest
import com.example.someproject.model.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : ComponentActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE)

        setContent {
            RegisterScreen()
        }
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun RegisterScreen() {
        val jwtTokenExists = remember { mutableStateOf(sharedPreferences.contains("jwt_token")) }

        var email = remember { mutableStateOf("") }
        var password = remember { mutableStateOf("") }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            content = {
                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // логин
                    TextField(
                        value = email.value,
                        onValueChange = { email.value = it },
                        label = { Text("Логин") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // пароль
                    TextField(
                        value = password.value,
                        onValueChange = { password.value = it },
                        label = { Text("Парол") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(onClick = {
                        if (email.value.isNotEmpty() && password.value.isNotEmpty()) {

                            registerUser(email.value, password.value)
                        } else {
                            Toast.makeText(
                                this@RegisterActivity,
                                "Please fill all fields",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }) {
                        Text("Register")
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
                                startActivity(Intent(this@RegisterActivity, CarsActivity::class.java))
                                finishAffinity()
                            }
                        )

                        NavigationBarItem(
                            icon = { Text(text = "Rents") },
                            selected = false,
                            onClick = {
                                startActivity(Intent(this@RegisterActivity, RentsActivity::class.java))
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

                                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                                finishAffinity()
                            }
                        )
                    } else {
                        NavigationBarItem(
                            icon = { Text(text = "Register") },
                            selected = false,
                            onClick = {
                                startActivity(Intent(this@RegisterActivity, RegisterActivity::class.java))
                                finishAffinity()
                            }
                        )

                        NavigationBarItem(
                            icon = { Text(text = "Login") },
                            selected = false,
                            onClick = {
                                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                                finishAffinity()
                            }
                        )
                    }
                }
            }
        )
    }

    // Function to register the user via Retrofit
    private fun registerUser(email: String, password: String) {
        val registerRequest = RegisterRequest(email, password, "user")

        ApiClient.getRetrofitInstance(this).create(ApiService::class.java)
            .register(registerRequest)
            .enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        val registerResponse = response.body()
                        Toast.makeText(this@RegisterActivity, "Registration Successful", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@RegisterActivity, "Registration Failed", Toast.LENGTH_SHORT).show()
                        Log.println(Log.INFO, "taggg", "asdfasdfasdf")
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    Toast.makeText(this@RegisterActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    Log.println(Log.INFO, "taggg", "asdfasdfasdf ${t.message}")
                }
            })
    }
}
