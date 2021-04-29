package com.cevlikalprn.harcamalarim.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.cevlikalprn.harcamalarim.R
import com.cevlikalprn.harcamalarim.databinding.FragmentAddExpenseBinding
import com.cevlikalprn.harcamalarim.model.Expense
import com.cevlikalprn.harcamalarim.util.Constants
import com.cevlikalprn.harcamalarim.viewmodel.CurrencyViewModel
import com.cevlikalprn.harcamalarim.viewmodel.ExpenseViewModel
import kotlin.math.exp

class AddExpenseFragment : Fragment() {

    private lateinit var binding: FragmentAddExpenseBinding
    private lateinit var expenseViewModel: ExpenseViewModel
    private lateinit var currencyViewModel: CurrencyViewModel

    private var rbBillType: Int? = null
    private var rbCurrency: Int? = null

    private var base = "TRY"
    private var tl: Double? = null
    private var eur: Double? = null
    private var usd: Double? = null
    private var gbp: Double? = null

    private var turkLirasi: Double? = null
    private var euro: Double? = null
    private var dollar: Double? = null
    private var sterlin: Double? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddExpenseBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        expenseViewModel = ViewModelProvider(this).get(ExpenseViewModel::class.java)
        currencyViewModel = ViewModelProvider(this).get(CurrencyViewModel::class.java)

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
                    else -> rbBillType = 0
                }
            }

        })

        binding.rgCurrency.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener{
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                when(checkedId)
                {
                    R.id.rb_turk_lirasi -> {
                        rbCurrency = 0
                        base = "TRY"
                        currencyViewModel.getRates(base)
                    } // Türk Lirası seçildi
                    R.id.rb_dolar -> {
                        rbCurrency = 1
                        base = "USD"
                        currencyViewModel.getRates(base)
                    } // Dolar seçildi
                    R.id.rb_euro -> {
                        rbCurrency = 2
                        base = "EUR"
                        currencyViewModel.getRates(base)
                    } // Euro seçildi
                    R.id.rb_sterlin -> {
                        rbCurrency = 3
                        base = "GBP"
                        currencyViewModel.getRates(base)
                    } // Sterlin seçildi
                    else ->  {
                        rbCurrency = 0
                        base = "TRY"
                        currencyViewModel.getRates(base)
                    }
                }
            }

        })

        currencyViewModel.getRates(base).observe(viewLifecycleOwner, Observer {
            when(base)
            {
                "TRY" -> {
                    eur = it.rates.EUR
                    usd=it.rates.USD
                    gbp = it.rates.GBP
                }
                "USD" ->{
                    eur = it.rates.EUR
                    tl = it.rates.TRY
                    gbp = it.rates.GBP
                }
                "EUR" -> {
                    usd = it.rates.USD
                    tl = it.rates.TRY
                    gbp = it.rates.GBP
                }
                "GBP" ->{
                    eur = it.rates.EUR
                    tl = it.rates.TRY
                    usd = it.rates.USD
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
            when(rbCurrency)
            {
                0 -> {
                    euro = expense.toInt() * eur!!
                    dollar = expense.toInt() * usd!!
                    sterlin = expense.toInt() * gbp!!
                    turkLirasi = expense.toDouble()
                }
                1-> {
                    euro = expense.toInt() * eur!!
                    turkLirasi = expense.toInt() * tl!!
                    sterlin = expense.toInt() * gbp!!
                    dollar = expense.toDouble()
                }
                2 -> {
                    turkLirasi = expense.toInt() * tl!!
                    dollar = expense.toInt() * usd!!
                    sterlin = expense.toInt() * gbp!!
                    euro = expense.toDouble()
                }
                3 -> {
                    euro = expense.toInt() * eur!!
                    dollar = expense.toInt() * usd!!
                    turkLirasi = expense.toInt() * tl!!
                    sterlin = expense.toDouble()
                }
            }
            insertDataToDatabase(statement)
            val action = AddExpenseFragmentDirections.actionAddExpenseFragmentToHomeFragment(rbCurrency!!)
            findNavController().navigate(action)
            Toast.makeText(requireContext(), "Harcamanız Eklendi!",Toast.LENGTH_LONG).show()
        }

    }

    private fun insertDataToDatabase(statement: String) {
        expenseViewModel.addExpense(
            Expense(0,
                statement,
                turkLirasi!!,
                dollar!!,
                euro!!,
                sterlin!!,
                rbBillType!!,
                rbCurrency!!)
        )
    }

}
