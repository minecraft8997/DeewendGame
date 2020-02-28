package ru.deewend.game.textures

import com.badlogic.gdx.graphics.VertexAttributes
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.utils.Disposable
import ru.deewend.game.Deewend
import ru.deewend.game.DeewendHelper
import ru.deewend.game.logic.DeewendWorld
import java.util.*

object DeewendTexturesHelper : Disposable {
    const val attr: Long = (VertexAttributes.Usage.Position or VertexAttributes.Usage.Normal or VertexAttributes.Usage.TextureCoordinates).toLong()

    @Volatile var errorLoadingTextures = false
    @Volatile var isLoadingTexturePackNow = false
        private set
    @Volatile lateinit var currentTexturePack: String
        private set
    private val blockTextures: MutableMap<Byte, DeewendBlockTextureManager> = Collections.synchronizedMap(HashMap<Byte, DeewendBlockTextureManager>())

    @Synchronized
    fun loadTexturePack(name: String, deewend: Deewend, updateBlockTextures: Boolean) {
        if (::currentTexturePack.isInitialized && name == currentTexturePack) return

        isLoadingTexturePackNow = true

        dispose()

        blockTextures.clear()
        currentTexturePack = name

        DeewendHelper.blockIdRange().forEach { id ->
            run {
                try {
                    blockTextures[id.toByte()] = DeewendBlockTextureManager(id.toByte())
                } catch (t: Throwable) {
                    errorLoadingTextures = true

                    if (name == DeewendDefaultTexturePack.PACK_NAME) throw t
                    loadTexturePack(DeewendDefaultTexturePack.PACK_NAME, deewend, updateBlockTextures)
                    return
                }
            }
        }

        if (updateBlockTextures) {
            deewend.world.getTerrain(deewend).forEach { block ->
                run {
                    block.init()
                }
            }
        }

        isLoadingTexturePackNow = false
    }

    @Synchronized
    fun getBlockTextureManager (id: Byte) = blockTextures[id]!!

    override fun dispose() {
        blockTextures.forEach { (_, blockTexture) ->
            run {
                blockTexture.dispose()
            }
        }
    }
}