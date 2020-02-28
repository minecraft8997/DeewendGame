@file:JvmName("Deewend")
package ru.deewend.game

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g3d.Environment
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight
import ru.deewend.game.DeewendHelper.convertDToS
import ru.deewend.game.DeewendHelper.convertSToD
import ru.deewend.game.logic.DeewendWorld
import ru.deewend.game.textures.DeewendTexturesHelper
import ru.deewend.game.util.DeewendUtils

class Deewend (vSyncEnabled: Boolean) : Game() {
    companion object {
        const val vLine = "Deewend Game v.0.0.0.0.9s (Special Edition) (Press F3 to hide) (With love, Vanya :3)"
    }

    @Volatile
    var vSyncEnabled: Boolean = vSyncEnabled
        private set (value) {
            Gdx.graphics.setVSync(value)
            field = value
        }

    @Volatile
    var showInfo = true
        private set

    private var currentLine: Int = -1
        get () {
            if (++field >= numOfInfoLines) field = 0
            return field
        }
        set(_) {}

    private val skyColor = DeewendHelper.GLRGB(17, 137, 217)
    private val mask = GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT

    private val numOfInfoLines = 6
    private val infoLinesY = ArrayList<Float>()

    private val environment = Environment()

    @Volatile
    private lateinit var font: BitmapFont
    @Volatile
    private lateinit var modelBatch: ModelBatch
    @Volatile
    private lateinit var spriteBatch: SpriteBatch
    @Volatile
    private lateinit var cameraController: DeewendFirstPersonCameraController
    @Volatile
    lateinit var world: DeewendWorld
        private set

    @Volatile
    private lateinit var camera: PerspectiveCamera
    @Suppress("unused") var fieldOfView: Float = -1f
        set (value) {
            if (!::camera.isInitialized) return
            camera.fieldOfView = value
            field = value
        }
        get() {
            return camera.fieldOfView
        }

    override fun create() {
        font = BitmapFont()

        modelBatch = ModelBatch()
        spriteBatch = SpriteBatch()

        environment.set(ColorAttribute(ColorAttribute.AmbientLight, 0.5f, 0.5f, 0.5f, 1f))
        environment.add(DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.5f))
        environment.add(DirectionalLight().set(0.2f, 0.2f, 0.2f, 1f, 0.8f, 0.5f))

        camera = PerspectiveCamera(60f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        DeewendHelper.init()

        repeat (numOfInfoLines) {
            infoLinesY += DeewendHelper.nextLineY
        }

        camera.position.set(convertDToS(DeewendWorld.worldSize / 2f), convertDToS(4f), convertDToS(DeewendWorld.worldSize / 2f))
        camera.near = 1f
        camera.far = 300f
        camera.update()

        cameraController = DeewendFirstPersonCameraController(this, camera)

        Gdx.input.inputProcessor = cameraController
        Gdx.input.isCursorCatched = true

        DeewendTexturesHelper.loadTexturePack(DeewendUtils.readFirstLine(DeewendHelper.pathToCpnTxt), this, false)
        world = DeewendWorld(this)
        world.init()
    }

    fun changeVSyncSettings() {
        if (vSyncEnabled) {
            vSyncEnabled = false
            return
        }

        vSyncEnabled = true
    }

    fun changeShowInfoSettings() {
        if (showInfo) {
            showInfo = false
            return
        }

        showInfo = true
    }

    override fun render() {
        cameraController.update()

        clearDisplay()


        modelBatch.begin(camera)
        world.render(modelBatch, environment)
        modelBatch.end()

        if (showInfo) {
            spriteBatch.begin()
            font.draw(spriteBatch, vLine, 2f, infoLinesY[currentLine])
            font.draw(spriteBatch, "FPS: ${Gdx.graphics.framesPerSecond}", 2f, infoLinesY[currentLine])
            font.draw(spriteBatch, "RAM (available): ~${Runtime.getRuntime().totalMemory() / 1000 / 1000}MB", 2f, infoLinesY[currentLine])
            font.draw(spriteBatch, "XYZ: ${convertSToD(camera.position.x)}, ${convertSToD(camera.position.y)}, ${convertSToD(camera.position.z)}", 2f, infoLinesY[currentLine])
            font.draw(spriteBatch, "vSync: ${if (vSyncEnabled) "ON" else "OFF"} (press V to turn it on/off)", 2f, infoLinesY[currentLine])
            font.draw(spriteBatch, "World size: ${world.getTerrain(this).size} block(s)", 2f, infoLinesY[currentLine])
            spriteBatch.end()
        }
    }

    private fun clearDisplay () {
        Gdx.gl.glClearColor(skyColor.red, skyColor.green, skyColor.blue, skyColor.alpha)
        Gdx.gl.glClear(mask)
    }

    override fun dispose() {
        modelBatch.dispose()
        spriteBatch.dispose()
        font.dispose()
        DeewendTexturesHelper.dispose()
        world.dispose()
    }
}