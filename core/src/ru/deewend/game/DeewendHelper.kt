@file:JvmName("DeewendHelper")
package ru.deewend.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.utils.Base64Coder
import ru.deewend.game.logic.DeewendConstants
import ru.deewend.game.logic.DeewendWorld
import ru.deewend.game.textures.DeewendDefaultTexturePack
import java.io.BufferedWriter
import java.io.File
import java.security.InvalidParameterException
import kotlin.random.Random

object DeewendHelper {
    private const val congratulations = "iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAABD0lEQVR42u1X0RKDIAyDu/z/L7vzYV7tmrQIp3uwDxPEQkjTMtBa29qDhv1n284Yeu9Hm43t732bPe1ctn8A8It6swsp+34XLcp8MUOfn9T2LRgFAtkC1tHTGjETManYhUJfmYCxUvWB+ljtxo5V2mw+RLuP+kr9jIEsND9ZwID4MUbzaPypBrJUZLtWdYQxgirSjAFWK5QmZBr6BasaGM0KVGKpVK5qRSUVkdV1Vk5nasVUKa6k1wiYNAtUjVh2HDMVe+qzk215CCoKvxp7mgWq1kfPWRBoD1sqQnXmW22M+FjGoGLuxRfF/orPf4bgBTD6D3iVTxmAuqzM+LwauB0AvRveZbQSVi8h6v0Vnw+IF/Tpw7facwAAAABJRU5ErkJggg=="

    val deewendHome: String = "./data"
    val pathToCpnTxt = "$deewendHome/textures/current_texture_pack_name.txt"
    var nextLineY: Float = Gdx.graphics.height.toFloat() - 2f + 14f
        get() {
            field -= 14f
            return field
        }
        private set

    class GLRGB (red: Short, green: Short, blue: Short) {
        val red = red.toFloat() / 255f
        val green = green.toFloat() / 255f
        val blue = blue.toFloat() / 255f
        val alpha = 1f
    }

    fun init() {
        val h = File(deewendHome)
        if (!h.exists()) {
            h.mkdir()
            File("$deewendHome/textures").mkdir()
            DeewendDefaultTexturePack.generate()
            generateCurrentTexturePackNameTxt()
        }
    }

    private fun isInRange (a: Int, b: Int, c: Int): Boolean = c in a..b

    fun generateCongratulationsText (world: DeewendWorld) {
        val result: ByteArray = Base64Coder.decode(congratulations)
        val map = Pixmap (result, 0, result.size)

        for (x in 0 until map.width) {
            for (y in 0 until map.height) {
                val pixel = map.getPixel(x, y)
                if (pixel == 255) {
                   continue
                }

                world.placeBlock(x, 3, y, DeewendConstants.PLANKS_BLOCK_ID)
            }
        }

        map.dispose()
    }

    private fun generateCurrentTexturePackNameTxt() {
        val cpn = File(pathToCpnTxt)
        cpn.createNewFile()
        val wCpn = BufferedWriter(cpn.writer())
        wCpn.write(DeewendDefaultTexturePack.PACK_NAME)
        wCpn.close()
    }

    fun rotate90Pixmap (pixmap: Pixmap, clockwise: Boolean): Pixmap {
        val width = pixmap.width
        val height = pixmap.height

        if (width != height || Math.floorMod(width, 2) != 0) throw InvalidParameterException()

        if (!clockwise) {
            for (x in 0 until width / 2) {
                for (y in x until width - x - 1) {
                    val temp: Int = pixmap.getPixel(x, y)
                    pixmap.drawPixel(x, y, pixmap.getPixel(y, width - 1 - x))
                    pixmap.drawPixel(y, width - 1 - x, pixmap.getPixel(width - 1 - x, width - 1 - y))
                    pixmap.drawPixel(width - 1 - x, width - 1 - y, pixmap.getPixel(width - 1 - y, x))
                    pixmap.drawPixel(width - 1 - y, x, temp)
                }
            }

            return pixmap
        }
        //clockwise

        for (x in 0 until width / 2) {
            for (y in x until width - x - 1) {
                val temp = pixmap.getPixel(x, y)
                pixmap.drawPixel(x, y, pixmap.getPixel(width - 1 - y, x))
                pixmap.drawPixel(width - 1 - y, x, pixmap.getPixel(width - 1 - x, width - 1 - y))
                pixmap.drawPixel(width - 1 - x, width - 1 - y, pixmap.getPixel(y, width - 1 - x))
                pixmap.drawPixel(y, width - 1 - x, temp)
            }
        }

        return pixmap
    }

    // convert standard cord system to Deewend's cord system
    fun convertSToD (f: Float): Float = f / DeewendBlock.BLOCK_SIZE / 2

    // convert Deewend's cord system to standard coordinate system
    fun convertDToS(f: Float) = f * DeewendBlock.BLOCK_SIZE * 2

    //fun lowerCaseWordAndUpperCaseFirstLetter (s: String): String {
    //    val s1 = s.toLowerCase()
    //    return s1.substring(0, 1).toUpperCase() + s1.substring(1)
    //}

    fun isCrustLayer (b: Int): Boolean = isInRange(0, 0, b)
    fun isDirtLayer (b: Int): Boolean = isInRange(1, 1, b)
    fun isGrassLayer (b: Int): Boolean = b == 2
    fun spawnDiamond (b: Int): Boolean = isCrustLayer(b) && Random.nextInt(1000) == 436

    //TODO Assert that the length of the range is equals DeewendConstants.TOTAL_BLOCKS
    fun blockIdRange (): IntRange {
        return (DeewendConstants.AIR_BLOCK_ID + 1)..(DeewendConstants.AIR_BLOCK_ID + DeewendConstants.TOTAL_BLOCKS)
    }
}