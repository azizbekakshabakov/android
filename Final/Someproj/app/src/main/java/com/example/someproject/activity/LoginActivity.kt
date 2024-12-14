package com.example.someproject.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
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

        // доступ к sharedPref
        sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE)

        setContent {
            LoginScreen()
        }
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun LoginScreen() {
        val jwtTokenExists = remember { mutableStateOf(sharedPreferences.contains("jwt_token")) }

        // хранение введенных юзером данных
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
                        label = { Text("Пароль") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // подтвердить
                    Button(onClick = {
                        if (email.value.isNotEmpty() && password.value.isNotEmpty()) {
                            // функция логина
                            loginUser(email.value, password.value)
                        }
                    }) {
                        Text("Подтвердить")
                    }
                }
            },
            bottomBar = {
                NavigationBar ( modifier = Modifier.height(56.dp) ) {
                    if (jwtTokenExists.value) {
                        NavigationBarItem(
                            icon = { Text(text = "Авто") },
                            selected = false,
                            onClick = {
                                startActivity(Intent(this@LoginActivity, CarsActivity::class.java))
                                finishAffinity()
                            }
                        )

                        NavigationBarItem(
                            icon = { Text(text = "Ренты") },
                            selected = false,
                            onClick = {
                                startActivity(Intent(this@LoginActivity, RentsActivity::class.java))
                                finishAffinity()
                            }
                        )

                        NavigationBarItem(
                            icon = { Text(text = "Выйти") },
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
                            icon = { Text(text = "Регистрация") },
                            selected = false,
                            onClick = {
                                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
                                finishAffinity()
                            }
                        )

                        NavigationBarItem(
                            icon = { Text(text = "Войти") },
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

    // войти с помощью ретрофит и jwt токена из sharedPrefs
    private fun loginUser(email: String, password: String) {
        val loginRequest = LoginRequest(email, password)

        ApiClient.getRetrofitInstance(this).create(ApiService::class.java)
            .login(loginRequest)
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        val loginResponse = response.body()

                        // сохранить jwt в sharedPrefs
                        val editor = sharedPreferences.edit()
                        if (loginResponse != null) {
                            editor.putString("jwt_token", loginResponse.token)
                        }
                        editor.apply()

                        Toast.makeText(this@LoginActivity, "Войден успешно", Toast.LENGTH_LONG).show()

                        // перейти в CarsActivity
                        val intent = Intent(this@LoginActivity, CarsActivity::class.java)
                        startActivity(intent)
                        finishAffinity()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.println(Log.ERROR, "LoginActivity", "${t.message}")
                }
            })
    }
}
