package com.cevlikalprn.harcamalarim.api

import com.cevlikalprn.harcamalarim.model.CurrencyResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyConverterApi {

    @GET("api/latest")
    suspend fun getRates(
            @Query("base") base: String
    ): Response<CurrencyResponse>

}