package com.example.someproject.service;

import android.content.Context;
import android.content.SharedPreferences;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

public class ApiClient {

    private static final String BASE_URL = "http://10.0.2.2:3000";  // адрес бэкэнда
    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance(Context context) {
        if (retrofit == null) {
            // создание OkHttpClient и интерсептор с jwt токеном
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request originalRequest = chain.request();
                            Request.Builder requestBuilder = originalRequest.newBuilder();

                            // достать jwt из sharedPrefs
                            SharedPreferences sharedPreferences = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
                            String token = sharedPreferences.getString("jwt_token", null);

                            if (token != null) {
                                // добавить токен в хэдер "x-auth-token"
                                requestBuilder.addHeader("x-auth-token", token);
                            }

                            Request modifiedRequest = requestBuilder.build();
                            return chain.proceed(modifiedRequest);
                        }
                    })
                    .build();

            // создание объекта ретрофита
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
