package com.cevlikalprn.harcamalarim.repository

import com.cevlikalprn.harcamalarim.data.ExpenseDao
import com.cevlikalprn.harcamalarim.model.Expense

class ExpenseRepository(private val dao: ExpenseDao) {

    //val readAllData: List<Expense> by lazy { dao.readAllData() }
    //val readAllData: MutableLiveData<List<Expense>> = MutableLiveData()
   /*
    fun getAllData()
    {
        val list = dao.readAllData()
        readAllData.value = list
    }

    */

    suspend fun readAllData(): List<Expense>
    {
        return dao.readAllData()
    }

    suspend fun addExpense(expense: Expense)
    {
        dao.addExpense(expense)
    }

    suspend fun deleteExpense(expense: Expense)
    {
        dao.deleteExpense(expense)
    }

}