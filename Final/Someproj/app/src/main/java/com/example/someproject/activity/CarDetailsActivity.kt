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

        // Initialize the API service
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
//                    car?.let {
//                        // If car details are found, display using Compose
//                        setContent {
//                            CarDetailsScreen(car = it, balance)
//                        }
//                    } ?: run {
//                        Toast.makeText(this@CarDetailsActivity, "Car details not found", Toast.LENGTH_SHORT).show()
//                    }
                } else {
                    Toast.makeText(this@CarDetailsActivity, "Failed to fetch car details", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Car>, t: Throwable) {
                Toast.makeText(this@CarDetailsActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
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
                    Log.e("CarsActivity", "Error fetching balance: ${t.message}")
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
                        Text("Cars details - Balance: $$balance")
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
                    // Car name and tariff
                    if (car != null) {
                        Text(
                            text = "Name: ${car.name}, Tariff: ${car.tariff}$ / day",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Black
                        )

                        // Car image
                        val carImage = rememberImagePainter("http://10.0.2.2:3000/${car.image}")
                        Image(
                            painter = carImage,
                            contentDescription = "Car Image",
                            modifier = Modifier
                                .height(200.dp)
                                .fillMaxWidth()
                                .padding(top = 16.dp)
                        )

                        // Car description
                        Text(
                            text = "Description: ${car.description}",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(top = 16.dp),
                            color = Color.Gray
                        )

                        // Rent button
                        Button(
                            onClick = { rentCar(car) },
                            modifier = Modifier.padding(top = 24.dp)
                        ) {
                            Text(text = "Rent")
                        }

                    }
                }
            },
            bottomBar = {
                NavigationBar (
                    modifier = Modifier.height(56.dp)////////////////////////////////////////////////
                )  {
                    if (jwtTokenExists.value) {
                        NavigationBarItem(
                            icon = { Text(text = "Cars") },
                            selected = false,
                            onClick = {
                                startActivity(Intent(this@CarDetailsActivity, CarsActivity::class.java))
                                finishAffinity()
                            }
                        )

                        NavigationBarItem(
                            icon = { Text(text = "Rents") },
                            selected = false,
                            onClick = {
                                startActivity(Intent(this@CarDetailsActivity, RentsActivity::class.java))
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

                                startActivity(Intent(this@CarDetailsActivity, LoginActivity::class.java))
                                finishAffinity()
                            }
                        )
                    } else {
                        NavigationBarItem(
                            icon = { Text(text = "Register") },
                            selected = false,
                            onClick = {
                                startActivity(Intent(this@CarDetailsActivity, RegisterActivity::class.java))
                                finishAffinity()
                            }
                        )

                        NavigationBarItem(
                            icon = { Text(text = "Login") },
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
                        Toast.makeText(this@CarDetailsActivity, rentResponse.message, Toast.LENGTH_SHORT).show()
                        // Navigate to the rents activity
                        val intent = Intent(this@CarDetailsActivity, RentsActivity::class.java)
                        startActivity(intent)
                    }
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Failed to rent car"
                    Toast.makeText(this@CarDetailsActivity, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RentResponse>, t: Throwable) {
                Toast.makeText(this@CarDetailsActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
