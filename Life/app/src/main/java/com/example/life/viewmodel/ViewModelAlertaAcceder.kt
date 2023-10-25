package com.example.life.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.life.service.APIInterface
import com.example.life.service.APIUtils
import com.example.life.service.models.RequestLastNoti
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewModelAlertaAcceder : ViewModel() {

    private var callLastUbi: Call<RequestLastNoti>? = null

    private val _lastUbication: MutableLiveData<String> = MutableLiveData("0.0,0.0")
    val lastUbication: LiveData<String> = _lastUbication


    fun setNewCoordinates(newCoordinates:String){
        _lastUbication.postValue(newCoordinates)
    }

    fun getLastUbicationService(
        context: Context,
        token: String,
        callback: (Boolean, String) -> Unit
    ) {

        callLastUbi?.cancel()
        callLastUbi =
            APIUtils.getUtils(context, "master").create(APIInterface::class.java)
                .getLastNotification(RequestLastNoti(token))
        callLastUbi?.enqueue(object : Callback<RequestLastNoti> {
            override fun onResponse(
                call: Call<RequestLastNoti>,
                response: Response<RequestLastNoti>
            ) {
                try {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            callback.invoke(true, "Coordenadas consultadas correctamente")
                            _lastUbication.postValue(response.body()?.ubicacion?.ubicacion ?: "0.0,0.0")
                        } else {
                            val message =
                                "Sin respuesta del servidor. Vuelva a intentar más tarde."
                            callback.invoke(false, message)
                        }
                    } else {
                        val message =
                            "No se pudo conectar con el servidor. Vuelva a intentar más tarde."
                        callback.invoke(false, message)
                    }

                } catch (e: Exception) {
                    val message =
                        "Error interno de la aplicación, Vuelva a intentar más tarde."
                    callback.invoke(false, message)
                }
            }

            override fun onFailure(call: Call<RequestLastNoti>, t: Throwable) {
                if (!call.isCanceled) {
                    val message =
                        "Error en el servicio de consulta, Vuelva a intentar más tarde."
                    callback.invoke(false, message)
                } else {
                    val message =
                        "Se cancelo la consulta, Vuelva a intentar más tarde."
                    callback.invoke(false, message)
                }
            }

        })


    }


    override fun onCleared() {
        println("Destruyendo instancia del ViewModelAlertaAcceder")
        super.onCleared()
    }
}