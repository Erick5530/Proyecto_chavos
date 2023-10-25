package com.example.life.service.models

import com.google.gson.annotations.SerializedName

class RequestLastNoti(
    val token: String
) {

    @SerializedName("ubicacion")
    var ubicacion: Ubicacion? = null

    class Ubicacion {
        @SerializedName("UBICACION")
        val ubicacion: String? = null

        @SerializedName("FECHA")
        val fecha: String? = null
    }


}