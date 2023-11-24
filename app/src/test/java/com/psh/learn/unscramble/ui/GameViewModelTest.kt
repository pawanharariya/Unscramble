package com.psh.learn.unscramble.ui

import com.psh.learn.unscramble.data.MAX_NO_OF_WORDS
import com.psh.learn.unscramble.data.SCORE_INCREASE
import com.psh.learn.unscramble.data.getUnscrambledWord
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GameViewModelTest {
    // By default, before each test method is executed, JUnit creates a new instance of the test class.
    // Hence every test has a new viewModel instance
    private val viewModel = GameViewModel()

    @Test
    fun `GameViewModel initialisation should load the first word`() {
        val currentUiState = viewModel.uiStateFlow.value
        val unscrambledWord = getUnscrambledWord(currentUiState.currentScrambledWord)
        assertEquals(0, currentUiState.score)
        assertEquals(1, currentUiState.wordCount)
        assertNotEquals(unscrambledWord, currentUiState.currentScrambledWord)
        assertFalse(currentUiState.isGuessedWordWrong)
        assertFalse(currentUiState.isGameOver)
    }

    @Test
    fun `GameViewModel with correct word guessed updates score and error flag unset`() {
        var currentGameUiState = viewModel.uiStateFlow.value
        val correctWord = getUnscrambledWord(currentGameUiState.currentScrambledWord)
        viewModel.updateUserGuess(correctWord)
        viewModel.checkUserGuess()

        currentGameUiState = viewModel.uiStateFlow.value
        assertFalse(currentGameUiState.isGuessedWordWrong)
        assertEquals(20, currentGameUiState.score)
    }

    @Test
    fun `GameViewModel with incorrect word guess`() {
        val incorrectWord = "and"
        viewModel.updateUserGuess(incorrectWord)
        viewModel.checkUserGuess()

        val currentGameUiState = viewModel.uiStateFlow.value
        assertTrue(currentGameUiState.isGuessedWordWrong)
        assertEquals(0, currentGameUiState.score)
    }

    @Test
    fun `GameViewModel all words guessed game should be over`() {
        var expectedScore = 0
        var currentGameUiState = viewModel.uiStateFlow.value
        var correctWord = getUnscrambledWord(currentGameUiState.currentScrambledWord)
        repeat(MAX_NO_OF_WORDS) {
            expectedScore += SCORE_INCREASE
            viewModel.updateUserGuess(correctWord)
            viewModel.checkUserGuess()
            currentGameUiState = viewModel.uiStateFlow.value
            correctWord = getUnscrambledWord(currentGameUiState.currentScrambledWord)
            assertEquals(expectedScore, currentGameUiState.score)
        }
        assertTrue(currentGameUiState.isGameOver)
        assertEquals(MAX_NO_OF_WORDS, currentGameUiState.wordCount)
    }

    @Test
    fun `GameViewModel when word skipped score remains unchanged and word count increases`() {
        var currentGameUiState = viewModel.uiStateFlow.value
        val correctPlayerWord = getUnscrambledWord(currentGameUiState.currentScrambledWord)
        viewModel.updateUserGuess(correctPlayerWord)
        viewModel.checkUserGuess()
        currentGameUiState = viewModel.uiStateFlow.value
        val lastScore = currentGameUiState.score
        val lastWordCount = currentGameUiState.wordCount
        viewModel.skipWord()
        currentGameUiState = viewModel.uiStateFlow.value
        assertEquals(lastWordCount + 1, currentGameUiState.wordCount)
        assertEquals(lastScore, currentGameUiState.score)
    }
}