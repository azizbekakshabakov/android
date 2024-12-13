package com.example.someproject.service;

import com.example.someproject.model.BalanceRequest;
import com.example.someproject.model.BalanceResponse;
import com.example.someproject.model.Car;
import com.example.someproject.model.CarsResponse;
import com.example.someproject.model.LoginRequest;
import com.example.someproject.model.LoginResponse;
import com.example.someproject.model.RegisterRequest;
import com.example.someproject.model.RegisterResponse;
import com.example.someproject.model.Rent;
import com.example.someproject.model.RentGetResponse;
import com.example.someproject.model.RentRequest;
import com.example.someproject.model.RentResponse;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    @POST("/auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("/auth/register")
    Call<RegisterResponse> register(@Body RegisterRequest request);

    @GET("/car/")
    Call<List<Car>> getCars();

    @GET("/car/{id}")
    Call<Car> getCar(@Path("id") String carId);

    @POST("/rent/")
    Call<RentResponse> rentCar(@Body RentRequest request);

    @GET("/rent/")
    Call<RentGetResponse> getRents();

    @DELETE("/rent/{id}")
    Call<ResponseBody> removeRent(@Path("id") String rentId);

    @POST("/rent/balance")
    Call<Response> addBalance(@Body BalanceRequest request);

    @GET("/rent/balance")
    Call<BalanceResponse> getBalance(@Header("Authorization") String token);
}
