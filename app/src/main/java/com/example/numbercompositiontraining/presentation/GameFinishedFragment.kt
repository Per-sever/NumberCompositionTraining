package com.example.numbercompositiontraining.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.numbercompositiontraining.R
import com.example.numbercompositiontraining.databinding.FragmentGameFinishedBinding
import com.example.numbercompositiontraining.domain.entity.GameResult

class GameFinishedFragment : Fragment() {

    private lateinit var gameResult: GameResult

    private var _binding: FragmentGameFinishedBinding? = null
    private val binding: FragmentGameFinishedBinding
        get() = _binding ?: throw RuntimeException("FragmentGameFinishedBinding == null")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }


    private fun retryGame() {
        findNavController().popBackStack()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListener()
        bindViews()
    }

    private fun bindViews() {
        with(binding) {
            binding.ivResultSmile.setBackgroundResource(getSmileId())
            tvRequiredAnswers.text = String.format(
                requireActivity().resources.getString(
                    R.string.required_percents
                ), gameResult.gameSettings.minPercentOfRightAnswers
            )
            tvYourScore.text = String.format(
                requireActivity().resources.getString(
                    R.string.your_score
                ), gameResult.countOfRightAnswers
            )
            tvRequiredPercentage.text = String.format(
                requireActivity().resources.getString(
                    R.string.required_answers
                ), gameResult.gameSettings.minCountOfRightAnswers
            )
            tvScorePercentage.text = String.format(
                requireActivity().resources.getString(
                    R.string.score_percentage
                ), getPercentOfRightAnswers()
            )
        }
    }

    private fun getSmileId(): Int {
        return if (gameResult.winner) {
            R.drawable.ic_smile
        } else {
            R.drawable.ic_sad_smile
        }
    }

    private fun getPercentOfRightAnswers() = with(gameResult) {
        if (countOfQuestions == 0) {
            0
        } else {
            (countOfRightAnswers / countOfQuestions.toDouble() * 100).toInt()
        }
    }

    private fun setupClickListener() {
        binding.buttonRetry.setOnClickListener {
            retryGame()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun parseArgs() {
        requireArguments().getParcelable<GameResult>(KEY_GAME_RESULT)?.let {
            gameResult = it
        }
    }

    companion object {

        const val KEY_GAME_RESULT = "game_result"

        fun newInstance(gameResult: GameResult): GameFinishedFragment {
            return GameFinishedFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_GAME_RESULT, gameResult)
                }
            }
        }
    }
}