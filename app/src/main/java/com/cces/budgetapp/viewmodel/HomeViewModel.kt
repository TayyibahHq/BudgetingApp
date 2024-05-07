package com.cces.budgetapp.viewmodel

import android.content.Context
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cces.budgetapp.R
import com.cces.budgetapp.Utils
import com.cces.budgetapp.data.dao.ExpenseDao
import com.cces.budgetapp.data.dao.ExpenseDatabase
import com.cces.budgetapp.data.model.ExpenseEntity
import java.lang.IllegalArgumentException
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ImageNotSupported

class HomeViewModel(val dao: ExpenseDao) : ViewModel() {
    val expenses = dao.getAllExpense()

    @Composable
    fun getBalance(list: List<ExpenseEntity>): String {
        var balance = 0.0
        for (expense in list) {
            if (expense.type == stringResource(R.string.income)) {
                balance += expense.amount
            } else {
                balance -= expense.amount
            }
        }
        return "$ ${Utils.formatToDecimalValue(balance)}"
    }

    @Composable
    fun getTotalExpense(list: List<ExpenseEntity>): String {
        var total = 0.0
        for (expense in list) {
            if (expense.type == stringResource(R.string.expense)) {
                total += expense.amount
            }}
            return "$ ${Utils.formatToDecimalValue(total)}"
        }


    @Composable
    fun getTotalIncome(list: List<ExpenseEntity>): String {
        var totalIncome = 0.0
        for (expense in list) {
            if (expense.type == stringResource(R.string.income)) {
                totalIncome += expense.amount
            }
        }
        return "$ ${Utils.formatToDecimalValue(totalIncome)}"
    }

    @Composable
    fun getItemIcon(item: ExpenseEntity): Any {
        return when (item.category) {
            stringResource(R.string.entertainment) -> {
                R.drawable.ic_netflix
            }
            stringResource(R.string.cashapp) -> {
                R.drawable.ic_paypal
            }
            stringResource(R.string.coffee) -> {
                R.drawable.ic_starbucks
            }
            else -> {
                R.drawable.ic_upwork
            }
        }
    }


class HomeViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            val dao = ExpenseDatabase.getInstance(context).expenseDao()
            return HomeViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}}