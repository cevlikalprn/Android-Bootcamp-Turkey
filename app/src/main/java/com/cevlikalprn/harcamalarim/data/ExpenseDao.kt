package com.cevlikalprn.harcamalarim.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.cevlikalprn.harcamalarim.model.Expense

@Dao
interface ExpenseDao {

    @Insert
    suspend fun addExpense(expense: Expense)

    @Query("SELECT * FROM expense_table ORDER BY expenseId DESC")
    fun readAllData(): LiveData<List<Expense>>

}