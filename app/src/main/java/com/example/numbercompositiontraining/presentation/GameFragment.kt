package com.example.numbercompositiontraining.presentation

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.numbercompositiontraining.databinding.FragmentGameBinding
import com.example.numbercompositiontraining.domain.entity.GameResult
import com.example.numbercompositiontraining.domain.entity.GameSettings
import com.example.numbercompositiontraining.domain.entity.Question

class GameFragment : Fragment() {

    private val args by navArgs<GameFragmentArgs>()

    private val viewModelFactory by lazy {
        GameViewModelFactory(args.level, requireActivity().application)
    }

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[GameViewModel::class.java]
    }

    private lateinit var gameSettings: GameSettings
    private lateinit var question: Question
    private var _binding: FragmentGameBinding? = null
    private val binding: FragmentGameBinding
        get() = _binding ?: throw RuntimeException("FragmentGameBinding == null")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        observeViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun launchGameFinishFragment(gameResult: GameResult) {
        findNavController().navigate(
            GameFragmentDirections
                .actionGameFragmentToGameFinishedFragment(gameResult)
        )
    }

    private fun observeViewModel() {

        viewModel.gameResult.observe(viewLifecycleOwner) {
            launchGameFinishFragment(it)
        }
    }

    private fun getColorByState(state: Boolean): Int {
        return if (state) {
            ContextCompat.getColor(
                requireContext(), android.R.color
                    .holo_green_light
            )
        } else {
            ContextCompat.getColor(
                requireContext(), android.R.color
                    .holo_red_light
            )
        }
    }


}