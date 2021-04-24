package com.cevlikalprn.harcamalarim.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cevlikalprn.harcamalarim.R
import com.cevlikalprn.harcamalarim.data.SharedPreferencesManager
import com.cevlikalprn.harcamalarim.databinding.FragmentHomeBinding
import com.cevlikalprn.harcamalarim.model.Expense
import com.cevlikalprn.harcamalarim.view.adapters.ExpensesAdapter
import com.cevlikalprn.harcamalarim.viewmodel.ExpenseViewModel

class HomeFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: ExpenseViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvUser.setOnClickListener(this)
        binding.btnAdd.setOnClickListener(this)

        readTheUser()

        val adapter = ExpensesAdapter(requireContext())
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel = ViewModelProvider(this).get(ExpenseViewModel::class.java)

        viewModel.readAllData.observe(viewLifecycleOwner, Observer {
            adapter.setData(it)
        })

    }

    override fun onClick(view: View?) {
        if (view != null)
        {
            when (view.id)
            {
                R.id.tv_user -> findNavController().navigate(R.id.action_homeFragment_to_changeUserInfoFragment)
                R.id.btn_add -> findNavController().navigate(R.id.action_homeFragment_to_addExpenseFragment)
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

}