package ru.deewend.game.textures

import ru.deewend.game.DeewendBlock
import ru.deewend.game.DeewendHelper
import ru.deewend.game.logic.DeewendConstants
import java.io.BufferedWriter
import java.io.File

object DeewendDefaultTexturePack {
    const val PACK_NAME = "default"

    fun generate() {
        val d = File("${DeewendHelper.deewendHome}/textures/$PACK_NAME")
        if (d.exists()) return

        d.mkdir()
        DeewendHelper.blockIdRange().forEach {
            File("${DeewendHelper.deewendHome}/textures/$PACK_NAME/${it}").mkdir()
            val right = File(DeewendBlockTextureManager.genPath(PACK_NAME, it.toByte(), DeewendBlockTextureManager.RIGHT_SIDE_NAME))
            val left = File(DeewendBlockTextureManager.genPath(PACK_NAME, it.toByte(), DeewendBlockTextureManager.LEFT_SIDE_NAME))
            val top = File(DeewendBlockTextureManager.genPath(PACK_NAME, it.toByte(), DeewendBlockTextureManager.TOP_SIDE_NAME))
            val bottom = File(DeewendBlockTextureManager.genPath(PACK_NAME, it.toByte(), DeewendBlockTextureManager.BOTTOM_SIDE_NAME))
            val front = File(DeewendBlockTextureManager.genPath(PACK_NAME, it.toByte(), DeewendBlockTextureManager.FRONT_SIDE_NAME))
            val back = File(DeewendBlockTextureManager.genPath(PACK_NAME, it.toByte(), DeewendBlockTextureManager.BACK_SIDE_NAME))

            right.createNewFile()
            left.createNewFile()
            top.createNewFile()
            bottom.createNewFile()
            front.createNewFile()
            back.createNewFile()

            val wRight = BufferedWriter(right.writer())
            val wLeft = BufferedWriter(left.writer())
            val wTop = BufferedWriter(top.writer())
            val wBottom = BufferedWriter(bottom.writer())
            val wFront = BufferedWriter(front.writer())
            val wBack = BufferedWriter(back.writer())

            fun closeOutputStreams() {
                wRight.close()
                wLeft.close()
                wTop.close()
                wBottom.close()
                wFront.close()
                wBack.close()
            }

            when (it.toByte()) {
                DeewendConstants.STONE_BLOCK_ID -> {
                    wRight.write("iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAABFklEQVR42oVTwQ4BQQydQ4WNhA0HBxIc/KKDL/CPDkg4OJAl2SyxCfuG13TGosmkk07b19d2ZLmYP1yNNETcvSz1TqGNoi+ddtsVt1vgyMCk2VQ77v00dccsc5c8dwIna4Qgka2AGiCn7OyPVoDHorgGRkov7XrN5PCpEAOKEjtDmCzWSdIKqGkPEAyU1Wb7MlYoltp6v9cgatIScoaTLc8iW0TQJchby89R1fUGweW99Friudo9mA6HwWRscvZDvgVD2Hn2CXO3PcBdbBCaRiQuD8utWzBtYrw0WBjy9zyrg/tsMvZVWTpBBaPBICgbPO07xoxkANAxWvTd4fARaD8UG0cAX51tXsyvbnHsDuhf+Pdlf8kToEjA35vCYqIAAAAASUVORK5CYII=")
                    wLeft.write("right")
                    wTop.write("right")
                    wBottom.write("right")
                    wFront.write("right")
                    wBack.write("right")
                }

                DeewendConstants.DIRT_BLOCK_ID -> {
                    wRight.write("iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAABJklEQVR42m1TsW3EMAxkwQRpDBWGFlD1G/wsGSTLZQNtoEoLGC4eaYJPkZD+o3FPRABhUT6Sd2dZPz/ef8VWt7hY7PJYK/Yr8jjfEk4nQDWBNtpfLQryhmfkKjQxg4LVzWJgH8UxWFeaXACOvKb9oIYNTZQnzqQzL/ajAKeF6Ar0NsorNa1450yWnzeRl++HiQFcQU+S00LFvJo1OSV0cj4ahD9c2MHSpx9fofEh9t757p4Y6EKsCnmzGcbfa1C5oaNT/aL9/s9nddkLS3hFwQCTmbT6tEMWcAJ2TxdpR3Engw4WDrSIJoO+1NmgIDppvKfpFU0ame1n2mlyaN6I7sB5RUFnbe5Bw4XwZyWNe7pIweYKTDBTN2NCUxwuyajzr0Q+6R78AVMUd9QZsSkXAAAAAElFTkSuQmCC")
                    wLeft.write("right")
                    wTop.write("right")
                    wBottom.write("right")
                    wFront.write("right")
                    wBack.write("right")
                }

                DeewendConstants.GRASS_BLOCK_ID -> {
                    wRight.write("iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAABf0lEQVR42l1SPWrDUAz+KK+lSzA4ePD6plwgZM3WJXQs5Ag9RCYfIkcoZAxZsmU1vUAmrxmMh9DFJNBWkiVHtkD4ve/Tzyf5BezyP7BFYNVccLjmMFslF/n2WFSi6kMQeoLAA/JH0jTHoRomcgPhYt4XCpZsgU/bNX4/vgaJ79tlj4maK2HHJfafJwQm928nFARsCJhT0PduDdC52CqWAptp17XYLbuqhIH4wOT82IGcwAQXmdM5Tl57dcKxKMKq5xbxTlza6g7Sx1IW+i1Tzm1FmfDkGdhbv0OEzF2mkPHEZuTNqHjE0FhFMKIkrx0504JnpwoaJ3caQxRED+qZK9/451DQzKlKtDBbfe92EUzKVSvySD/u3DjppRbgHUz8CC+aYHKr0azcTcbSOKi6x0vUTgvtYqpEBQeSWxFrEv1TTtRLN+Nt1D3TItEtm7FQus42c+3knmH/v0so/Wy8A5GqLytzMzbWZbSLhcaYssDLqHQmAyejRUFVWXLl3sE/9yeVkMy5WVwAAAAASUVORK5CYII=")
                    wLeft.write("right")
                    wTop.write("iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAABE0lEQVR42m1TQQrCQAzMYXHFiqCCB/HmH3yKP/fozasKgmKloE7aaWd320s32UkymWTD8WTf18WKL1Rm75vZdGWj32TZ4eiY7dr/554Cm2ebjEGPc3tm8oDqACAQ4Kb+27H1aXUEA6M+T06qnvVqNt8OFeknmK0lrbIXN7rK8DmzmGpCJrjrmaqA0IE95tXIwM+1sFUggtEjWaEFAKfrgR0TIdhj1UmBOAkEbQ6DTZG1qGtAhVU0H1GV+unT8Qb27BRjuTAOjOUkyDzwMheMrPLtLBLp2lIwUCUzispFy5P1IoIJlwh2/gaog07Dd4EHHZ8q3rcYxx9VUKN4DyIeliwf52L/x/AV6kYiMKlejWjQ3f0AAB6O9q670TMAAAAASUVORK5CYII=")
                    wBottom.write("iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAABJklEQVR42m1TsW3EMAxkwQRpDBWGFlD1G/wsGSTLZQNtoEoLGC4eaYJPkZD+o3FPRABhUT6Sd2dZPz/ef8VWt7hY7PJYK/Yr8jjfEk4nQDWBNtpfLQryhmfkKjQxg4LVzWJgH8UxWFeaXACOvKb9oIYNTZQnzqQzL/ajAKeF6Ar0NsorNa1450yWnzeRl++HiQFcQU+S00LFvJo1OSV0cj4ahD9c2MHSpx9fofEh9t757p4Y6EKsCnmzGcbfa1C5oaNT/aL9/s9nddkLS3hFwQCTmbT6tEMWcAJ2TxdpR3Engw4WDrSIJoO+1NmgIDppvKfpFU0ame1n2mlyaN6I7sB5RUFnbe5Bw4XwZyWNe7pIweYKTDBTN2NCUxwuyajzr0Q+6R78AVMUd9QZsSkXAAAAAElFTkSuQmCC")
                    wFront.write("right")
                    wBack.write("right")
                }

                DeewendConstants.PLANKS_BLOCK_ID -> {
                    wRight.write("iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAABAUlEQVR42qVTsQrCMBDNcG1jOhQcRBB0KIiTm1/kh/k3Lm6Ci+BQRAcHoUNr0wraS70Qa9MqPii9tLl3914usF4tH+xHpFmu3oK7DDC4xon+yT2nNRmT8CEiRdAP/I+NNlKqToDN9qCCQuYsTgsVZ/JeJiluFgiHOZ5r7QjCybCxUiYrsltJ1oTeq4BVAulsA8oErFrX1WUiAQvDbn9sTUBvEOQPIkkl84VXSVjMQxWcLrHW1nWUZjeAOrCV0SBQmjD5G/36FNBlImkyk8yqd0VFIIrOSp+pq+vs3zqYTcfsHwC1RoPzzZ0wPQJamB/NubAZSnv0XWibA5qBuk9ccPYEyNJmXB4aUTUAAAAASUVORK5CYII=")
                    wLeft.write("right")
                    wTop.write("right")
                    wBottom.write("right")
                    wFront.write("right")
                    wBack.write("right")
                }

                DeewendConstants.DIAMOND_BLOCK_ID -> {
                    wRight.write("iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAABmUlEQVR42oVTPUsDQRB9xZoPE/JJDBghNmp+hAH/gEUKBf+BYhmUKAFBUqSxtRcEwcZCELQSYiP4DyzUIlW4JAohhhzovY2z7p0BB44bZmdm5715q5qH+1+YYjNKYey6xheTmJg5ScRiGI5GvkQpjIbDJk4/m0rB6ffxMRhAMckO0tjInoD/2uoaUC6jcXyEbv/9dwIeDoefOljf3AI6HcBxgFIJp3c3OonN67ee730qNuuDaCBkUknUz8/g9HqToqdHdEMh7bN5xPOj0Qiq6xWcXF/5OWAxb2FiIZ8nLtjQXtptwwcvQTJh4CnBzCQW2qwLVnsLnISQJVfJYXBVmrRcDo3Liz8rZrE7dvVfBfcqRtLIR2Zl2WzG1gH58JEYFFHawzk/lzMwyBP3LpOKTpRdRNLkJhGPjDtNYIZEBqp7B0CrhebDvValkKhxeh/9pcWiFpsNx7Ta2d1GNp1GwhqbOO0Jn1/fdDNeYNYoiiouFHQSu9uFklirbExU+iNngecjMYjPXq+oNGvJ2byF/54sLe4Vxa1CsW/htMn4xdwufwAAAABJRU5ErkJggg==")
                    wLeft.write("right")
                    wTop.write("right")
                    wBottom.write("right")
                    wFront.write("right")
                    wBack.write("right")
                }

                else -> {
                    closeOutputStreams()
                    throw Error("No default texture provided for block with id $it")
                }
            }

            closeOutputStreams()
        }
    }

