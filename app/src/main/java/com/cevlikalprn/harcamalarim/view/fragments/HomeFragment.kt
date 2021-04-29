package com.cevlikalprn.harcamalarim.view.fragments

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.cevlikalprn.harcamalarim.R
import com.cevlikalprn.harcamalarim.data.SharedPreferencesManager
import com.cevlikalprn.harcamalarim.databinding.FragmentHomeBinding
import com.cevlikalprn.harcamalarim.model.Expense
import com.cevlikalprn.harcamalarim.util.Constants
import com.cevlikalprn.harcamalarim.view.adapters.ExpenseAdapter
import com.cevlikalprn.harcamalarim.viewmodel.CurrencyViewModel
import com.cevlikalprn.harcamalarim.viewmodel.ExpenseViewModel
import kotlinx.coroutines.currentCoroutineContext

class HomeFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var expenseViewModel: ExpenseViewModel
    private lateinit var currencyViewModel: CurrencyViewModel
    private val args: HomeFragmentArgs by navArgs()

    private lateinit var itemList: List<Expense>
    private lateinit var dollarList: List<Expense>
    private lateinit var euroList: List<Expense>
    private lateinit var sterlinList: List<Expense>

    private lateinit var preferences: SharedPreferences
    private var sum = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        expenseViewModel = ViewModelProvider(this).get(ExpenseViewModel::class.java)
        currencyViewModel = ViewModelProvider(this).get(CurrencyViewModel::class.java)
        binding.tvUser.setOnClickListener(this)
        binding.btnAdd.setOnClickListener(this)
        binding.btnTurkLirasi.setOnClickListener(this)
        binding.btnSterlin.setOnClickListener(this)
        binding.btnEuro.setOnClickListener(this)
        binding.btnDolar.setOnClickListener(this)

        readTheUser()

        when(args.currencyType)
        {
            0 -> preferences.edit().putInt("type", 0).apply()
            1 -> preferences.edit().putInt("type", 1).apply()
            2 -> preferences.edit().putInt("type", 2).apply()
            3 -> preferences.edit().putInt("type", 3).apply()
        }



        val adapter = ExpenseAdapter(requireContext())
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        expenseViewModel.getAllData().observe(viewLifecycleOwner, Observer {
            itemList=it
            adapter.setData(it)
            showTotalAmountOfMoney(it)
        })

        currencyViewModel.getRates(Constants.base).observe(viewLifecycleOwner, Observer {
            val usd =it.rates.USD
            val eur = it.rates.EUR
            val gbp = it.rates.GBP

            if(itemList.isNotEmpty()){
                for(item in itemList)
                {
                    val type = preferences.getInt("type",0)
                    val currencyType = item.currencyType

                    when(type)
                    {
                        0 -> { // Türk Lirası
                            if(currencyType != 0)
                            {
                                convertToTurkLirasi(item, usd, eur, gbp)
                                expenseViewModel.readAllData.value = itemList
                            }
                        }
                        1 -> { // Dolar
                            if(currencyType != 1){
                                if(currencyType != 0)
                                {
                                    convertToTurkLirasi(item,usd,eur,gbp)
                                }
                                val dollar = item.amountOfMoney * usd
                                item.amountOfMoney = dollar
                                item.currencyType = 1
                                expenseViewModel.readAllData.value = itemList
                            }
                        }
                        2 -> { // Euro
                            if(currencyType != 2) {
                                if(currencyType != 0)
                                {
                                    convertToTurkLirasi(item,usd,eur,gbp)
                                }
                                val euro = item.amountOfMoney * eur
                                item.amountOfMoney = euro
                                item.currencyType = 2
                                expenseViewModel.readAllData.value = itemList
                            }
                        }
                        3 -> { // Sterlin
                            if(currencyType != 3){
                                if(currencyType != 0)
                                {
                                    convertToTurkLirasi(item,usd,eur,gbp)
                                }
                                val sterlin = item.amountOfMoney * gbp
                                item.amountOfMoney = sterlin
                                item.currencyType = 3
                                expenseViewModel.readAllData.value = itemList
                            }
                        }
                    }
                }
            }

        })

    }

    override fun onClick(view: View?) {
        if (view != null)
        {
            when (view.id)
            {
                R.id.tv_user -> findNavController().navigate(R.id.action_homeFragment_to_changeUserInfoFragment)
                R.id.btn_add -> findNavController().navigate(R.id.action_homeFragment_to_addExpenseFragment)
                R.id.btn_turk_lirasi -> {
                    preferences.edit().putInt("type", 0).apply()
                    currencyViewModel.getRates(Constants.base)
                }
                R.id.btn_dolar -> {
                    preferences.edit().putInt("type", 1).apply()
                    currencyViewModel.getRates(Constants.base)
                }
                R.id.btn_euro -> {
                    preferences.edit().putInt("type", 2).apply()
                    currencyViewModel.getRates(Constants.base)
                }
                R.id.btn_sterlin -> {
                    preferences.edit().putInt("type", 3).apply()
                    currencyViewModel.getRates(Constants.base)
                }
            }
        }
    }

    private fun readTheUser() {
        preferences = SharedPreferencesManager.getSharedPreferences(requireContext())!!
        val userName = preferences!!.getString("user_name", "İsminizi Girin")
        val gender = preferences.getInt("gender", 2)

        when (gender)
        {
            0 -> binding.tvUser.text = userName+" Bey"
            1 -> binding.tvUser.text = userName + " Hanım"
            else -> binding.tvUser.text = userName
        }

    }

    private fun showTotalAmountOfMoney(itemList: List<Expense>)
    {
        val currencyType = preferences.getInt("type",0)
        for(item in itemList){
            sum += item.amountOfMoney
        }
            when(currencyType)
            {
                0 -> binding.tvTotalMoney.text = sum.toInt().toString() +"₺"
                1 -> binding.tvTotalMoney.text = sum.toInt().toString() +"$"
                2 -> binding.tvTotalMoney.text = sum.toInt().toString() +"€"
                3 -> binding.tvTotalMoney.text = sum.toInt().toString() +"£"
                else -> binding.tvTotalMoney.text = sum.toInt().toString() +"₺"
            }
            sum = 0.0
    }

    private fun convertToTurkLirasi(item: Expense, usd: Double, eur: Double, gbp: Double)
    {
        when(item.currencyType)
        {
            1 -> {
                val turkLirasi = item.amountOfMoney / usd
                item.amountOfMoney = turkLirasi
                item.currencyType = 0
            }
            2 -> {
                val turkLirasi = item.amountOfMoney / eur
                item.amountOfMoney = turkLirasi
                item.currencyType = 0
            }
            3 -> {
                val turkLirasi = item.amountOfMoney / gbp
                item.amountOfMoney = turkLirasi
                item.currencyType = 0
            }
        }
    }

}