package ru.deewend.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.PixmapIO
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.ScreenUtils
import ru.deewend.game.DeewendHelper.convertSToD
import ru.deewend.game.util.DeewendBlockPosition
import java.io.File
import java.util.zip.Deflater

class DeewendFirstPersonCameraController (private val deewend: Deewend, private val camera: Camera) : FirstPersonCameraController(camera) {
    companion object {
        //const val LEFT_CLICK = 0
        //const val RIGHT_CLICK = 1

        const val velocity = 5f
    }

    private val tmp = Vector3()
    private val lastCameraPosition = Vector3 (camera.position)

    init {
        setDegreesPerPixel(0.08f)
        setVelocity(15f)
    }

    override fun update (deltaTime: Float) {
        if (!(camera.position.x == lastCameraPosition.x && camera.position.y == lastCameraPosition.y && camera.position.z == lastCameraPosition.z)) {
            val x = convertSToD(camera.position.x).toInt()
            val y = convertSToD(camera.position.y).toInt()
            val z = convertSToD(camera.position.z).toInt()

            if (DeewendBlockPosition (x, y, z) in deewend.world.getIndexedPositions(deewend)) {
                //camera is in the block

                camera.position.x = lastCameraPosition.x
                camera.position.y = lastCameraPosition.y
                camera.position.z = lastCameraPosition.z

                return
            }

            lastCameraPosition.x = camera.position.x
            lastCameraPosition.y = camera.position.y
            lastCameraPosition.z = camera.position.z
        }

        if (Gdx.input.isKeyPressed(Keys.SPACE) && !Gdx.input.isKeyPressed(Keys.Q)) {
            tmp.set(camera.up).nor().scl(deltaTime * velocity)
            camera.position.add(tmp)
        }

        if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) && !Gdx.input.isKeyPressed(Keys.E)) {
            tmp.set(camera.up).nor().scl(-deltaTime * velocity)
            camera.position.add(tmp)
        }

        super.update(deltaTime)
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        touchDragged(screenX, screenY, 0)
        return super.mouseMoved(screenX, screenY)
    }

    override fun keyDown(keycode: Int): Boolean {
        when (keycode) {
            Keys.ESCAPE -> {
                Gdx.app.exit()
            }

            Keys.V -> {
                deewend.changeVSyncSettings()
            }

            Keys.F2 -> {
                val pixmap: Pixmap = ScreenUtils.getFrameBufferPixmap(0, 0, Gdx.graphics.width, Gdx.graphics.height)
                try {
                    val d = File("${DeewendHelper.deewendHome}/screenshots")
                    if (!d.exists()) d.mkdir()
                    val f = File("${DeewendHelper.deewendHome}/screenshots/${System.currentTimeMillis()}.png")
                    f.createNewFile()
                    PixmapIO.writePNG(FileHandle(f), pixmap, Deflater.DEFAULT_COMPRESSION, true)
                    pixmap.dispose()
                } catch (t: Throwable) {
                    println(t.message)
                }
            }

            Keys.F3 -> {
                deewend.changeShowInfoSettings()
            }
        }

        return super.keyDown(keycode)
    }
}