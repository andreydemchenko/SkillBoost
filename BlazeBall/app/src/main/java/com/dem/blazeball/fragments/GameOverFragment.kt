package com.dem.blazeball.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.dem.blazeball.R
import com.dem.blazeball.databinding.FragmentGameOverBinding

class GameOverFragment : Fragment() {

    private lateinit var binding: FragmentGameOverBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentGameOverBinding.inflate(layoutInflater)

        val score = arguments?.getInt("score") ?: 0

        val sharedPreferences = requireActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        var currentRecord = sharedPreferences.getInt("record", 0)
        if (score > currentRecord) {
            currentRecord = score
            val editor = sharedPreferences.edit()
            editor.putInt("record", currentRecord)
            editor.apply()
        }

        val formattedScore = String.format("%02d", score)
        binding.scoreValueTv.text = formattedScore
        binding.recordValueTv.text = currentRecord.toString()

        binding.buttonRestart.setOnClickListener {
            findNavController().apply {
                popBackStack(R.id.GameOverFragment, true)
                navigate(R.id.GameFragment)
            }
        }

        return binding.root
    }
}