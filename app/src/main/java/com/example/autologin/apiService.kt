package com.example.autologin

import com.example.autologin.LoginRequest
import retrofit2.Call
import retrofit2.http.*


interface apiService {

    @POST("/api/auth/login")
    fun loginRequest(@Body loginRequest: LoginRequest) : Call<String>

}