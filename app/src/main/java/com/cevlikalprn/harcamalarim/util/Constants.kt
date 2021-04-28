package com.cevlikalprn.harcamalarim.util

object Constants {

    const val BASE_URL = "https://api.ratesapi.io/"

    //Uygulama ilk açıldığında internet bağlantısı yoksa kullanılacak güncel tahmini kurlar.
    const val TRY_TO_USD = 0.12F
    const val TRY_TO_EUR = 0.10F
    const val TRY_TO_GBP = 0.08F

    const val EUR_TO_USD = 1.21F
    const val EUR_TO_TRY = 9.88F
    const val EUR_TO_GBP = 0.87F

    const val USD_TO_TRY = 8.18F
    const val USD_TO_EUR = 0.83F
    const val USD_TO_GBP = 0.72F

    const val GBP_TO_TRY = 11.34F
    const val GBP_TO_EUR = 1.15F
    const val GBP_TO_USD = 1.39F
}