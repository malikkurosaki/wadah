package dev.malikkurosaki.kotlinv1

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DataRepository {

    fun create(): UserInterface{
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://radiant-caverns-42043.herokuapp.com/")
            .build()

        return retrofit.create(UserInterface::class.java)
    }
}