    /*
    abstract class BlockTexture {
        abstract val rightSideBase64: String
        abstract val leftSideBase64: String
        abstract val topSideBase64: String
        abstract val bottomSideBase64: String
        abstract val frontSideBase64: String
        abstract val backSideBase64: String
    }
     */

    //class ExampleBlockTexture : BlockTexture() {
    //    override val rightSideBase64 = ""
    //    override val leftSideBase64 = ""
    //    override val topSideBase64 = ""
    //    override val bottomSideBase64 = ""
    //    override val frontSideBase64 = ""
    //    override val backSideBase64 = ""
    //}

    /*
    class StoneBlockTexture : BlockTexture() {
        override val rightSideBase64 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAABFklEQVR42oVTwQ4BQQydQ4WNhA0HBxIc/KKDL/CPDkg4OJAl2SyxCfuG13TGosmkk07b19d2ZLmYP1yNNETcvSz1TqGNoi+ddtsVt1vgyMCk2VQ77v00dccsc5c8dwIna4Qgka2AGiCn7OyPVoDHorgGRkov7XrN5PCpEAOKEjtDmCzWSdIKqGkPEAyU1Wb7MlYoltp6v9cgatIScoaTLc8iW0TQJchby89R1fUGweW99Friudo9mA6HwWRscvZDvgVD2Hn2CXO3PcBdbBCaRiQuD8utWzBtYrw0WBjy9zyrg/tsMvZVWTpBBaPBICgbPO07xoxkANAxWvTd4fARaD8UG0cAX51tXsyvbnHsDuhf+Pdlf8kToEjA35vCYqIAAAAASUVORK5CYII="
        override val leftSideBase64 = "right"
        override val topSideBase64 = "right"
        override val bottomSideBase64 = "right"
        override val frontSideBase64 = "right"
        override val backSideBase64 = "right"
    }

    class DirtBlockTexture : BlockTexture() {
        override val rightSideBase64 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAABJklEQVR42m1TsW3EMAxkwQRpDBWGFlD1G/wsGSTLZQNtoEoLGC4eaYJPkZD+o3FPRABhUT6Sd2dZPz/ef8VWt7hY7PJYK/Yr8jjfEk4nQDWBNtpfLQryhmfkKjQxg4LVzWJgH8UxWFeaXACOvKb9oIYNTZQnzqQzL/ajAKeF6Ar0NsorNa1450yWnzeRl++HiQFcQU+S00LFvJo1OSV0cj4ahD9c2MHSpx9fofEh9t757p4Y6EKsCnmzGcbfa1C5oaNT/aL9/s9nddkLS3hFwQCTmbT6tEMWcAJ2TxdpR3Engw4WDrSIJoO+1NmgIDppvKfpFU0ame1n2mlyaN6I7sB5RUFnbe5Bw4XwZyWNe7pIweYKTDBTN2NCUxwuyajzr0Q+6R78AVMUd9QZsSkXAAAAAElFTkSuQmCC"
        override val leftSideBase64 = "right"
        override val topSideBase64 = "right"
        override val bottomSideBase64 = "right"
        override val frontSideBase64 = "right"
        override val backSideBase64 = "right"
    }

    class GrassBlockTexture : BlockTexture() {
        override val rightSideBase64 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAABf0lEQVR42l1SPWrDUAz+KK+lSzA4ePD6plwgZM3WJXQs5Ag9RCYfIkcoZAxZsmU1vUAmrxmMh9DFJNBWkiVHtkD4ve/Tzyf5BezyP7BFYNVccLjmMFslF/n2WFSi6kMQeoLAA/JH0jTHoRomcgPhYt4XCpZsgU/bNX4/vgaJ79tlj4maK2HHJfafJwQm928nFARsCJhT0PduDdC52CqWAptp17XYLbuqhIH4wOT82IGcwAQXmdM5Tl57dcKxKMKq5xbxTlza6g7Sx1IW+i1Tzm1FmfDkGdhbv0OEzF2mkPHEZuTNqHjE0FhFMKIkrx0504JnpwoaJ3caQxRED+qZK9/451DQzKlKtDBbfe92EUzKVSvySD/u3DjppRbgHUz8CC+aYHKr0azcTcbSOKi6x0vUTgvtYqpEBQeSWxFrEv1TTtRLN+Nt1D3TItEtm7FQus42c+3knmH/v0so/Wy8A5GqLytzMzbWZbSLhcaYssDLqHQmAyejRUFVWXLl3sE/9yeVkMy5WVwAAAAASUVORK5CYII="
        override val leftSideBase64 = "right"
        override val topSideBase64 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAABE0lEQVR42m1TQQrCQAzMYXHFiqCCB/HmH3yKP/fozasKgmKloE7aaWd320s32UkymWTD8WTf18WKL1Rm75vZdGWj32TZ4eiY7dr/554Cm2ebjEGPc3tm8oDqACAQ4Kb+27H1aXUEA6M+T06qnvVqNt8OFeknmK0lrbIXN7rK8DmzmGpCJrjrmaqA0IE95tXIwM+1sFUggtEjWaEFAKfrgR0TIdhj1UmBOAkEbQ6DTZG1qGtAhVU0H1GV+unT8Qb27BRjuTAOjOUkyDzwMheMrPLtLBLp2lIwUCUzispFy5P1IoIJlwh2/gaog07Dd4EHHZ8q3rcYxx9VUKN4DyIeliwf52L/x/AV6kYiMKlejWjQ3f0AAB6O9q670TMAAAAASUVORK5CYII="
        override val bottomSideBase64 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAABJklEQVR42m1TsW3EMAxkwQRpDBWGFlD1G/wsGSTLZQNtoEoLGC4eaYJPkZD+o3FPRABhUT6Sd2dZPz/ef8VWt7hY7PJYK/Yr8jjfEk4nQDWBNtpfLQryhmfkKjQxg4LVzWJgH8UxWFeaXACOvKb9oIYNTZQnzqQzL/ajAKeF6Ar0NsorNa1450yWnzeRl++HiQFcQU+S00LFvJo1OSV0cj4ahD9c2MHSpx9fofEh9t757p4Y6EKsCnmzGcbfa1C5oaNT/aL9/s9nddkLS3hFwQCTmbT6tEMWcAJ2TxdpR3Engw4WDrSIJoO+1NmgIDppvKfpFU0ame1n2mlyaN6I7sB5RUFnbe5Bw4XwZyWNe7pIweYKTDBTN2NCUxwuyajzr0Q+6R78AVMUd9QZsSkXAAAAAElFTkSuQmCC"
        override val frontSideBase64 = "right"
        override val backSideBase64 = "right"
    }

    class DiamondBlockTexture : BlockTexture() {
        override val rightSideBase64 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAABmUlEQVR42oVTPUsDQRB9xZoPE/JJDBghNmp+hAH/gEUKBf+BYhmUKAFBUqSxtRcEwcZCELQSYiP4DyzUIlW4JAohhhzovY2z7p0BB44bZmdm5715q5qH+1+YYjNKYey6xheTmJg5ScRiGI5GvkQpjIbDJk4/m0rB6ffxMRhAMckO0tjInoD/2uoaUC6jcXyEbv/9dwIeDoefOljf3AI6HcBxgFIJp3c3OonN67ee730qNuuDaCBkUknUz8/g9HqToqdHdEMh7bN5xPOj0Qiq6xWcXF/5OWAxb2FiIZ8nLtjQXtptwwcvQTJh4CnBzCQW2qwLVnsLnISQJVfJYXBVmrRcDo3Liz8rZrE7dvVfBfcqRtLIR2Zl2WzG1gH58JEYFFHawzk/lzMwyBP3LpOKTpRdRNLkJhGPjDtNYIZEBqp7B0CrhebDvValkKhxeh/9pcWiFpsNx7Ta2d1GNp1GwhqbOO0Jn1/fdDNeYNYoiiouFHQSu9uFklirbExU+iNngecjMYjPXq+oNGvJ2byF/54sLe4Vxa1CsW/htMn4xdwufwAAAABJRU5ErkJggg=="
        override val leftSideBase64 = "right"
        override val topSideBase64 = "right"
        override val bottomSideBase64 = "right"
        override val frontSideBase64 = "right"
        override val backSideBase64 = "right"
    }

    class PlanksBlockTexture : BlockTexture() {
        override val rightSideBase64 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAABAUlEQVR42qVTsQrCMBDNcG1jOhQcRBB0KIiTm1/kh/k3Lm6Ci+BQRAcHoUNr0wraS70Qa9MqPii9tLl3914usF4tH+xHpFmu3oK7DDC4xon+yT2nNRmT8CEiRdAP/I+NNlKqToDN9qCCQuYsTgsVZ/JeJiluFgiHOZ5r7QjCybCxUiYrsltJ1oTeq4BVAulsA8oErFrX1WUiAQvDbn9sTUBvEOQPIkkl84VXSVjMQxWcLrHW1nWUZjeAOrCV0SBQmjD5G/36FNBlImkyk8yqd0VFIIrOSp+pq+vs3zqYTcfsHwC1RoPzzZ0wPQJamB/NubAZSnv0XWibA5qBuk9ccPYEyNJmXB4aUTUAAAAASUVORK5CYII="
        override val leftSideBase64 = "right"
        override val topSideBase64 = "right"
        override val bottomSideBase64 = "right"
        override val frontSideBase64 = "right"
        override val backSideBase64 = "right"
    }

     */
}