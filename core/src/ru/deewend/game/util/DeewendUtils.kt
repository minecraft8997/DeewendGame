package ru.deewend.game.util

import java.io.BufferedReader
import java.io.File

object DeewendUtils {
    fun readFirstLine(p: String): String {
        val f = File(p)
        val r = BufferedReader(f.reader())
        val l: String = r.readLine()
        r.close()
        return l
    }
}