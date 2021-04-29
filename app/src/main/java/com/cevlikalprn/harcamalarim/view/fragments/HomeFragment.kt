package com.cevlikalprn.harcamalarim.view.fragments

import android.content.SharedPreferences
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
import com.cevlikalprn.harcamalarim.model.CurrencyResponse
import com.cevlikalprn.harcamalarim.model.Expense
import com.cevlikalprn.harcamalarim.view.adapters.ExpenseAdapter
import com.cevlikalprn.harcamalarim.viewmodel.CurrencyViewModel
import com.cevlikalprn.harcamalarim.viewmodel.ExpenseViewModel
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class HomeFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var expenseViewModel: ExpenseViewModel
    private lateinit var currencyViewModel: CurrencyViewModel
    private val args: HomeFragmentArgs by navArgs()

    private lateinit var preferences: SharedPreferences
    private var base = "TRY"
    private var sum = 0.0
    private var currencyUnit = "₺"

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
            adapter.setData(it)
            showTotalAmountOfMoney(it)
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
                    expenseViewModel.getAllData()
                }
                R.id.btn_dolar -> {
                    preferences.edit().putInt("type", 1).apply()
                    expenseViewModel.getAllData()
                }
                R.id.btn_euro -> {
                    preferences.edit().putInt("type", 2).apply()
                    expenseViewModel.getAllData()
                }
                R.id.btn_sterlin -> {
                    preferences.edit().putInt("type", 3).apply()
                    expenseViewModel.getAllData()
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
            when(currencyType)
            {
                0 -> {
                    sum += item.turkLirasi
                    currencyUnit = "₺"
                }
                1 -> {
                    sum += item.dollar
                    currencyUnit = "$"
                }
                2 -> {
                    sum += item.euro
                    currencyUnit = "€"
                }
                3 -> {
                    sum += item.sterlin
                    currencyUnit = "£"
                }
            }
        }
        binding.tvTotalMoney.text = sum.toInt().toString()+currencyUnit
        sum = 0.0
    }

}