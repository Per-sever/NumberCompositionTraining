package com.example.numbercompositiontraining.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.numbercompositiontraining.R
import com.example.numbercompositiontraining.databinding.FragmentGameBinding
import com.example.numbercompositiontraining.domain.entity.GameResult
import com.example.numbercompositiontraining.domain.entity.GameSettings
import com.example.numbercompositiontraining.domain.entity.Level
import com.example.numbercompositiontraining.domain.entity.Question

class GameFragment : Fragment() {

    private lateinit var level: Level
    private lateinit var viewModel: GameViewModel
    private lateinit var gameSettings: GameSettings
    private lateinit var question: Question
    private var _binding: FragmentGameBinding? = null
    private val binding: FragmentGameBinding
        get() = _binding ?: throw RuntimeException("FragmentGameBinding == null")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
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
        viewModel = ViewModelProvider(this)[GameViewModel::class.java]
        observeViewModel()
        viewModel.startGame(level)
//        binding.tvSum.setOnClickListener {
//            launchGameFinishFragment(
//                GameResult(
//                    false,
//                    10,
//                    18,
//                    GameSettings(
//                        1,
//                        3,
//                        4,
//                        5
//                    )
//                )
//            )
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun parseArgs() {
        requireArguments().getParcelable<Level>(KEY_LEVEL)?.let {
            level = it
        }
    }

    private fun launchGameFinishFragment(gameResult: GameResult) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, GameFinishedFragment.newInstance(gameResult))
            .addToBackStack(null)
            .commit()
    }

    private fun observeViewModel() {
        viewModel.timerInGame.observe(viewLifecycleOwner) {
            binding.tvTimer.text = it
        }

        viewModel.question.observe(viewLifecycleOwner) {
            binding.tvSum.text = it.visibleNumber.toString()

        }
    }

    companion object {

        private const val KEY_LEVEL = "level"
        const val NAME = "GameFragment"

        fun newInstance(level: Level): GameFragment {
            return GameFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_LEVEL, level)
                }
            }
        }
    }

}