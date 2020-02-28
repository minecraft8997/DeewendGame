@file:JvmName("DeewendBlock")

package ru.deewend.game

import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g3d.*
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.utils.Disposable
import ru.deewend.game.logic.DeewendWorld
import ru.deewend.game.textures.DeewendBlockTextureManager
import ru.deewend.game.textures.DeewendTexturesHelper
import ru.deewend.game.util.DeewendBlockPosition
import ru.deewend.game.DeewendHelper.convertDToS
import ru.deewend.game.DeewendHelper.convertSToD
import java.security.InvalidParameterException

class DeewendBlock (@SuppressWarnings("WeakerAccess") val id: Byte, private val world: DeewendWorld, private val terrain: MutableSet<DeewendBlock>, private val indexedPositions: MutableSet<DeewendBlockPosition>) : Disposable {
    companion object {
        const val BLOCK_SIZE = 1
        private const val POSITIONS_ARE_NOT_SYNCHRONIZED_ERR_MSG = "Block position and block's model position aren't synchronized"
    }

    @Volatile
    private lateinit var modelBuilder: ModelBuilder
    @Volatile
    private lateinit var model: Model
    @Volatile
    private lateinit var modelInstance: ModelInstance
    @Volatile
    private lateinit var position: DeewendBlockPosition
    var modelInstanceInitialized: Boolean = false
        private set
    @Volatile
    var positionInitialized = false
        private set

    init {
        if (!world.checkReferenceToTerrainIsCorrect(terrain)) throw InvalidParameterException("Invalid terrain data")
    }

    @Synchronized
    fun init() {
        while (DeewendTexturesHelper.isLoadingTexturePackNow);
        modelInstanceInitialized = false

        dispose()

        var x: Int? = null
        var y: Int? = null
        var z: Int? = null

        if (::modelInstance.isInitialized && ::position.isInitialized) {
            x = getX()
            y = getY()
            z = getZ()
        }

        val manager = DeewendTexturesHelper.getBlockTextureManager(id)
        modelBuilder = ModelBuilder()

        modelBuilder.begin()
        modelBuilder.node()

        modelBuilder.part(
                DeewendBlockTextureManager.FRONT_SIDE_NAME,
                GL20.GL_TRIANGLES,
                DeewendTexturesHelper.attr,
                Material(TextureAttribute.createDiffuse(manager.doneFrontSideTexture))
        ).rect(-BLOCK_SIZE.toFloat(), -BLOCK_SIZE.toFloat(), -BLOCK_SIZE.toFloat(),
                -BLOCK_SIZE.toFloat(), BLOCK_SIZE.toFloat(), -BLOCK_SIZE.toFloat(),
                BLOCK_SIZE.toFloat(), BLOCK_SIZE.toFloat(), -BLOCK_SIZE.toFloat(),
                BLOCK_SIZE.toFloat(), -BLOCK_SIZE.toFloat(), -BLOCK_SIZE.toFloat(),
                0f, 0f, -1f)

        modelBuilder.part(
                DeewendBlockTextureManager.BACK_SIDE_NAME,
                GL20.GL_TRIANGLES,
                DeewendTexturesHelper.attr,
                Material(TextureAttribute.createDiffuse(manager.doneBackSideTexture))
        ).rect(-BLOCK_SIZE.toFloat(), BLOCK_SIZE.toFloat(), BLOCK_SIZE.toFloat(),
                -BLOCK_SIZE.toFloat(), -BLOCK_SIZE.toFloat(), BLOCK_SIZE.toFloat(),
                BLOCK_SIZE.toFloat(), -BLOCK_SIZE.toFloat(), BLOCK_SIZE.toFloat(),
                BLOCK_SIZE.toFloat(), BLOCK_SIZE.toFloat(), BLOCK_SIZE.toFloat(),
                0f, 0f, 1f
        )

        modelBuilder.part(
                DeewendBlockTextureManager.BOTTOM_SIDE_NAME,
                GL20.GL_TRIANGLES,
                DeewendTexturesHelper.attr,
                Material(TextureAttribute.createDiffuse(manager.doneBottomSideTexture))
        ).rect(-BLOCK_SIZE.toFloat(), -BLOCK_SIZE.toFloat(), BLOCK_SIZE.toFloat(),
                -BLOCK_SIZE.toFloat(), -BLOCK_SIZE.toFloat(), -BLOCK_SIZE.toFloat(),
                BLOCK_SIZE.toFloat(), -BLOCK_SIZE.toFloat(), -BLOCK_SIZE.toFloat(),
                BLOCK_SIZE.toFloat(), -BLOCK_SIZE.toFloat(), BLOCK_SIZE.toFloat(),
                0f, -1f, 0f
        )

        modelBuilder.part(
                DeewendBlockTextureManager.TOP_SIDE_NAME,
                GL20.GL_TRIANGLES,
                DeewendTexturesHelper.attr,
                Material(TextureAttribute.createDiffuse(manager.doneTopSideTexture))
        ).rect(-BLOCK_SIZE.toFloat(), BLOCK_SIZE.toFloat(), -BLOCK_SIZE.toFloat(),
                -BLOCK_SIZE.toFloat(), BLOCK_SIZE.toFloat(), BLOCK_SIZE.toFloat(),
                BLOCK_SIZE.toFloat(), BLOCK_SIZE.toFloat(), BLOCK_SIZE.toFloat(),
                BLOCK_SIZE.toFloat(), BLOCK_SIZE.toFloat(), -BLOCK_SIZE.toFloat(),
                0f, 1f, 0f
        )

        modelBuilder.part(
                DeewendBlockTextureManager.LEFT_SIDE_NAME,
                GL20.GL_TRIANGLES,
                DeewendTexturesHelper.attr,
                Material(TextureAttribute.createDiffuse(manager.doneLeftSideTexture))
        ).rect(-BLOCK_SIZE.toFloat(), -BLOCK_SIZE.toFloat(), BLOCK_SIZE.toFloat(),
                -BLOCK_SIZE.toFloat(), BLOCK_SIZE.toFloat(), BLOCK_SIZE.toFloat(),
                -BLOCK_SIZE.toFloat(), BLOCK_SIZE.toFloat(), -BLOCK_SIZE.toFloat(),
                -BLOCK_SIZE.toFloat(), -BLOCK_SIZE.toFloat(), -BLOCK_SIZE.toFloat(),
                -1f, 0f, 0f
        )

        modelBuilder.part(
                DeewendBlockTextureManager.RIGHT_SIDE_NAME,
                GL20.GL_TRIANGLES,
                DeewendTexturesHelper.attr,
                Material(TextureAttribute.createDiffuse(manager.doneRightSideTexture))
        ).rect(BLOCK_SIZE.toFloat(), -BLOCK_SIZE.toFloat(), -BLOCK_SIZE.toFloat(),
                BLOCK_SIZE.toFloat(), BLOCK_SIZE.toFloat(), -BLOCK_SIZE.toFloat(),
                BLOCK_SIZE.toFloat(), BLOCK_SIZE.toFloat(), BLOCK_SIZE.toFloat(),
                BLOCK_SIZE.toFloat(), -BLOCK_SIZE.toFloat(), BLOCK_SIZE.toFloat(),
                1f, 0f, 0f
        )

        model = modelBuilder.end()
        modelInstance = ModelInstance(model)
        modelInstanceInitialized = true

        if (x != null && y != null && z != null) move(x, y, z)
    }

