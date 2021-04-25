package com.cevlikalprn.harcamalarim.repository

import androidx.lifecycle.LiveData
import com.cevlikalprn.harcamalarim.data.ExpenseDao
import com.cevlikalprn.harcamalarim.model.Expense

class ExpenseRepository(private val dao: ExpenseDao) {

    val readAllData: LiveData<List<Expense>> by lazy { dao.readAllData() }

    suspend fun addExpense(expense: Expense)
    {
        dao.addExpense(expense)
    }

    suspend fun deleteExpense(expense: Expense)
    {
        dao.deleteExpense(expense)
    }
}