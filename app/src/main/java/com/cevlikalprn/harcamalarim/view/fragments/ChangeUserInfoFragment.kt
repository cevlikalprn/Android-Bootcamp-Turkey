package com.cevlikalprn.harcamalarim.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.cevlikalprn.harcamalarim.R
import com.cevlikalprn.harcamalarim.data.SharedPreferencesManager
import com.cevlikalprn.harcamalarim.databinding.FragmentChangeUserInfoBinding


class ChangeUserInfoFragment : Fragment() {

    private lateinit var mBinding: FragmentChangeUserInfoBinding
    private var gender: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentChangeUserInfoBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.btnSaveUserInfo.setOnClickListener {
            saveUser()
        }

        mBinding.rgGender.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener{
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                when(checkedId)
                {
                    R.id.rb_gender_man -> gender = 0 // man selected
                    R.id.rb_gender_woman ->  gender = 1 // woman selected
                    R.id.rb_no_gender -> gender = 2 // no gender selected
                }
            }

        })

    }

    private fun saveUser()
    {
        val userName = mBinding.etName.text.toString()

        if(userName == "" || gender == null)
        {
            Toast.makeText(requireContext(), "Lütfen Boşlukları Doldurunuz", Toast.LENGTH_LONG).show()
        }
        else
        {
            //Save User
            val preferences = SharedPreferencesManager.getSharedPreferences(requireContext())
            preferences!!.edit().putString("user_name",userName).apply()
            preferences.edit().putInt("gender", gender!!).apply()

            findNavController().navigate(R.id.action_changeUserInfoFragment_to_homeFragment)
        }

    }

}