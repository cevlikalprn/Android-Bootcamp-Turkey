package com.cevlikalprn.harcamalarim.viewmodel

import android.app.Application
import android.util.Log
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

    fun setBase(base: String){
        getRates(base)
    }

    fun getRates(base: String): MutableLiveData<CurrencyResponse>
    {
        viewModelScope.launch {
            Log.d("tag","1")
            repo.getRates(base).enqueue(object : Callback<CurrencyResponse>{
                override fun onResponse(
                    call: Call<CurrencyResponse>,
                    response: Response<CurrencyResponse>
                ) {
                    Log.d("tag","2")
                    if(response.isSuccessful)
                    {
                        Log.d("tag","3")
                        saveLatestCurrencyRates(response)
                        myRates.value = response.body()
                    }
                }

                override fun onFailure(call: Call<CurrencyResponse>, t: Throwable) {
                    Log.d("tag","4")
                    getDefaultCurrencyRates(base)
                }
            })
        }

        return myRates
    }

    //Api'den gelen son verilerin saklanması.
    private fun saveLatestCurrencyRates(response:Response<CurrencyResponse> ) {
        preferences?.edit()?.putFloat("TRY_TO_USD", response.body()!!.rates.USD.toFloat())?.apply()
        preferences?.edit()?.putFloat("TRY_TO_EUR", response.body()!!.rates.EUR.toFloat())?.apply()
        preferences?.edit()?.putFloat("TRY_TO_GBP", response.body()!!.rates.GBP.toFloat())?.apply()
    }

    //Uygulamanın ilk açılışı da dahil olmak üzere internet bağlantısı gerçekleşmemişse ya da veri api'den gelmemişse.
    // Senaryo 1 : İlk açılışta internet bağlantısı yoksa veriler benim default olarak girdiğim sonuçlardan elde edilir.
    // Senaryo 2: Eğer ilk açılışta herhangi bir bağlantı sağlanmışsa veriler api'den temin edilir ve son güncel kur saklanır.
    private fun getDefaultCurrencyRates(base: String){
        val usd = preferences?.getFloat("TRY_TO_USD", Constants.TRY_TO_USD) //veri yoksa defalult değer Constants'da
        val eur = preferences?.getFloat("TRY_TO_EUR", Constants.TRY_TO_EUR)
        val gbp = preferences?.getFloat("TRY_TO_GBP", Constants.TRY_TO_GBP)
        myRates.value = CurrencyResponse(base, Rates(eur!!.toDouble(),gbp!!.toDouble(),0.0,usd!!.toDouble()))
    }



}