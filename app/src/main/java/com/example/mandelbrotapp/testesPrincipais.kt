package com.example.mandelbrotapp

import kotlin.math.*

fun main() {
    mandelBrotParte01(50000, -1.5f, -1.0f, 0.5f, 1.0f)
}

fun mandelBrotParte01(
    iteracoes: Long,
    xInfEsq: Float,
    yInfEsq: Float,
    xSupDir: Float,
    ySupDir: Float
) {
    val tamImgY = 20 // Altura da imagem (Linhas - Eixo Y)
    val tamImgX = 80 // Largura da imagem (Colunas - Eixo X)

    val Dy = ySupDir - yInfEsq
    val Dx = xSupDir - xInfEsq
    val varY = Dy / tamImgY
    val varX = Dx / tamImgX

    val matrizFinal: MutableList<MutableList<String>> =
        MutableList(tamImgY) { MutableList(tamImgX) { "." } }

    var yIte = ySupDir
    for (i in 0 until tamImgY) {
        var xIte = xInfEsq
        for (j in 0 until tamImgX) {
            if (pertenceAoMandelbrot(xIte, yIte, iteracoes)) {
                matrizFinal[i][j] = "o"
            }
            xIte += varX
        }
        yIte -= varY
    }

    printarMatrizNoTerminal(matrizFinal)
}


fun printarMatrizNoTerminal(matriz: MutableList<MutableList<String>>) {
    for (i in 0 until matriz.size) {
        for (j in 0 until matriz[i].size) {
            print(matriz[i][j])
        }
        print("\n")
    }
}

fun pertenceAoMandelbrot(
    x: Float,
    y: Float,
    qtdIteracoes: Long
): Boolean {
    var xAnt = 0.0f
    var yAnt = 0.0f
    var xis = x
    var yps = y
    repeat(qtdIteracoes.toInt()) {
        xis = (xAnt * xAnt) - (yAnt * yAnt) + x
        yps = 2 * xAnt * yAnt + y
        if (sqrt(xis * xis + yps * yps) > 2.0) {
            return false
        } else {
            xAnt = xis
            yAnt = yps
        }
    }
    return true
}