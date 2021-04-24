package com.cevlikalprn.harcamalarim.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.cevlikalprn.harcamalarim.R
import com.cevlikalprn.harcamalarim.databinding.FragmentAddExpenseBinding
import com.cevlikalprn.harcamalarim.model.Expense
import com.cevlikalprn.harcamalarim.viewmodel.ExpenseViewModel

class AddExpenseFragment : Fragment() {

    private lateinit var binding: FragmentAddExpenseBinding
    private lateinit var viewModel: ExpenseViewModel
    private var rbBillType: Int? = null
    private var rbCurrency: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddExpenseBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(ExpenseViewModel::class.java)

        binding.btnAddExpense.setOnClickListener {
            saveData()
        }

        binding.rgExpense.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener{
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                when(checkedId)
                {
                    R.id.rb_bill -> rbBillType = 0 // Fatura seçildi
                    R.id.rb_rent -> rbBillType = 1 // Kira seçildi
                    R.id.rb_other -> rbBillType = 2 // Diğer seçildi
                }
            }

        })

        binding.rgCurrency.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener{
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                when(checkedId)
                {
                    R.id.rb_turk_lirasi -> rbCurrency = 0 // Türk Lirası seçildi
                    R.id.rb_dolar -> rbCurrency = 1 // Dolar seçildi
                    R.id.rb_euro -> rbCurrency = 2 // Euro seçildi
                    R.id.rb_sterlin -> rbCurrency = 3 // Sterlin seçildi
                }
            }

        })
    }

    private fun saveData()
    {
        val statement = binding.etStatement.text.toString()
        val expense = binding.etExpense.text.toString()

        if (statement == "" || expense == "" || rbBillType == null || rbCurrency == null)
        {
            Toast.makeText(requireContext(), "Lütfen boşlukları doldurunuz", Toast.LENGTH_LONG).show()
        }
        else
        {
            insertDataToDatabase(statement, expense, rbBillType!!, rbCurrency!!)
            findNavController().navigate(R.id.action_addExpenseFragment_to_homeFragment)
            Toast.makeText(requireContext(), "Harcamanız Eklendi!",Toast.LENGTH_LONG).show()
        }

    }

    private fun insertDataToDatabase(statement: String, expense: String, billType: Int, currency: Int) {
        viewModel.addExpense(
            Expense(
                0,
                statement,
                Integer.parseInt(expense),
                billType,
                currency)
        )
    }
}