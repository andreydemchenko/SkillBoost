package com.dem.blazeball.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.dem.blazeball.R
import com.dem.blazeball.databinding.FragmentInitialBinding

class InitialFragment : Fragment() {

    private lateinit var binding: FragmentInitialBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentInitialBinding.inflate(layoutInflater)

        binding.buttonStart.setOnClickListener {
            findNavController().navigate(R.id.action_InitialFragment_to_GameFragment)
        }

        return binding.root
    }

}