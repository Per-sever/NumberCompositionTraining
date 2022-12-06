package com.example.numbercompositiontraining.presentation

import android.content.Context
import android.content.res.ColorStateList
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.example.numbercompositiontraining.R
import com.example.numbercompositiontraining.domain.entity.GameResult


interface OnOptionClickListener {
    fun onOptionClick(option: Int)
}

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


@BindingAdapter("numberAsString")
fun numberAsString(textView: TextView, number: Int) {
    textView.text = number.toString()
}


@BindingAdapter("enoughCountOfRightAnswers")
fun bindEnoughCountOfRightAnswers(textView: TextView, enough: Boolean) {
    textView.setTextColor(getColorByState(textView.context, enough))
}

@BindingAdapter("enoughPercentOfRightAnswers")
fun bindEnoughPercentOfRightAnswers(progressBar: ProgressBar, enough: Boolean) {
    progressBar.progressTintList = ColorStateList.valueOf(
        getColorByState(
            progressBar.context,
            enough
        )
    )
}

private fun getColorByState(context: Context, state: Boolean): Int {
    return if (state) {
        ContextCompat.getColor(
            context, android.R.color
                .holo_green_light
        )
    } else {
        ContextCompat.getColor(
            context, android.R.color
                .holo_red_light
        )
    }
}

@BindingAdapter("onOptionClickListener")
fun bindOnOptionClickListener(textView: TextView, clickListener: OnOptionClickListener) {
    textView.setOnClickListener {
        clickListener.onOptionClick(textView.text.toString().toInt())
    }
}

