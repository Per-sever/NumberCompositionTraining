package com.example.numbercompositiontraining.domain.repository

import com.example.numbercompositiontraining.domain.entity.GameSettings
import com.example.numbercompositiontraining.domain.entity.Level
import com.example.numbercompositiontraining.domain.entity.Question

interface GameRepository {
    fun generateQuestion(
        maxSumValue: Int,
        countOfOptions: Int
    ): Question

    fun getGameSettings(level: Level): GameSettings
}