package com.example.medical.networking

import com.example.medical.model.Login
import com.example.medical.model.User
import com.example.medical.model.UserToken
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("/book-api/register")
    fun register(@Body user: User):Call<UserToken>

    @POST("/book-api/login")
    fun login(@Body login: Login):Call<UserToken>

}