package com.cevlikalprn.harcamalarim.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.cevlikalprn.harcamalarim.R
import com.cevlikalprn.harcamalarim.data.SharedPreferencesManager
import com.cevlikalprn.harcamalarim.databinding.FragmentHomeBinding
import com.cevlikalprn.harcamalarim.model.Expense
import com.cevlikalprn.harcamalarim.view.adapters.ExpenseAdapter
import com.cevlikalprn.harcamalarim.viewmodel.CurrencyViewModel
import com.cevlikalprn.harcamalarim.viewmodel.ExpenseViewModel

class HomeFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var expenseViewModel: ExpenseViewModel
    private lateinit var currencyViewModel: CurrencyViewModel
    private val args: HomeFragmentArgs by navArgs()

    private lateinit var itemList: List<Expense>
    private var base = "TRY"
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

        readTheUser()
        expenseViewModel = ViewModelProvider(this).get(ExpenseViewModel::class.java)
        currencyViewModel = ViewModelProvider(this).get(CurrencyViewModel::class.java)

        binding.tvUser.setOnClickListener(this)
        binding.btnAdd.setOnClickListener(this)
        binding.btnTurkLirasi.setOnClickListener(this)
        binding.btnSterlin.setOnClickListener(this)
        binding.btnEuro.setOnClickListener(this)
        binding.btnDolar.setOnClickListener(this)

        when(args.currencyType)
        {
            0 -> {
                base = "TRY"
                currencyViewModel.getRates(base)
            }
            1 ->{
                base = "USD"
                currencyViewModel.getRates(base)
            }
            2 -> {
                base = "EUR"
                currencyViewModel.getRates(base)
            }
            3 ->{
                base = "GBP"
                currencyViewModel.getRates(base)
            }
        }


        val adapter = ExpenseAdapter(requireContext())
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        expenseViewModel.getAllData()
        expenseViewModel.readAllData.observe(viewLifecycleOwner, Observer {
            itemList = it
            adapter.setData(it)

            for (item in itemList)
            {
                sum +=item.amountOfMoney
            }
            if(itemList.isNotEmpty())
            {
                when(itemList[0].currencyType){
                    0 -> binding.tvTotalMoney.text = sum.toInt().toString() + " ₺"
                    1 -> binding.tvTotalMoney.text = sum.toInt().toString() + " $"
                    2 -> binding.tvTotalMoney.text = sum.toInt().toString() + " €"
                    3 -> binding.tvTotalMoney.text = sum.toInt().toString() + " £"
                }
            }
            sum = 0.0
        })

        convertCurrenciesWithApi()

    }



    override fun onClick(view: View?) {
        if (view != null)
        {
            when (view.id)
            {
                R.id.tv_user -> findNavController().navigate(R.id.action_homeFragment_to_changeUserInfoFragment)
                R.id.btn_add -> findNavController().navigate(R.id.action_homeFragment_to_addExpenseFragment)
                R.id.btn_turk_lirasi -> {
                    base = "TRY"
                    currencyViewModel.getRates(base)
                }
                R.id.btn_dolar -> {
                    base = "USD"
                    currencyViewModel.getRates(base)
                }
                R.id.btn_euro -> {
                    base = "EUR"
                    currencyViewModel.getRates(base)
                }
                R.id.btn_sterlin -> {
                    base = "GBP"
                    currencyViewModel.getRates(base)
                }
            }
        }
    }


    private fun readTheUser() {
        val preferences = SharedPreferencesManager.getSharedPreferences(requireContext())
        val userName = preferences!!.getString("user_name", "İsminizi Girin")
        val gender = preferences.getInt("gender", 2)

        when (gender)
        {
            0 -> binding.tvUser.text = userName+" Bey"
            1 -> binding.tvUser.text = userName + " Hanım"
            else -> binding.tvUser.text = userName
        }

    }

    private fun convertCurrenciesWithApi() {
        currencyViewModel.getRates(base)
        currencyViewModel.myRates.observe(viewLifecycleOwner, Observer {
            if(it.isSuccessful)
            {
                when(base)
                {
                    "TRY" -> {
                        val usd = it.body()!!.rates.USD
                        val eur = it.body()!!.rates.EUR
                        val gbp = it.body()!!.rates.GBP
                        for(item in itemList)
                        {
                            if(item.currencyType != 0){ // Türk Lirası Değilse
                                when(item.currencyType)
                                {
                                    1 -> { //dolar
                                        val tl = item.amountOfMoney / usd
                                        item.amountOfMoney = tl
                                        item.currencyType = 0
                                        expenseViewModel.readAllData.value = itemList
                                    }
                                    2 -> { // euro
                                        val tl = item.amountOfMoney/ eur
                                        item.amountOfMoney = tl
                                        item.currencyType = 0
                                        expenseViewModel.readAllData.value = itemList
                                    }
                                    3 -> { // sterlin
                                        val tl = item.amountOfMoney / gbp
                                        item.amountOfMoney = tl
                                        item.currencyType = 0
                                        expenseViewModel.readAllData.value = itemList
                                    }
                                }
                            }
                        }
                    }

                    "USD" -> {
                        val gbp = it.body()!!.rates.GBP
                        val eur = it.body()!!.rates.EUR
                        val tl = it.body()!!.rates.TRY
                        for(item in itemList){
                            if(item.currencyType != 1){ // Dolar Değilse
                                when(item.currencyType){
                                    0 -> { //türk lirası
                                        val dollar = item.amountOfMoney / tl
                                        item.amountOfMoney = dollar
                                        item.currencyType = 1
                                        expenseViewModel.readAllData.value = itemList
                                    }
                                    2 -> { // euro
                                        val dollar = item.amountOfMoney / eur
                                        item.amountOfMoney = dollar
                                        item.currencyType = 1
                                        expenseViewModel.readAllData.value = itemList
                                    }
                                    3 -> { // sterlin
                                        val dollar = item.amountOfMoney / gbp
                                        item.amountOfMoney = dollar
                                        item.currencyType = 1
                                        expenseViewModel.readAllData.value = itemList
                                    }
                                }
                            }
                        }
                    }

                    "EUR" ->{
                        val gbp = it.body()!!.rates.GBP
                        val usd = it.body()!!.rates.USD
                        val tl = it.body()!!.rates.TRY
                        for(item in itemList){
                            if(item.currencyType != 2){ // Euro Değilse
                                when(item.currencyType){
                                    0 -> { //türk lirası
                                        val euro = item.amountOfMoney / tl
                                        item.amountOfMoney = euro
                                        item.currencyType = 2
                                        expenseViewModel.readAllData.value = itemList
                                    }
                                    1 -> { // dolar
                                        val euro = item.amountOfMoney / usd
                                        item.amountOfMoney = euro
                                        item.currencyType = 2
                                        expenseViewModel.readAllData.value = itemList
                                    }
                                    3 -> { // sterlin
                                        val euro = item.amountOfMoney / gbp
                                        item.amountOfMoney = euro
                                        item.currencyType = 2
                                        expenseViewModel.readAllData.value = itemList
                                    }
                                }
                            }
                        }
                    }

                    "GBP" ->{
                        val eur = it.body()!!.rates.EUR
                        val usd = it.body()!!.rates.USD
                        val tl = it.body()!!.rates.TRY
                        for(item in itemList){
                            if(item.currencyType != 3){ // Sterlin Değilse
                                when(item.currencyType){
                                    0 -> { //türk lirası
                                        val gbp = item.amountOfMoney / tl
                                        item.amountOfMoney = gbp
                                        item.currencyType = 3
                                        expenseViewModel.readAllData.value = itemList
                                    }
                                    1 -> { // dolar
                                        val gbp = item.amountOfMoney / usd
                                        item.amountOfMoney = gbp
                                        item.currencyType = 3
                                        expenseViewModel.readAllData.value = itemList
                                    }
                                    2 -> { // euro
                                        val gbp = item.amountOfMoney / eur
                                        item.amountOfMoney = gbp
                                        item.currencyType = 3
                                        expenseViewModel.readAllData.value = itemList
                                    }
                                }
                            }
                        }
                    }

                }
            }
        })
    }


}