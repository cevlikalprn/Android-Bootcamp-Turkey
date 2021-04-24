package com.cevlikalprn.harcamalarim.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.cevlikalprn.harcamalarim.R
import com.cevlikalprn.harcamalarim.databinding.FragmentShowTheExpenseBinding

class ShowTheExpenseFragment : Fragment() {

    private val args: ShowTheExpenseFragmentArgs by navArgs()
    private lateinit var binding: FragmentShowTheExpenseBinding
    private var expenseId = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShowTheExpenseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        expenseId = args.expenseID
        val billType = args.billType
        val statement = args.statement
        val amountOfMoney = args.amountOfMoney

        binding.txtStatementType.text = statement
        binding.txtAmountOfMoney.text = amountOfMoney.toString()
        setImage(billType)
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