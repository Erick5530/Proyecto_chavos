package com.example.life.service;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.example.life.R;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIUtils {
    public static boolean isNewThread = false;

    public static Retrofit getUtils(Context act, String endPoint) {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .addInterceptor(interceptor);
        client.connectTimeout(100, TimeUnit.SECONDS);
        client.readTimeout(100, TimeUnit.SECONDS);

        OkHttpClient okHttpClient = client.build();

        String BASE_URL = "";

        switch (endPoint) {
            case "master":
                BASE_URL = act.getString(R.string.pref_default_base_url);
                break;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .callbackExecutor((APIUtils.isNewThread) ? Executors.newSingleThreadExecutor() : new MainThreadExecutor())
                .client(okHttpClient)
                .build();

        APIUtils.isNewThread = false;

        return retrofit;
    }

    public static Retrofit getUtils(
            Context act,
            String endPoint,
            int timeOutInSeconds
    ) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder client = new OkHttpClient.Builder().addInterceptor(interceptor);
        client.connectTimeout(timeOutInSeconds, TimeUnit.SECONDS);
        client.readTimeout(timeOutInSeconds, TimeUnit.SECONDS);

        OkHttpClient okHttpClient = client.build();

        String BASE_URL = "";

        switch (endPoint) {
            case "master":
                BASE_URL = act.getString(R.string.pref_default_base_url);
                break;

        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .callbackExecutor((APIUtils.isNewThread) ? Executors.newSingleThreadExecutor() : new MainThreadExecutor())
                .client(okHttpClient)
                .build();

        APIUtils.isNewThread = false;

        return retrofit;
    }

    public static void setInNewSingleThread() {
        isNewThread = true;
    }

    static class MainThreadExecutor implements Executor {
        private final Handler handler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            handler.post(command);
        }
    }
}
