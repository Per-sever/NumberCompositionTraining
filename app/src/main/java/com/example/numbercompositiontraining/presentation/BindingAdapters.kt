package com.example.numbercompositiontraining.presentation

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.numbercompositiontraining.R
import com.example.numbercompositiontraining.domain.entity.GameResult


@BindingAdapter("resultSmile")
fun bindResultSmile(imageView: ImageView, winner: Boolean) {
    imageView.setImageResource(getSmileResId(winner))
}

private fun getSmileResId(winner: Boolean): Int {
    return if (winner) {
        R.drawable.ic_smile
    } else {
        R.drawable.ic_sad_smile
    }
}

@BindingAdapter("requiredAnswers")
fun bindRequiredAnswers(textView: TextView, score: Int) {
    textView.text = String.format(
        textView.context.getString(
            R.string.required_answers
        ), score
    )
}

@BindingAdapter("yourScore")
fun bindYourScore(textView: TextView, count: Int) {
    textView.text = String.format(textView.context.getString(R.string.your_score), count)
}

@BindingAdapter("requiredPercentage")
fun bindRequiredPercentage(textView: TextView, percent: Int) {
    textView.text = String.format(textView.context.getString(R.string.required_percents), percent)
}

@BindingAdapter("yourPercentageOfAnswers")
fun bindYourPercentageOfAnswers(textView: TextView, gameResult: GameResult) {
    val result = with(gameResult) {
        if (countOfQuestions == 0) {
            0
        } else {
            (countOfRightAnswers / countOfQuestions.toDouble() * 100).toInt()
        }
    }
    textView.text = String.format(
        textView.context.getString(R.string.score_percentage),
        result
    )
}


