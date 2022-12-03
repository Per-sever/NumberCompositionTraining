package com.example.numbercompositiontraining.presentation

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.numbercompositiontraining.databinding.FragmentGameBinding
import com.example.numbercompositiontraining.domain.entity.GameResult
import com.example.numbercompositiontraining.domain.entity.GameSettings
import com.example.numbercompositiontraining.domain.entity.Level
import com.example.numbercompositiontraining.domain.entity.Question

class GameFragment : Fragment() {

    private val args by navArgs<GameFragmentArgs>()

    private val viewModelFactory by lazy {
        GameViewModelFactory(args.level, requireActivity().application)
    }

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[GameViewModel::class.java]
    }

    private val tvOptions by lazy {
        mutableListOf<TextView>().apply {
            add(binding.tvOption1)
            add(binding.tvOption2)
            add(binding.tvOption3)
            add(binding.tvOption4)
            add(binding.tvOption5)
            add(binding.tvOption6)
        }
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
        viewModel.timerInGame.observe(viewLifecycleOwner) {
            binding.tvTimer.text = it
        }

        viewModel.question.observe(viewLifecycleOwner) {
            binding.tvSum.text = it.sum.toString()
            binding.tvVisibleNumber.text = it.visibleNumber.toString()
            for (i in 0 until tvOptions.size) {
                tvOptions[i].text = it.options[i].toString()
                tvOptions[i].setOnClickListener() {
                    val answer = tvOptions[i].text.toString()
                    viewModel.chooseAnswers(answer.toInt())
                }
            }
        }

        viewModel.progressAnswers.observe(viewLifecycleOwner) {
            binding.tvAnswersProgress.text = it
        }

        viewModel.minPercent.observe(viewLifecycleOwner) {
            binding.progressBar.secondaryProgress = it
        }

        viewModel.percentOfRightAnswers.observe(viewLifecycleOwner) {
            binding.progressBar.setProgress(it, true)
        }

        viewModel.enoughCountOfRightAnswers.observe(viewLifecycleOwner) {
            binding.tvAnswersProgress.setTextColor(getColorByState(it))
        }
        viewModel.enoughPercentOfRightAnswers.observe(viewLifecycleOwner) {
            binding.progressBar.progressTintList = ColorStateList.valueOf(getColorByState(it))
        }
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