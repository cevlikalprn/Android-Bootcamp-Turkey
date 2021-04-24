package com.cevlikalprn.harcamalarim.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expense_table")
data class Expense(
        @PrimaryKey(autoGenerate = true)
        val expenseId: Int,

        @ColumnInfo(name = "statement")
        val statement: String,

        @ColumnInfo(name = "amount_of_money")
        val amountOfMoney: Int,

        @ColumnInfo(name = "bill_type")
        val billType: Int,

        @ColumnInfo(name = "currency_type")
        val currencyType: Int
)