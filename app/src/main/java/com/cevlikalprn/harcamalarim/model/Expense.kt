package com.cevlikalprn.harcamalarim.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "expense_table")
data class Expense(
        @PrimaryKey(autoGenerate = true)
        var expenseId: Int,

        @ColumnInfo(name = "statement")
        val statement: String,

        @ColumnInfo(name = "turk_lirasi")
        var turkLirasi: Double,

        var dollar: Double,

        var euro: Double,

        var sterlin: Double,

        @ColumnInfo(name = "bill_type")
        val billType: Int,

        @ColumnInfo(name = "currency_type")
        var currencyType: Int
): Serializable