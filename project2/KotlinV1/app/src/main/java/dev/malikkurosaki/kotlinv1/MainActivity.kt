package dev.malikkurosaki.kotlinv1

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*val userInterface = DataRepository.create()
        userInterface.getPost().enqueue(object : Callback<List<User>> {
            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Log.d("aaaa",t.toString())
            }

            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {

                val data = response.body();

                data?.map {
                    isian.append("${it.name} \n")
                }
            }

        })*/


        val inter = Kurungan.create()
        inter.getNama().enqueue(object :Callback<List<Nama>>{
            override fun onFailure(call: Call<List<Nama>>, t: Throwable) {
                Log.d("gangguan",t.toString())
            }

            override fun onResponse(call: Call<List<Nama>>, response: Response<List<Nama>>) {
                val data = response.body()

                data?.map {
                    isian.append("${it.name} \n")
                }
            }

        })
    }

    data class Nama(val id:String,val name:String);

    interface NamaUser{
        @GET("user")
        fun getNama():Call<List<Nama>>
    }

    object Kurungan{
        fun create():NamaUser{
            val ret = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://radiant-caverns-42043.herokuapp.com/")
                .build()
            return ret.create(NamaUser::class.java)
        }
    }
}
