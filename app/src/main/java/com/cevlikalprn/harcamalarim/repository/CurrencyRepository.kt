package com.cevlikalprn.harcamalarim.repository

import com.cevlikalprn.harcamalarim.api.RetrofitInstance
import com.cevlikalprn.harcamalarim.model.CurrencyResponse
import retrofit2.Response

class CurrencyRepository {

    private val api by lazy { RetrofitInstance.api }
    suspend fun getRates(base: String): Response<CurrencyResponse>
    {
        return api.getRates(base)
    }

}