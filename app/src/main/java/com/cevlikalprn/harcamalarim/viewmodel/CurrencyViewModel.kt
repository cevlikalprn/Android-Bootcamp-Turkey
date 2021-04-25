package com.cevlikalprn.harcamalarim.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cevlikalprn.harcamalarim.model.CurrencyResponse
import com.cevlikalprn.harcamalarim.repository.CurrencyRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class CurrencyViewModel(application: Application): AndroidViewModel(application) {

    private val repo by lazy { CurrencyRepository() }
    val myRates: MutableLiveData<Response<CurrencyResponse>> = MutableLiveData()

    fun getRates(base: String)
    {
        viewModelScope.launch {
            val rates = repo.getRates(base)
            myRates.value = rates
        }

    }

}