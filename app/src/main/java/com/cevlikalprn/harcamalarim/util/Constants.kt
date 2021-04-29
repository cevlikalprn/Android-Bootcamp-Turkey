package com.cevlikalprn.harcamalarim.util

object Constants {

    const val BASE_URL = "https://api.ratesapi.io/"

    //Uygulama ilk açıldığında internet bağlantısı yoksa kullanılacak güncel tahmini kurlar.
    const val TRY_TO_USD = 0.12F
    const val TRY_TO_EUR = 0.10F
    const val TRY_TO_GBP = 0.08F

    const val base = "TRY"
}