package com.example.numbercompositiontraining.domain.usecases

import com.example.numbercompositiontraining.domain.entity.Question
import com.example.numbercompositiontraining.domain.repository.GameRepository

class GenerateQuestionUseCase(private val repository: GameRepository) {

    operator fun invoke(maxSumValue: Int): Question {
        return repository.generateQuestion(maxSumValue, COUNT_OF_OPTIONS)
    }

    private companion object {
        private const val COUNT_OF_OPTIONS = 6
    }
}