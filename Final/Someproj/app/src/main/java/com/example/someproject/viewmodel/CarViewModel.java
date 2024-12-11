package com.example.someproject.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.someproject.model.Car;
import com.example.someproject.service.ApiClient;
import com.example.someproject.service.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CarViewModel extends ViewModel {
//    private MutableLiveData<List<Car>> cars;
//
//    public LiveData<List<Car>> getCars() {
//        if (cars == null) {
//            cars = new MutableLiveData<>();
//            loadCars();
//        }
//        return cars;
//    }
//
//    private void loadCars() {
//        ApiClient.getRetrofitInstance().create(ApiService.class)
//                .getCars()
//                .enqueue(new Callback<List<Car>>() {
//                    @Override
//                    public void onResponse(Call<List<Car>> call, Response<List<Car>> response) {
//                        if (response.isSuccessful()) {
//                            cars.setValue(response.body());
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<List<Car>> call, Throwable t) {
//                        Log.println(Log.ERROR, "tag", "asdf");
//                    }
//                });
//    }
}
