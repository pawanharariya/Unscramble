package com.psh.learn.unscramble.ui

data class GameUiState(
    val currentScrambledWord: String = "",
    val isGuessedWordWrong: Boolean = false,
    val score: Int = 0,
    val wordCount: Int = 0,
    val isGameOver: Boolean = false
)