package com.cevlikalprn.harcamalarim.repository

import com.cevlikalprn.harcamalarim.api.RetrofitInstance
import com.cevlikalprn.harcamalarim.model.CurrencyResponse
import retrofit2.Call
import retrofit2.Response

class CurrencyRepository {

    private val api by lazy { RetrofitInstance.api }
    fun getRates(base: String): Call<CurrencyResponse>
    {
        return api.getRates(base)
    }

}