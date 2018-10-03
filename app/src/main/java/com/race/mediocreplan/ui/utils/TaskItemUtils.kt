package com.race.mediocreplan.ui.utils

import com.race.mediocreplan.R

abstract class TaskItemUtils {
    companion object {
        @JvmStatic
        fun getCardColor(cardIdentifier: String): Int {
            return when (cardIdentifier) {
                "RedTaskCard" -> R.color.redTaskCard
                "CyanTaskCard" -> R.color.cyanTaskCard
                "WhiteTaskCard" -> R.color.whiteTaskCard
                else -> R.color.redTaskCard
            }
        }

        fun getButtonColor(cardIdentifier: String): Int {
            return when (cardIdentifier) {
                "RedTaskCard" -> R.color.redTaskButton
                "CyanTaskCard" -> R.color.cyanTaskButton
                "WhiteTaskCard" -> R.color.whiteTaskButton
                else -> R.color.redTaskCard
            }
        }

        fun getTextColor(cardIdentifier: String): Int {
            return when (cardIdentifier) {
                "RedTaskCard" -> R.color.redTaskText
                "CyanTaskCard" -> R.color.cyanTaskText
                "WhiteTaskCard" -> R.color.whiteTaskText
                else -> R.color.redTaskText
            }
        }
    }
}