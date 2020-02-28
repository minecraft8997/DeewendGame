@file:JvmName("DesktopLauncher")
package ru.deewend.game.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import ru.deewend.game.Deewend
import java.awt.GraphicsEnvironment

const val fullscreen = false
const val vSyncEnabled = false

fun main() {
    val config = LwjglApplicationConfiguration()

    config.title = "Deewend Game: Special Edition"
    config.vSyncEnabled = vSyncEnabled
    config.backgroundFPS = 0
    config.foregroundFPS = 0

    config.width = if (fullscreen) GraphicsEnvironment.getLocalGraphicsEnvironment().defaultScreenDevice.displayMode.width else 1280
    config.height = if (fullscreen) GraphicsEnvironment.getLocalGraphicsEnvironment().defaultScreenDevice.displayMode.height else 720
    config.fullscreen = fullscreen

    LwjglApplication(Deewend(config.vSyncEnabled), config)
}