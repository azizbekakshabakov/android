package com.example.someproject.activity

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.someproject.service.ApiClient
import com.example.someproject.service.ApiService
import com.example.someproject.model.Car
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CarDetailsActivity : ComponentActivity() {

    private lateinit var linearLayout: LinearLayout
    private lateinit var apiService: ApiService
    private lateinit var carId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        linearLayout = LinearLayout(this)
        linearLayout.orientation = LinearLayout.VERTICAL

        // Get the car ID passed from the previous activity
        carId = intent.getStringExtra("carId") ?: ""

        // Initialize API service
        apiService = ApiClient.getRetrofitInstance().create(ApiService::class.java)

        // Fetch car details from the API
        if (carId.isNotEmpty()) {
            fetchCarDetails(carId)
        } else {
            Toast.makeText(this, "Car ID not found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchCarDetails(carId: String) {
        apiService.getCar(carId).enqueue(object : Callback<Car> {
            override fun onResponse(call: Call<Car>, response: Response<Car>) {
                if (response.isSuccessful) {
                    val car = response.body()


                    car?.let {
                        // Display car details if the response is successful
                        displayCarDetails(it)
                    } ?: run {
                        Toast.makeText(this@CarDetailsActivity, "Car details not found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@CarDetailsActivity, "Failed to fetch car details", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Car>, t: Throwable) {
                Toast.makeText(this@CarDetailsActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun displayCarDetails(car: Car) {
        // Create TextViews to display car details
        val carNameTextView = TextView(this)
        carNameTextView.text = "Name: ${car.name}, Tariff: ${car.tariff}$/день"
        carNameTextView.textSize = 22f

        val imageView = androidx.compose.ui.platform.ComposeView(this).apply {
            setContent {
                Image(
                    painter = rememberImagePainter("http://10.0.2.2:3000/${car.image}"),
                    contentDescription = "Car Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }
        }

        val carDescriptionTextView = TextView(this)
        carDescriptionTextView.text = "Description: ${car.description}"
        carDescriptionTextView.textSize = 18f

        // Create a button to rent the car
        val rentButton = Button(this)
        rentButton.text = "Rent"
        rentButton.setOnClickListener {
            rentCar(car)
        }

        // Add views to the layout
        linearLayout.addView(carNameTextView)
        linearLayout.addView(imageView)
        linearLayout.addView(carDescriptionTextView)
        linearLayout.addView(rentButton)

        // Set the dynamically created layout to the activity
        setContentView(linearLayout)
    }

    private fun rentCar(car: Car) {
        // Implement rent functionality here
        // Example: You could show a Toast message or call an API to rent the car
        Toast.makeText(this, "Renting car: ${car.name}", Toast.LENGTH_SHORT).show()

        // You can also make a network call to rent the car if needed
    }
}
