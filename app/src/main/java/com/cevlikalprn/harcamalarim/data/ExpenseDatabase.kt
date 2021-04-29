package com.cevlikalprn.harcamalarim.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.cevlikalprn.harcamalarim.model.Expense

@Database(entities = [Expense::class], version = 6, exportSchema = false)
abstract class ExpenseDatabase: RoomDatabase() {

    abstract fun expenseDao(): ExpenseDao

    companion object
    {
        @Volatile
        private var INSTANCE: ExpenseDatabase? = null

        fun getDatabase(context: Context): ExpenseDatabase
        {
            val tempInstance = INSTANCE
            if (tempInstance != null)
            {
                return tempInstance
            }

            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ExpenseDatabase::class.java,
                    "expense_database"
                ).addMigrations(MIGRATION_1_2).build()

                INSTANCE = instance
                return instance
            }
        }


        val MIGRATION_1_2 = object : Migration(1,2){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "ALTER TABLE Expense ADD COLUMN turkLirasi DOUBLE NOT NULL DEFAULT 0.0"
                )
            }
        }
    }






}