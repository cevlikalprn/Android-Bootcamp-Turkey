package com.cevlikalprn.harcamalarim.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.cevlikalprn.harcamalarim.R
import com.cevlikalprn.harcamalarim.databinding.ExpenseItemBinding
import com.cevlikalprn.harcamalarim.model.Expense
import com.cevlikalprn.harcamalarim.view.fragments.HomeFragmentDirections


class ExpensesAdapter(
        private val context: Context,
        ): RecyclerView.Adapter<ExpensesAdapter.MyViewHolder>()

{

    private var itemList = emptyList<Expense>()

    class MyViewHolder(view: ExpenseItemBinding): RecyclerView.ViewHolder(view.root){
        val billImage = view.imgBillPicture
        val statement = view.txtStatement
        val money = view.txtMoney
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: ExpenseItemBinding = ExpenseItemBinding.inflate(LayoutInflater.from(context),parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val item = itemList[position]

        when(item.billType)
        {
            0 -> holder.billImage.setImageResource(R.drawable.receipt) // fatura
            1 -> holder.billImage.setImageResource(R.drawable.rent) // kira
            2 -> holder.billImage.setImageResource(R.drawable.shopping) // diğer
            else -> holder.billImage.setImageResource(R.drawable.bill)
        }

        holder.statement.text = item.statement
        holder.money.text = item.amountOfMoney.toString()

        holder.itemView.setOnClickListener{
            onItemClicked(it, item.expenseId, item.billType, item.statement, item.amountOfMoney)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun setData(itemList: List<Expense>)
    {
        this.itemList = itemList
        notifyDataSetChanged()
    }

    private fun onItemClicked(
        view: View,
        id: Int,
        billType: Int,
        statement: String,
        amountOfMoney: Int)
    {
        val action = HomeFragmentDirections
            .actionHomeFragmentToShowTheExpenseFragment(
                id,
                billType,
                statement,
                amountOfMoney
            )

        view.findNavController().navigate(action)
    }
}