    @Synchronized
    fun move (x: Int, y: Int, z: Int) {
        if (this !in terrain) return
        positionInitialized = false
        if (::position.isInitialized) indexedPositions -= position

        for (b in terrain) {
            if (b == this) continue

            while (!b.positionInitialized);

            val tmpX = b.getX()
            val tmpY = b.getY()
            val tmpZ = b.getZ()

            if (tmpX == x && tmpY == y && tmpZ == z && b != this) {
                terrain -= b
                break
            }
        }

        position = DeewendBlockPosition(x, y, z)
        modelInstance.transform = Matrix4().translate(convertDToS(x.toFloat()), convertDToS(y.toFloat()), convertDToS(z.toFloat()))

        indexedPositions += position
        positionInitialized = true

        world.blockDataChanged()
    }

    @Synchronized
    fun remove () {
        if (this !in terrain) return
        val x = getX()
        val y = getY()
        val z = getZ()

        val position = DeewendBlockPosition(x, y, z)
        indexedPositions -= position
        terrain -= this

        world.blockDataChanged()
        dispose ()
    }

    @Synchronized
    fun getX(): Int {
        val x1 = convertSToD(modelInstance.transform.values[Matrix4.M03])
        val x2 = position.x.toFloat()

        if (x1 != x2) throw Error("Block position and block's model position aren't synchronized")

        return x1.toInt()
    }

    @Synchronized
    fun getY(): Int {
        val y1 = convertSToD(modelInstance.transform.values[Matrix4.M13])
        val y2 = position.y.toFloat()

        if (y1 != y2) throw Error(POSITIONS_ARE_NOT_SYNCHRONIZED_ERR_MSG)

        return y1.toInt()
    }

    @Synchronized
    fun getZ(): Int {
        val z1 = convertSToD(modelInstance.transform.values[Matrix4.M23])
        val z2 = position.z.toFloat()

        if (z1 != z2) throw Error(POSITIONS_ARE_NOT_SYNCHRONIZED_ERR_MSG)

        return z1.toInt()
    }

    @Synchronized
    fun getModelInstance (worldModel: ModelCache): ModelInstance {
        if (!world.checkReferenceToWorldModelIsCorrect(worldModel)) throw InvalidParameterException("Invalid terrain data")
        return modelInstance
    }

    @Synchronized
    fun shouldBeRendered (): Boolean {
        val x = getX()
        val y = getY()
        val z = getZ()

        if (
                isThereABlock(x, y, z - 1) &&
                isThereABlock(x, y, z + 1) &&
                isThereABlock(x - 1, y, z) &&
                isThereABlock(x + 1, y, z) &&
                isThereABlock(x, y + 1, z) &&
                isThereABlock(x, y - 1, z)
        ) {
            return false
        }

        return true
    }

    @Synchronized
    override fun dispose() {
        if (::model.isInitialized) model.dispose()
    }

    @Synchronized
    private fun isThereABlock(x: Int, y: Int, z: Int) = DeewendBlockPosition(x, y, z) in indexedPositions
}