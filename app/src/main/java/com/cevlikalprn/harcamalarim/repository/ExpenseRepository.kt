package com.cevlikalprn.harcamalarim.repository

import com.cevlikalprn.harcamalarim.data.ExpenseDao
import com.cevlikalprn.harcamalarim.model.Expense

class ExpenseRepository(private val dao: ExpenseDao) {

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