package com.example.numbercompositiontraining.presentation

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.numbercompositiontraining.R
import com.example.numbercompositiontraining.data.GameRepositoryImpl
import com.example.numbercompositiontraining.domain.entity.GameResult
import com.example.numbercompositiontraining.domain.entity.GameSettings
import com.example.numbercompositiontraining.domain.entity.Level
import com.example.numbercompositiontraining.domain.entity.Question
import com.example.numbercompositiontraining.domain.usecases.GenerateQuestionUseCase
import com.example.numbercompositiontraining.domain.usecases.GetGameSettingsUseCase

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var level: Level
    private lateinit var gameSettings: GameSettings

    private var countDownTimer: CountDownTimer? = null

    private val context = application
    private val repository = GameRepositoryImpl

    private val generateQuestionUseCase = GenerateQuestionUseCase(repository)
    private val getGameSettingsUseCase = GetGameSettingsUseCase(repository)


    private val _timerInGame = MutableLiveData<String>()
    val timerInGame: LiveData<String>
        get() = _timerInGame

    private val _question = MutableLiveData<Question>()
    val question: LiveData<Question>
        get() = _question

    private var countOfRightAnswers = 0
    private var countOfQuestions = 0

    private val _progressAnswers = MutableLiveData<String>()
    val progressAnswers: LiveData<String>
        get() = _progressAnswers

    private val _percentOfRightAnswers = MutableLiveData<Int>()
    val percentOfRightAnswers: LiveData<Int>
        get() = _percentOfRightAnswers

    private val _enoughCountOfRightAnswers = MutableLiveData<Boolean>()
    val enoughCountOfRightAnswers: LiveData<Boolean>
        get() = _enoughCountOfRightAnswers


    private val _enoughPercentOfRightAnswers = MutableLiveData<Boolean>()
    val enoughPercentOfRightAnswers: LiveData<Boolean>
        get() = _enoughPercentOfRightAnswers

    private val _minPercent = MutableLiveData<Int>()
    val minPercent: LiveData<Int>
        get() = _minPercent


    private val _gameResult = MutableLiveData<GameResult>()
    val gameResult: LiveData<Int>
        get() = _minPercent

    private fun updateProgress() {
        val percent = calculatePercentOfRightAnswers()
        _percentOfRightAnswers.value = percent
        _progressAnswers.value = String.format(
            context.resources.getString(
                R.string
                    .progress_answers
            ), countOfRightAnswers, gameSettings.minCountOfRightAnswers
        )
        _enoughCountOfRightAnswers.value = countOfRightAnswers >= gameSettings
            .minCountOfRightAnswers
        _enoughPercentOfRightAnswers.value = percent >= gameSettings.minPercentOfRightAnswers
    }

    private fun calculatePercentOfRightAnswers(): Int {
        return ((countOfRightAnswers / countOfQuestions.toDouble()) * 100).toInt()
    }


    fun startGame(level: Level) {
        getGameSettings(level)
        setTimer()
        generateQuestion()
    }

    private fun getGameSettings(level: Level) {
        this.level = level
        this.gameSettings = getGameSettingsUseCase(level)
        _minPercent.value = gameSettings.minPercentOfRightAnswers
    }

    fun chooseAnswers(number: Int) {
        checkAnswers(number)
        updateProgress()
        generateQuestion()
    }

    private fun checkAnswers(number: Int) {
        val rightAnswer = question.value?.rightAnswer
        if (number == rightAnswer) {
            countOfRightAnswers++
        }
        countOfQuestions++
    }

    private fun setTimer() {
        countDownTimer =
            object :
                CountDownTimer(
                    (gameSettings.gameTimeInSeconds * MILLIS_IN_SECONDS),
                    MILLIS_IN_SECONDS
                ) {
                override fun onTick(millisUntilFinished: Long) {
                    _timerInGame.value = formatTime(millisUntilFinished)
                }

                override fun onFinish() {
                    finishGame()
                }
            }
        countDownTimer?.start()
    }

    private fun formatTime(millisUntilFinished: Long): String {
        val seconds = millisUntilFinished / MILLIS_IN_SECONDS
        val minutes = seconds / SECONDS_IN_MINUTES
        val leftSeconds = seconds - (minutes * SECONDS_IN_MINUTES)
        return String.format("%02d:%02d", minutes, leftSeconds)
    }

    private fun generateQuestion() {
        _question.value = generateQuestionUseCase(gameSettings.maxSumValue)
    }

    private fun finishGame() {
        _gameResult.value = GameResult(
            enoughCountOfRightAnswers.value == true && enoughPercentOfRightAnswers.value
                    ==
                    true,
            countOfRightAnswers,
            countOfQuestions,
            gameSettings
        )
    }

    override fun onCleared() {
        super.onCleared()
        countDownTimer?.cancel()
    }

    companion object {
        private const val MILLIS_IN_SECONDS = 1000L
        private const val SECONDS_IN_MINUTES = 60
    }
}