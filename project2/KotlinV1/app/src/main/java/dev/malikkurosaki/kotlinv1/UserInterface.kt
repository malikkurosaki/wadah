package dev.malikkurosaki.kotlinv1

import retrofit2.http.GET

interface UserInterface {
    @GET("/user")
    fun getPost(): retrofit2.Call<List<User>>

}