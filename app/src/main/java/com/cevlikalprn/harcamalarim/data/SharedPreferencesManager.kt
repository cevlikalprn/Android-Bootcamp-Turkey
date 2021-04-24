package com.cevlikalprn.harcamalarim.data

import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesManager {

    private var preferences: SharedPreferences? = null

    fun getSharedPreferences(context: Context): SharedPreferences?
    {
        if(preferences != null)
        {
            return preferences
        }

        preferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        return preferences
    }


}