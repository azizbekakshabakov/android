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
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.someproject.service.ApiClient
import com.example.someproject.service.ApiService
import com.example.someproject.model.Car
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CarsActivity : ComponentActivity() {

    private lateinit var apiService: ApiService
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CarsScreen()
        }

        apiService = ApiClient.getRetrofitInstance(this).create(ApiService::class.java)
        sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE)

        // Fetch the cars from the API
        fetchCars()
    }

    private fun fetchCars() {
        apiService.getCars().enqueue(object : Callback<List<Car>> {
            override fun onResponse(call: Call<List<Car>>, response: Response<List<Car>>) {
                if (response.isSuccessful) {
                    val carList = response.body() ?: emptyList()
                    // Call Compose function to display cars
                    setContent {
                        CarsScreen(cars = carList)
                    }
                } else {
                    Toast.makeText(this@CarsActivity, "Failed to load cars", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Car>>, t: Throwable) {
                Toast.makeText(this@CarsActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun CarsScreen(cars: List<Car> = emptyList()) {
        val jwtTokenExists = remember { mutableStateOf(sharedPreferences.contains("jwt_token")) }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            content = {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 56.dp)/////////////////////////////////
                ) {
                    items(cars) { car ->
                        CarItem(car)
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
                                startActivity(Intent(this@CarsActivity, CarsActivity::class.java))
                                finishAffinity()
                            }
                        )

                        NavigationBarItem(
                            icon = { Text(text = "Rents") },
                            selected = false,
                            onClick = {
                                startActivity(Intent(this@CarsActivity, RentsActivity::class.java))
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

                                startActivity(Intent(this@CarsActivity, LoginActivity::class.java))
                                finishAffinity()
                            }
                        )
                    } else {
                        NavigationBarItem(
                            icon = { Text(text = "Register") },
                            selected = false,
                            onClick = {
                                startActivity(Intent(this@CarsActivity, RegisterActivity::class.java))
                                finishAffinity()
                            }
                        )

                        NavigationBarItem(
                            icon = { Text(text = "Login") },
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Load the image with Glide
            val carImage = rememberImagePainter("http://10.0.2.2:3000/${car.image}")

            // Display the car image
            Image(
                painter = carImage,
                contentDescription = "Car Image",
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth()
            )

            // Display the car name
            Text(
                text = car.name,
                modifier = Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.bodyLarge
            )

            // View Details Button
            Button(
                onClick = {
                    val intent = Intent(this@CarsActivity, CarDetailsActivity::class.java)
                    intent.putExtra("carId", car._id) // Pass car ID to the details activity
                    startActivity(intent)
                },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(text = "View Details")
            }
        }
    }
}
