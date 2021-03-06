package com.cevlikalprn.harcamalarim.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cevlikalprn.harcamalarim.data.SharedPreferencesManager
import com.cevlikalprn.harcamalarim.model.CurrencyResponse
import com.cevlikalprn.harcamalarim.model.Rates
import com.cevlikalprn.harcamalarim.repository.CurrencyRepository
import com.cevlikalprn.harcamalarim.util.Constants
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CurrencyViewModel(application: Application): AndroidViewModel(application) {

    private val repo by lazy { CurrencyRepository() }
    private val myRates: MutableLiveData<CurrencyResponse> = MutableLiveData()

    val preferences = SharedPreferencesManager.getSharedPreferences(application)

    fun getRates(base: String): MutableLiveData<CurrencyResponse>
    {
        viewModelScope.launch {
            repo.getRates(base).enqueue(object : Callback<CurrencyResponse>{
                override fun onResponse(
                    call: Call<CurrencyResponse>,
                    response: Response<CurrencyResponse>
                ) {
                    if(response.isSuccessful)
                    {
                        saveLatestCurrencyRates(response)
                        myRates.value = response.body()
                    }
                }

                override fun onFailure(call: Call<CurrencyResponse>, t: Throwable) {
                    getDefaultCurrencyRates(base)
                }
            })
        }

        return myRates
    }

    //Api'den gelen son verilerin saklanmasÄ±.
    private fun saveLatestCurrencyRates(response:Response<CurrencyResponse> ) {
        preferences?.edit()?.putFloat("USD_TO_TRY", response.body()!!.rates.USD.toFloat())?.apply()
        preferences?.edit()?.putFloat("EUR_TO_TRY", response.body()!!.rates.EUR.toFloat())?.apply()
        preferences?.edit()?.putFloat("GBP_TO_TRY", response.body()!!.rates.GBP.toFloat())?.apply()
    }

    //UygulamanÄ±n ilk aÃ§Ä±lÄ±ÅÄ± da dahil olmak Ã¼zere internet baÄlantÄ±sÄ± gerÃ§ekleÅmemiÅse ya da veri api'den gelmemiÅse.
    // Senaryo 1 : Ä°lk aÃ§Ä±lÄ±Åta internet baÄlantÄ±sÄ± yoksa veriler benim default olarak girdiÄim sonuÃ§lardan elde edilir.
    // Senaryo 2: EÄer ilk aÃ§Ä±lÄ±Åta herhangi bir baÄlantÄ± saÄlanmÄ±Åsa veriler api'den temin edilir ve son gÃ¼ncel kur saklanÄ±r.
    private fun getDefaultCurrencyRates(base: String){
        val usd = preferences?.getFloat("USD_TO_TRY", Constants.USD_TO_TRY) //veri yoksa defalult deÄer Constants'da
        val eur = preferences?.getFloat("EUR_TO_TRY", Constants.EUR_TO_TRY)
        val gbp = preferences?.getFloat("GBP_TO_TRY", Constants.GBP_TO_TRY)
        myRates.value = CurrencyResponse(base, Rates(eur!!.toDouble(),gbp!!.toDouble(),0.0,usd!!.toDouble()))
    }



}