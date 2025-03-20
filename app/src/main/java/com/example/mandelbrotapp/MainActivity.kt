package com.example.mandelbrotapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.mandelbrotapp.ui.theme.MandelbrotAPPTheme
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.asImageBitmap
import android.graphics.Bitmap
import kotlin.math.*


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val rightSuperior = Pair(0.285413f, -0.007440f)
        val leftInferior = Pair(0.278587f, -0.012560f)
        val numOfIterations = 50000L
        val width = 500
        val height = 500
        val firstColor = 0xFFFF0000.toInt() //red
        val secondColor = 0xFFFFFF00.toInt() //yellow

        val inicioExc =
            mandelBrotDefiner( //devolve um objeto q tem duas listas: a matriz preto e branco e o k com as qtdds de iterações
                iteracoes = numOfIterations,
                rightSuperior = rightSuperior,
                leftInferior = leftInferior,
                widthImageSize = width,
                heightImageSize = height
            )


        val matrizPretoEBranco: IntArray = inicioExc.imgArray //
        val matrizColorida =
            imagemColorida(
                iterations = numOfIterations,
                color1 = firstColor,
                color2 = secondColor,
                whiteAndBlackImage = inicioExc
            )
        setContent {
            MandelbrotAPPTheme {
                MandelbrotImage(matrizColorida, width, height)
            }
        }
    }
}

class MandelbrotComLimites(
    val imgArray: IntArray,
    val kArray: LongArray
)

@Composable
fun MandelbrotImage(pixelData: IntArray, width: Int, height: Int) {
    val bitmap = Bitmap.createBitmap(pixelData, width, height, Bitmap.Config.ARGB_8888)
    Image(
        bitmap.asImageBitmap(),
        contentDescription = "Mandelbrot Fractal",
        modifier = Modifier.fillMaxSize()
    )
}


fun imagemColorida(
    iterations: Long,
    color1: Int,
    color2: Int,
    whiteAndBlackImage: MandelbrotComLimites,
): IntArray {
    var valoresValidos = whiteAndBlackImage.kArray.filter { it > 0 && it < iterations }
    val kMax = valoresValidos.maxOrNull() ?: iterations - 1
    val kMin = valoresValidos.minOrNull() ?: 0

    val matrizColorida = whiteAndBlackImage.imgArray.copyOf()
    var ctg = 0
    matrizColorida.forEach {
        if (it == 0xFF000000.toInt()) {
            //se for matriz preto e branco, nao precisa fazer nada com quem já é preto.
            ctg++
        } else {
            matrizColorida[ctg] = confeccionarCor(
                color1,
                color2,
                ponderacaoLogaritmica(kMax, kMin, whiteAndBlackImage.kArray[ctg]).first
            )
            ctg++
        }
    }
    return matrizColorida
}

fun confeccionarCor(color1: Int, color2: Int, p1: Float): Int {
    //aux cores
    val a1 = (color1 shr 24) and 0xFF
    val r1 = (color1 shr 16) and 0xFF
    val g1 = (color1 shr 8) and 0xFF
    val b1 = color1 and 0xFF
    val a2 = (color2 shr 24) and 0xFF
    val r2 = (color2 shr 16) and 0xFF
    val g2 = (color2 shr 8) and 0xFF
    val b2 = color2 and 0xFF
    val ap = (a1 * p1 + a2 * (1 - p1)).toInt()
    val rp = (r1 * p1 + r2 * (1 - p1)).toInt()
    val gp = (g1 * p1 + g2 * (1 - p1)).toInt()
    val bp = (b1 * p1 + b2 * (1 - p1)).toInt()
    return (ap shl 24) or (rp shl 16) or (gp shl 8) or bp
}

fun ponderacaoLogaritmica(kMax: Long, kMin: Long, kc: Long): Pair<Float, Float> {
    //aux cores
    //É do enunciado!!
    var p = (log((kc - kMin + 1).toDouble(), 10.toDouble()) / log(
        (kMax - kMin + 1).toDouble(),
        10.toDouble()
    )).toFloat()
    return Pair(1.0f - p, p)
}


fun mandelBrotDefiner(
    iteracoes: Long,
    rightSuperior: Pair<Float, Float>,
    leftInferior: Pair<Float, Float>,
    widthImageSize: Int,
    heightImageSize: Int,
): MandelbrotComLimites {
    //preto e branco!!
    val Dy = rightSuperior.second - leftInferior.second
    val Dx = rightSuperior.first - leftInferior.first
    val varY = Dy / heightImageSize
    val varX = Dx / widthImageSize
    val k = LongArray(widthImageSize * heightImageSize) { 0L }

    val arrayIntToImage = IntArray(widthImageSize * heightImageSize) { 0xFFFFFFFF.toInt() }

    var yIte = rightSuperior.second
    for (i in 0 until heightImageSize) {
        var xIte = leftInferior.first
        for (j in 0 until widthImageSize) {
            val result = pertenceAoMandelbrot(xIte, yIte, iteracoes)
            if (result.first) {
                arrayIntToImage[i * widthImageSize + j] = 0xFF000000.toInt()
            }
            k[i * widthImageSize + j] = result.second
            xIte += varX
        }
        yIte -= varY
    }

    return MandelbrotComLimites(
        imgArray = arrayIntToImage,
        kArray = k
    )
}

fun pertenceAoMandelbrot(
    x: Float,
    y: Float,
    qtdIteracoes: Long
): Pair<Boolean, Long> {
    var k: Long = 0
    var xAnt = 0.0f
    var yAnt = 0.0f
    var xis = x
    var yps = y
    repeat(qtdIteracoes.toInt()) {
        xis = (xAnt * xAnt) - (yAnt * yAnt) + x
        yps = 2 * xAnt * yAnt + y
        if (sqrt(xis * xis + yps * yps) > 2.0) {
            return Pair(false, k - 1)
        } else {
            xAnt = xis
            yAnt = yps
            k = k + 1
        }
    }
    return Pair(true, k)
}

fun printarMatrizNoTerminal(matriz: IntArray, colunas: Int) {
    for (i in 0 until matriz.size / colunas) {
        for (j in 0 until colunas) {
            if (matriz[i * colunas + j] == 0x00FFFFFF.toInt()) {
                print("o")
            } else {
                print(".")
            }
        }
        print("\n")
    }
}