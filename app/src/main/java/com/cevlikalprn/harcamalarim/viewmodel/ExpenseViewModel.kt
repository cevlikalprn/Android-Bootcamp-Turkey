package com.cevlikalprn.harcamalarim.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cevlikalprn.harcamalarim.data.ExpenseDatabase
import com.cevlikalprn.harcamalarim.model.Expense
import com.cevlikalprn.harcamalarim.repository.ExpenseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExpenseViewModel(application: Application): AndroidViewModel(application) {


    private val dao by lazy { ExpenseDatabase.getDatabase(application).expenseDao() }
    private val repository by lazy { ExpenseRepository(dao) }

    val readAllData by lazy { dao.readAllData() }

    fun addExpense(expense: Expense)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addExpense(expense)
        }
    }

    fun deleteExpense(expense: Expense)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteExpense(expense)
        }
    }
}