package com.cevlikalprn.harcamalarim.api

import com.cevlikalprn.harcamalarim.util.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val retrofit by lazy {
        Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    val api: CurrencyConverterApi by lazy {
        retrofit.create(CurrencyConverterApi::class.java)
    }

}