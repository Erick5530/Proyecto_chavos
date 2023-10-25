package com.example.life.service;

import com.example.life.service.models.RequestLastNoti;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface APIInterface {
    // GET
    @POST("Contacto/VerUltimaNotificacion")
    Call<RequestLastNoti> getLastNotification(@Body RequestLastNoti postCatalogos);

}