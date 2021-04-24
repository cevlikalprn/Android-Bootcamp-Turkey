package com.cevlikalprn.harcamalarim.view.fragments.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.navigation.fragment.findNavController
import com.cevlikalprn.harcamalarim.R
import com.cevlikalprn.harcamalarim.data.SharedPreferencesManager
import com.cevlikalprn.harcamalarim.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {

    private lateinit var binding: FragmentSplashBinding
    private var finished: Boolean? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentSplashBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onBoardingFinished()

        val splashAnimation = AnimationUtils.loadAnimation(requireContext(),
            R.anim.splash_animation
        )
        binding.imageView.animation = splashAnimation
        binding.textView3.animation = splashAnimation

        splashAnimation.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                Handler(Looper.getMainLooper()).postDelayed({

                    if(finished != null)
                    {
                        when(finished)
                        {
                            false -> findNavController().navigate(R.id.action_splashFragment_to_viewPagerFragment)
                            true -> findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
                        }
                    }
                },1000)
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })
    }

    private fun onBoardingFinished()
    {
        val preferences = SharedPreferencesManager.getSharedPreferences(requireContext())
        finished = preferences!!.getBoolean("finished",false)

    }

}


