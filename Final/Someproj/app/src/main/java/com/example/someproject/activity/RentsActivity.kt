package com.example.someproject.activity

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.someproject.model.Car
import com.example.someproject.model.Rent
import com.example.someproject.model.RentGetResponse
import com.example.someproject.service.ApiClient
import com.example.someproject.service.ApiService
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RentsActivity : ComponentActivity() {

    private lateinit var apiService: ApiService
    private lateinit var token: String
    private lateinit var linearLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create the parent layout programmatically
        linearLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(16, 16, 16, 16)
        }
        setContentView(ScrollView(this).apply { addView(linearLayout) })

        // Retrieve the JWT token from shared preferences
        val sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        token = sharedPreferences.getString("jwt_token", null) ?: ""

        // Initialize API service
        apiService = ApiClient.getRetrofitInstance(this).create(ApiService::class.java)

        fetchRents()
    }

    private fun fetchRents() {
        apiService.getRents().enqueue(object : Callback<RentGetResponse> {
            override fun onResponse(call: Call<RentGetResponse>, response: Response<RentGetResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { rentGetResponse ->
                        // Display rents and cars data
                        displayRents(rentGetResponse.rents, rentGetResponse.cars)
                    }
                } else {
                    Toast.makeText(this@RentsActivity, "Failed to fetch rents", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RentGetResponse>, t: Throwable) {
                Log.e("Failed", t.message ?: "Unknown error")
                Toast.makeText(this@RentsActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun displayRents(rents: List<Rent>, cars: List<Car>) {
        // Clear the layout first to avoid duplication
        linearLayout.removeAllViews()

        for (rent in rents) {
            val car = cars.find { it._id == rent.carId } // Match car data to rent
            val carName = TextView(this).apply {
                text = "Car: ${car?.name ?: "Unknown"}" // Use car name if available
                textSize = 18f
                setPadding(0, 8, 0, 8)
            }

            val deleteButton = Button(this).apply {
                text = "Delete Rent"
                setOnClickListener { deleteRent(rent.carId) }
            }

            val separator = View(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 2
                ).apply { setMargins(0, 16, 0, 16) }
                setBackgroundColor(Color.LTGRAY)
            }

            // Add views to the parent layout
            linearLayout.addView(carName)
            linearLayout.addView(deleteButton)
            linearLayout.addView(separator)
        }
    }


    private fun deleteRent(carId: String) {
        apiService.removeRent(carId).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@RentsActivity, "Rent deleted successfully", Toast.LENGTH_SHORT).show()
                    fetchRents() // Refresh the list
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Failed to delete rent"
                    Toast.makeText(this@RentsActivity, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@RentsActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
