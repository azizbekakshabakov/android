package com.example.someproject.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.someproject.model.BalanceResponse
import com.example.someproject.service.ApiClient
import com.example.someproject.service.ApiService
import com.example.someproject.model.Car
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CarsActivity : ComponentActivity() {

    private lateinit var apiService: ApiService
    private lateinit var sharedPreferences: SharedPreferences

    private var carList: List<Car> = emptyList()
    private var balance: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CarsScreen()
        }

        apiService = ApiClient.getRetrofitInstance(this).create(ApiService::class.java)
        sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE)

        // достать машины с бэка
        fetchCars()
        setContent {
            CarsScreen()
        }
    }

    private fun fetchCars() {
        apiService.getCars().enqueue(object : Callback<List<Car>> {
            override fun onResponse(call: Call<List<Car>>, response: Response<List<Car>>) {
                if (response.isSuccessful) {
                    carList = response.body() ?: emptyList()
                    setContent {
                        CarsScreen(cars = carList, balance = balance)
                    }
                }
            }

            override fun onFailure(call: Call<List<Car>>, t: Throwable) {
                Toast.makeText(this@CarsActivity, "${t.message}", Toast.LENGTH_LONG).show()
            }
        })

        apiService.getBalance()
            .enqueue(object : Callback<BalanceResponse> {
                override fun onResponse(call: Call<BalanceResponse>, response: Response<BalanceResponse>) {
                    if (response.isSuccessful) {
                        balance = response.body()?.balance ?: 0.0
                        setContent {
                            CarsScreen(cars = carList, balance = balance)
                        }
                    }
                }

                override fun onFailure(call: Call<BalanceResponse>, t: Throwable) {
                    Log.e("CarsActivity", "${t.message}")
                }
            })
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun CarsScreen(cars: List<Car> = emptyList(), balance: Double = 0.0) {
        val jwtTokenExists = remember { mutableStateOf(sharedPreferences.contains("jwt_token")) }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {
                        Text("Ассортимент - Баланс: $$balance")
                    }
                )
            },
            content = {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(start = 16.dp, end = 16.dp, top = 65.dp, bottom = 56.dp)
                ) {
                    items(cars) { car ->
                        CarItem(car)
                    }
                }
            },
            bottomBar = {
                NavigationBar(
                    modifier = Modifier.height(56.dp)
                ) {
                    if (jwtTokenExists.value) {
                        NavigationBarItem(
                            icon = { Text(text = "Авто") },
                            selected = false,
                            onClick = {
                                startActivity(Intent(this@CarsActivity, CarsActivity::class.java))
                                finishAffinity()
                            }
                        )

                        NavigationBarItem(
                            icon = { Text(text = "Ренты") },
                            selected = false,
                            onClick = {
                                startActivity(Intent(this@CarsActivity, RentsActivity::class.java))
                                finishAffinity()
                            }
                        )

                        NavigationBarItem(
                            icon = { Text(text = "Баланс") },
                            selected = false,
                            onClick = {
                                startActivity(Intent(this@CarsActivity, BalanceActivity::class.java))
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

                                startActivity(Intent(this@CarsActivity, LoginActivity::class.java))
                                finishAffinity()
                            }
                        )
                    } else {
                        NavigationBarItem(
                            icon = { Text(text = "Регистрация") },
                            selected = false,
                            onClick = {
                                startActivity(Intent(this@CarsActivity, RegisterActivity::class.java))
                                finishAffinity()
                            }
                        )

                        NavigationBarItem(
                            icon = { Text(text = "Войти") },
                            selected = false,
                            onClick = {
                                startActivity(Intent(this@CarsActivity, LoginActivity::class.java))
                                finishAffinity()
                            }
                        )
                    }
                }
            }
        )
    }

    @Composable
    fun CarItem(car: Car) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // картинка
            Image(
                painter = rememberImagePainter("http://10.0.2.2:3000/${car.image}"),
                contentDescription = "Car Image",
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth()
            )

            // модель
            Text(
                text = car.name,
                modifier = Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.bodyLarge
            )

            // детали
            Button(
                onClick = {
                    val intent = Intent(this@CarsActivity, CarDetailsActivity::class.java)
                    intent.putExtra("carId", car._id) // Pass car ID to the details activity
                    startActivity(intent)
                },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(text = "Детали")
            }
        }
    }
}
