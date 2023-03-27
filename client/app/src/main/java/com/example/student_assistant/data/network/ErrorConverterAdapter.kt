package com.example.student_assistant.data.network

import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

class ErrorConverterAdapter(private val type: Type) : CallAdapter<Type, Call<Type>> {
    override fun responseType(): Type = type
    override fun adapt(call: Call<Type>): Call<Type> = ErrorConverterCall(call, type)
}