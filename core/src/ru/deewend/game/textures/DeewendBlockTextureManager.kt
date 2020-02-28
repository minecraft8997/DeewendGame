package ru.deewend.game.textures

import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.Base64Coder
import com.badlogic.gdx.utils.Disposable
import ru.deewend.game.DeewendHelper
import ru.deewend.game.util.DeewendUtils
import java.security.InvalidParameterException

data class DeewendBlockTextureManager(val id: Byte) : Disposable {
    companion object {
        const val RIGHT_SIDE_NAME = "right"
        const val LEFT_SIDE_NAME = "left"
        const val TOP_SIDE_NAME = "top"
        const val BOTTOM_SIDE_NAME = "bottom"
        const val FRONT_SIDE_NAME = "front"
        const val BACK_SIDE_NAME = "back"

        fun genPath (texturePackName: String, id: Byte, sideName: String) = DeewendHelper.deewendHome + "/textures/$texturePackName/$id/$sideName.txt"
    }

    val rightSide: Pixmap
    val leftSide: Pixmap
    val topSide: Pixmap
    val bottomSide: Pixmap
    val frontSide: Pixmap
    val backSide: Pixmap

    val doneRightSideTexture: Texture
    val doneLeftSideTexture: Texture
    val doneTopSideTexture: Texture
    val doneBottomSideTexture: Texture
    val doneFrontSideTexture: Texture
    val doneBackSideTexture: Texture

    private val pathToRightSide = genPath(DeewendTexturesHelper.currentTexturePack, id, RIGHT_SIDE_NAME)
    private val pathToLeftSide = genPath(DeewendTexturesHelper.currentTexturePack, id, LEFT_SIDE_NAME)
    private val pathToTopSide = genPath(DeewendTexturesHelper.currentTexturePack, id, TOP_SIDE_NAME)
    private val pathToBottomSide = genPath(DeewendTexturesHelper.currentTexturePack, id, BOTTOM_SIDE_NAME)
    private val pathToFrontSide = genPath(DeewendTexturesHelper.currentTexturePack, id, FRONT_SIDE_NAME)
    private val pathToBackSide = genPath(DeewendTexturesHelper.currentTexturePack, id, BACK_SIDE_NAME)

    init {
        if (!checkIDIsValid()) throw InvalidParameterException("Invalid ID was given")

        val rightSideBase64: ByteArray = Base64Coder.decode(findBase64(pathToRightSide))
        val leftSideBase64: ByteArray = Base64Coder.decode(findBase64(pathToLeftSide))
        val topSideBase64: ByteArray = Base64Coder.decode(findBase64(pathToTopSide))
        val bottomSideBase64: ByteArray = Base64Coder.decode(findBase64(pathToBottomSide))
        val frontSideBase64: ByteArray = Base64Coder.decode(findBase64(pathToFrontSide))
        val backSideBase64: ByteArray = Base64Coder.decode(findBase64(pathToBackSide))

        rightSide = Pixmap(rightSideBase64, 0, rightSideBase64.size)
        leftSide = Pixmap(leftSideBase64, 0, leftSideBase64.size)
        topSide = Pixmap(topSideBase64, 0, topSideBase64.size)
        bottomSide = Pixmap(bottomSideBase64, 0, bottomSideBase64.size)
        frontSide = Pixmap(frontSideBase64, 0, frontSideBase64.size)
        backSide = Pixmap(backSideBase64, 0, backSideBase64.size)

        doneRightSideTexture = Texture(DeewendHelper.rotate90Pixmap(rightSide, false))
        doneLeftSideTexture = Texture(DeewendHelper.rotate90Pixmap(leftSide, false))
        doneTopSideTexture = Texture(topSide)
        doneBottomSideTexture = Texture(bottomSide)
        doneFrontSideTexture = Texture(DeewendHelper.rotate90Pixmap(frontSide, false))
        doneBackSideTexture = Texture(DeewendHelper.rotate90Pixmap(backSide, true))
    }

    private fun checkIDIsValid(): Boolean {
        for (id_ in DeewendHelper.blockIdRange()) {
            if (id_.toByte() == id) return true
        }

        return false
    }
    private fun findBase64(path: String) = findBase64(path, 0)
    private fun findBase64(path: String, currentStackSize: Byte): String {
        if (currentStackSize < 0) throw InvalidParameterException()
        if (currentStackSize > 6) throw StackOverflowError()

        return when (val line = DeewendUtils.readFirstLine(path)) {
            RIGHT_SIDE_NAME -> findBase64(pathToRightSide, (currentStackSize + 1).toByte())
            LEFT_SIDE_NAME -> findBase64(pathToLeftSide, (currentStackSize + 1).toByte())
            TOP_SIDE_NAME -> findBase64(pathToTopSide, (currentStackSize + 1).toByte())
            BOTTOM_SIDE_NAME -> findBase64(pathToBottomSide, (currentStackSize + 1).toByte())
            FRONT_SIDE_NAME -> findBase64(pathToFrontSide, (currentStackSize + 1).toByte())
            BACK_SIDE_NAME -> findBase64(pathToBackSide, (currentStackSize + 1).toByte())

            else -> line
        }
    }

    override fun dispose() {
        rightSide.dispose()
        leftSide.dispose()
        topSide.dispose()
        bottomSide.dispose()
        frontSide.dispose()
        backSide.dispose()

        doneRightSideTexture.dispose()
        doneLeftSideTexture.dispose()
        doneTopSideTexture.dispose()
        doneBottomSideTexture.dispose()
        doneFrontSideTexture.dispose()
        doneBackSideTexture.dispose()
    }
}
