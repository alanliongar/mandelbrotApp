package com.example.mandelbrotapp

import java.awt.image.BufferedImage
import java.awt.Color
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.*

fun main() {
    val width = 1000
    val height = 1000
    val iteracoes = 5000L
    val rightSuperior = Pair(0.5f, 1.0f)
    val leftInferior = Pair(-1.5f, -1.0f)
    val firstColor = 0xFF000000.toInt() // preto
    val secondColor = 0xFFFFFFFF.toInt() // branco

    val mandelbrot = mandelBrotDefiner(iteracoes, rightSuperior, leftInferior, width, height)
    val buddha = conjuntoBuddhaBrot(
        fator = 2,
        mandelbrotComLimites = mandelbrot,
        iteracoes = iteracoes,
        rightSuperior = rightSuperior,
        leftInferior = leftInferior,
        width = width,
        height = height
    )

    val max = buddha.first.maxOrNull() ?: 1L
    val min = buddha.first.minOrNull() ?: 0L

    val image = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)

    for (i in 0 until width * height) {
        val color = confeccionarCor(
            firstColor,
            secondColor,
            weightImageWithMath(max, min, buddha.first[i], "simple").second
        )
        val x = i % width
        val y = i / width
        image.setRGB(x, y, color)
    }

    val output = File("C:\\Users\\Alan\\Desktop\\buddha.png")
    ImageIO.write(image, "png", output)

    println("Imagem gerada em: ${output.absolutePath}")
}
