package com.cevlikalprn.harcamalarim.view.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.cevlikalprn.harcamalarim.R
import com.cevlikalprn.harcamalarim.databinding.FragmentDeleteExpenseBinding
import com.cevlikalprn.harcamalarim.model.Expense
import com.cevlikalprn.harcamalarim.viewmodel.ExpenseViewModel

class DeleteExpenseFragment : Fragment() {

    private val args: DeleteExpenseFragmentArgs by navArgs()
    private lateinit var binding: FragmentDeleteExpenseBinding
    private lateinit var viewModel: ExpenseViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDeleteExpenseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(ExpenseViewModel::class.java)

        val expense = args.expense

        binding.txtStatementType.text = expense.statement
        binding.txtAmountOfMoney.text = expense.amountOfMoney.toInt().toString()

        val billType = expense.billType
        setImage(billType)

        binding.btnDelete.setOnClickListener {
            deleteExpense(expense)
        }
    }

    private fun deleteExpense(expense: Expense) {
        AlertDialog.Builder(requireContext())
                .setPositiveButton("Evet"){_,_ ->
                    viewModel.deleteExpense(expense)
                    Toast.makeText(requireContext()," Harcamanız Silindi", Toast.LENGTH_LONG).show()
                    findNavController().navigate(R.id.action_deleteExpenseFragment_to_homeFragment)
                }
                .setNegativeButton("Hayır"){_,_ ->

                }
                .setTitle("${expense.statement} Harcamasını Sil")
                .setMessage("Silmek istediğinize emin misiniz?")
                .create().show()
    }


    private fun setImage(billType: Int)
    {
        when(billType)
        {
            0 -> binding.imgBillType.setImageResource(R.drawable.receipt) // fatura
            1 -> binding.imgBillType.setImageResource(R.drawable.rent) // kira
            2 -> binding.imgBillType.setImageResource(R.drawable.shopping) // diğer
            else -> binding.imgBillType.setImageResource(R.drawable.bill)
        }
    }
}