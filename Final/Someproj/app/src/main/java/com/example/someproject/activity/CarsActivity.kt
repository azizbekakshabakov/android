package com.example.someproject.activity

import android.content.Intent
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CarsScreen()
        }

        apiService = ApiClient.getRetrofitInstance(this).create(ApiService::class.java)

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

    @Composable
    fun CarsScreen(cars: List<Car> = emptyList()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(cars) { car ->
                CarItem(car)
            }
        }
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
