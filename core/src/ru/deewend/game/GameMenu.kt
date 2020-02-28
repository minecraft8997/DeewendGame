package ru.deewend.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input

object GameMenu {
    private const val TITLE = "Deewend Game Menu"
    private const val TEXT = "Deewend Game Menu"
    private const val HINT = "1"

    @Volatile var hasResponseMessage = false
    @Volatile var responseMessage: String? = null
    @Volatile var removeMessageBy = System.currentTimeMillis()

    private object Menu : Input.TextInputListener {
        override fun input (text: String) {
            try {

            } catch (t: Throwable) {
                responseMessage = "Invalid input. Try again."
                hasResponseMessage = true
                removeMessageBy = System.currentTimeMillis() + 5000
            }
        }

        override fun canceled() {}
    }

    fun call () {
        Gdx.input.getTextInput(Menu, TITLE, TEXT, HINT)
    }

    private fun handleUserInput (actionId: Byte) {

    }
}