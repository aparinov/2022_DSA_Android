package com.example.student_assistant.data.network

import android.accounts.NetworkErrorException
import com.example.student_assistant.data.network.entity.MessageResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.*
import java.lang.reflect.Type
import java.net.UnknownHostException

class ErrorConverterCall<T>(private val delegate: Call<T>, private val type: Type) : Call<T> by delegate {
    override fun enqueue(callback: Callback<T>) {
        delegate.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful) {
                    callback.onResponse(call, response)
                } else {
                    val type = object : TypeToken<MessageResponse>() {}.type
                    val errorResponse: MessageResponse? = Gson().fromJson(response.errorBody()!!.charStream(), type)
                    callback.onFailure(call, IllegalStateException(errorResponse?.detail))
                }
            }
            override fun onFailure(call: Call<T>, t: Throwable) {
                callback.onFailure(call, safeConvert(t))
            }
        })
    }

    override fun clone(): ErrorConverterCall<T> = ErrorConverterCall(delegate.clone(), type)

    private fun safeConvert(throwable: Throwable): Throwable {
        return runCatching {
            return when (throwable) {
                is IllegalStateException -> {
                    return throwable
                }
                is HttpException -> {
                    return IllegalStateException(throwable.code().toString() + " " + throwable.message())
//                    return when (throwable.code() / 100) {
//                        4 -> IllegalStateException(throwable.message())
//                        5 -> IllegalStateException("Сервер не работает. Сообщите об ошибке разработчику.")
//                        else -> IllegalStateException("Повторите запрос и сообщите об ошибке разработчику.")
//                    }
                }
                is UnknownHostException -> NetworkErrorException("Нет подключения к сети.")
                else -> IllegalStateException("Повторите запрос и сообщите об ошибке разработчику.")
            }
        }.getOrElse { throwable }
    }
}