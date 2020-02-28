@file:JvmName("DeewendWorld")
package ru.deewend.game.logic

import com.badlogic.gdx.graphics.g3d.Environment
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.g3d.ModelCache
import com.badlogic.gdx.utils.Disposable
import ru.deewend.game.Deewend
import ru.deewend.game.DeewendBlock
import ru.deewend.game.DeewendHelper
import ru.deewend.game.util.DeewendBlockPosition
import java.security.InvalidParameterException
import java.util.*
import kotlin.collections.HashSet

class DeewendWorld (private val deewend: Deewend) : Disposable {
    companion object {
        const val worldSize = 32
    }

    private val terrain: MutableSet<DeewendBlock> = Collections.synchronizedSet(HashSet<DeewendBlock>())
    private val indexedPositions: MutableSet<DeewendBlockPosition> = Collections.synchronizedSet(HashSet<DeewendBlockPosition>())

    @Volatile
    private var generatingTerrain = false

    @Volatile
    private lateinit var worldModel: ModelCache

    @Synchronized
    fun init() {
        generateTerrain()
        generateWorldModel()
    }

    @Synchronized
    private fun generateTerrain() {
        generatingTerrain = true

        for (x in 0 until worldSize) {
            for (y in 0 until worldSize) {
                for (z in 0 until worldSize) {
                    if (DeewendHelper.isCrustLayer(y)) {
                        if (DeewendHelper.spawnDiamond(y)) {
                            placeBlock(x, y, z, DeewendConstants.DIAMOND_BLOCK_ID)
                            continue
                        }

                        placeBlock(x, y, z, DeewendConstants.STONE_BLOCK_ID)
                        continue
                    }

                    if (DeewendHelper.isDirtLayer(y)) {
                        placeBlock(x, y, z, DeewendConstants.DIRT_BLOCK_ID)
                        continue
                    }

                    if (DeewendHelper.isGrassLayer(y)) {
                        placeBlock(x, y, z, DeewendConstants.GRASS_BLOCK_ID)
                        continue
                    }
                }
            }
        }

        DeewendHelper.generateCongratulationsText(this)
        generatingTerrain = false
    }

    @Synchronized
    fun placeBlock (x: Int, y: Int, z: Int, blockID: Byte): DeewendBlock {
        val block = DeewendBlock(blockID, this, terrain, indexedPositions)
        terrain += block

        block.init()
        block.move (x, y, z)

        return block
    }

    @Synchronized
    fun removeBlock (x: Int, y: Int, z: Int) {
        val block = getBlockAt(x, y, z) ?: throw Exception("There is no block with this coordinates: ($x, $y, $z)")
        block.remove()
    }

    @Synchronized
    fun checkReferenceToTerrainIsCorrect (terrain: MutableSet<DeewendBlock>) = this.terrain == terrain

    @Synchronized
    fun checkReferenceToWorldModelIsCorrect (worldModel: ModelCache): Boolean = this.worldModel == worldModel

    @Synchronized
    fun blockDataChanged () {
        if (generatingTerrain) return

        regenerateWorldModel()
    }

    @Synchronized
    fun getBlockAt (x: Int, y: Int, z: Int): DeewendBlock? {
        while (generatingTerrain);

        for (b in terrain) {
            while (!b.positionInitialized);

            val tmpX = b.getX()
            val tmpY = b.getY()
            val tmpZ = b.getZ()

            if (tmpX == x && tmpY == y && tmpZ == z) return b
        }

        return null
    }

    @Synchronized
    private fun generateWorldModel () = regenerateWorldModel()

    @Synchronized
    fun getTerrain (deewend: Deewend): MutableSet<DeewendBlock> {
        if (this.deewend != deewend) throw IllegalAccessException()

        return terrain
    }

    @Synchronized
    private fun regenerateWorldModel () {
        if (::worldModel.isInitialized) {
            worldModel.dispose()
        }

        worldModel = ModelCache()
        worldModel.begin()

        terrain.forEach { block ->
            run {
                while (!block.modelInstanceInitialized);
                while (!block.positionInitialized);

                if (block.shouldBeRendered()) worldModel.add(block.getModelInstance(worldModel))
            }
        }

        worldModel.end()
    }

    @Synchronized
    fun getIndexedPositions (deewend: Deewend): MutableSet<DeewendBlockPosition> {
        if (this.deewend != deewend) throw InvalidParameterException()

        return indexedPositions
    }

    @Synchronized
    fun render (batch: ModelBatch, environment: Environment) = batch.render(worldModel, environment)

    override fun dispose() {
        terrain.forEach { block ->
            run {
                block.dispose()
            }
        }

        if (::worldModel.isInitialized) {
            worldModel.dispose()
        }
    }
}