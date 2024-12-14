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
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.someproject.model.BalanceResponse
import com.example.someproject.service.ApiClient
import com.example.someproject.service.ApiService
import com.example.someproject.model.Car
import com.example.someproject.model.RentRequest
import com.example.someproject.model.RentResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CarDetailsActivity : ComponentActivity() {

    private lateinit var apiService: ApiService
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var carId: String

    private var car: Car? = null
    private var balance: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CarDetailsScreen()
        }

        carId = intent.getStringExtra("carId") ?: ""

        // подключение апи сервис
        apiService = ApiClient.getRetrofitInstance(this).create(ApiService::class.java)
        sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE)

        fetchCarDetails(carId)
    }

    private fun fetchCarDetails(carId: String) {
        apiService.getCar(carId).enqueue(object : Callback<Car> {
            override fun onResponse(call: Call<Car>, response: Response<Car>) {
                if (response.isSuccessful) {
                    car = response.body()

                    setContent {
                        CarDetailsScreen(car, balance)
                    }
                }
            }

            override fun onFailure(call: Call<Car>, t: Throwable) {
                Log.println(Log.ERROR, "CarDetailsActivity", "${t.message}")
            }
        })

        apiService.getBalance()
            .enqueue(object : Callback<BalanceResponse> {
                override fun onResponse(call: Call<BalanceResponse>, response: Response<BalanceResponse>) {
                    if (response.isSuccessful) {
                        balance = response.body()?.balance ?: 0.0

                        setContent {
                            CarDetailsScreen(car, balance)
                        }
                    }
                }

                override fun onFailure(call: Call<BalanceResponse>, t: Throwable) {
                    Log.e("CarDetailsActivity", "${t.message}")
                }
            })
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun CarDetailsScreen(car: Car? = null, balance: Double = 0.0) {
        val jwtTokenExists = remember { mutableStateOf(sharedPreferences.contains("jwt_token")) }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {
                        Text("Детали авто - Balance: $$balance")
                    }
                )
            },
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // если car не нул то
                    if (car != null) {
                        Text(
                            text = "Name: ${car.name}, Tariff: ${car.tariff}$ / день",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Black
                        )

                        // изобр машины
                        val carImage = rememberImagePainter("http://10.0.2.2:3000/${car.image}")
                        Image(
                            painter = carImage,
                            contentDescription = "Авто",
                            modifier = Modifier
                                .height(200.dp)
                                .fillMaxWidth()
                                .padding(top = 16.dp)
                        )

                        Text(
                            text = "Description: ${car.description}",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(top = 16.dp),
                            color = Color.Gray
                        )

                        // арнеда
                        Button(
                            onClick = { rentCar(car) },
                            modifier = Modifier.padding(top = 24.dp)
                        ) {
                            Text(text = "Арендовать")
                        }

                    }
                }
            },
            bottomBar = {
                NavigationBar ( modifier = Modifier.height(56.dp) )  {
                    if (jwtTokenExists.value) {
                        NavigationBarItem(
                            icon = { Text(text = "Авто") },
                            selected = false,
                            onClick = {
                                startActivity(Intent(this@CarDetailsActivity, CarsActivity::class.java))
                                finishAffinity()
                            }
                        )

                        NavigationBarItem(
                            icon = { Text(text = "Аренды") },
                            selected = false,
                            onClick = {
                                startActivity(Intent(this@CarDetailsActivity, RentsActivity::class.java))
                                finishAffinity()
                            }
                        )

                        NavigationBarItem(
                            icon = { Text(text = "Баланс") },
                            selected = false,
                            onClick = {
                                startActivity(Intent(this@CarDetailsActivity, BalanceActivity::class.java))
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

                                startActivity(Intent(this@CarDetailsActivity, LoginActivity::class.java))
                                finishAffinity()
                            }
                        )
                    } else {
                        NavigationBarItem(
                            icon = { Text(text = "Регистрация") },
                            selected = false,
                            onClick = {
                                startActivity(Intent(this@CarDetailsActivity, RegisterActivity::class.java))
                                finishAffinity()
                            }
                        )

                        NavigationBarItem(
                            icon = { Text(text = "Войти") },
                            selected = false,
                            onClick = {
                                startActivity(Intent(this@CarDetailsActivity, LoginActivity::class.java))
                                finishAffinity()
                            }
                        )
                    }
                }
            }
        )
    }

    private fun rentCar(car: Car) {
        val request = RentRequest(car._id)
        apiService.rentCar(request).enqueue(object : Callback<RentResponse> {
            override fun onResponse(call: Call<RentResponse>, response: Response<RentResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { rentResponse ->
                        Toast.makeText(this@CarDetailsActivity, "Авто арендовано", Toast.LENGTH_LONG).show()
                        // прееити на другие активити
                        val intent = Intent(this@CarDetailsActivity, RentsActivity::class.java)
                        startActivity(intent)
                        finishAffinity()
                    }
                }
            }

            override fun onFailure(call: Call<RentResponse>, t: Throwable) {
                Toast.makeText(this@CarDetailsActivity, "${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}
