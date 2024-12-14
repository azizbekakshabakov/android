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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.someproject.model.BalanceRequest
import com.example.someproject.model.BalanceResponse
import com.example.someproject.model.Car
import com.example.someproject.service.ApiClient
import com.example.someproject.service.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BalanceActivity : ComponentActivity() {
    private lateinit var apiService: ApiService
    private lateinit var sharedPreferences: SharedPreferences

//    private var newBalance: Double = 0.0
    private var balance: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        apiService = ApiClient.getRetrofitInstance(this).create(ApiService::class.java)
        sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE)

        fetchBalance()
        setContent {
            BalanceScreen()
        }
    }

    private fun fetchBalance() {
        apiService.getBalance()
            .enqueue(object : Callback<BalanceResponse> {
                override fun onResponse(call: Call<BalanceResponse>, response: Response<BalanceResponse>) {
                    if (response.isSuccessful) {
                        balance = response.body()?.balance ?: 0.0
                        setContent {
                            BalanceScreen(balance)
                        }
                    }
                }

                override fun onFailure(call: Call<BalanceResponse>, t: Throwable) {
                    Log.e("CarsActivity", "Error fetching balance: ${t.message}")
                }
            })
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun BalanceScreen(balance: Double = 0.0) {
        val balanceInput = remember { mutableStateOf("") }
        val jwtTokenExists = remember { mutableStateOf(sharedPreferences.contains("jwt_token")) }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {
                        Text("Cars Activity - Balance: $$balance")
                    }
                )
            },
            content = {
                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Add Balance")

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = balanceInput.value,
                        onValueChange = { balanceInput.value = it },
                        label = { Text("Enter Amount") },
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(onClick = {
                        val amount = balanceInput.value.toDoubleOrNull()
                        if (amount != null) {
                            sendBalanceUpdate(amount)
                        }
                    }) {
                        Text("Оптравить")
                    }
                }
            },
            bottomBar = {
                NavigationBar (modifier = Modifier.height(56.dp)) {
                    if (jwtTokenExists.value) {
                        NavigationBarItem(
                            icon = { Text(text = "Авто") },
                            selected = false,
                            onClick = {
                                startActivity(Intent(this@BalanceActivity, CarsActivity::class.java))
                                finishAffinity()
                            }
                        )

                        NavigationBarItem(
                            icon = { Text(text = "Мои ренты") },
                            selected = false,
                            onClick = {
                                startActivity(Intent(this@BalanceActivity, RentsActivity::class.java))
                                finishAffinity()
                            }
                        )

                        NavigationBarItem(
                            icon = { Text(text = "Баланс") },
                            selected = false,
                            onClick = {
                                startActivity(Intent(this@BalanceActivity, BalanceActivity::class.java))
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

                                startActivity(Intent(this@BalanceActivity, LoginActivity::class.java))
                                finishAffinity()
                            }
                        )
                    } else {
                        NavigationBarItem(
                            icon = { Text(text = "Регистрация") },
                            selected = false,
                            onClick = {
                                startActivity(Intent(this@BalanceActivity, RegisterActivity::class.java))
                                finishAffinity()
                            }
                        )

                        NavigationBarItem(
                            icon = { Text(text = "Войти") },
                            selected = false,
                            onClick = {
                                startActivity(Intent(this@BalanceActivity, LoginActivity::class.java))
                                finishAffinity()
                            }
                        )
                    }
                }
            }
        )
    }

    private fun sendBalanceUpdate(amount: Double) {
        val apiService = ApiClient.getRetrofitInstance(this).create(com.example.someproject.service.ApiService::class.java)
        val request = BalanceRequest(amount)

        apiService.addBalance(request).enqueue(object : Callback<BalanceResponse> {
            override fun onResponse(call: Call<BalanceResponse>, response: Response<BalanceResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@BalanceActivity, "Balance added successfully!", Toast.LENGTH_LONG).show()
                    fetchBalance()
                } else {
                    Toast.makeText(this@BalanceActivity, "Failed to add balance", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<BalanceResponse>, t: Throwable) {
                Toast.makeText(this@BalanceActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })

    }
}
