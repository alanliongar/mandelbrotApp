/*
import java.awt.image.BufferedImage
import java.awt.Color
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.*

fun main() {
    val width = 200
    val height = 200
    val iteracoes = 50000L
    val rightSuperior = Pair(0.8f, 1.0f)
    val leftInferior = Pair(-1.5f, -1.0f)
    val firstColor = 0xFFFFFFFF.toInt() // branco - cor do buddha
    val secondColor = 0xFF000000.toInt() // preto - cor do fundo

    val mandelbrot = mandelBrotDefiner(iteracoes, rightSuperior, leftInferior, width, height)
    val mandelbrotColorido =
        imagemColorida(iteracoes, firstColor, secondColor, mandelbrot, "simple")
    val buddha = conjuntoBuddhaBrot(
        fator = 5,
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


fun weightImageWithMath(
    cMax: Long,
    cMin: Long,
    cCont: Long,
    typeMath: String = "simple"
): Pair<Float, Float> {
    if (typeMath == "log") {
        var p = (log((cCont - cMin + 1).toDouble(), 10.toDouble()) / log(
            (cMax - cMin + 1).toDouble(),
            10.toDouble()
        )).toFloat()
        return Pair(1.0f - p, p)
    } else if (typeMath == "gamma") {
        val gamma = 0.6
        val p = (cCont.toDouble() / cMax).pow(gamma).toFloat()
        return Pair(1.0f - p, p)
    } else {
        val calc = ((cCont - cMin).toDouble() / (cMax - cMin).toDouble())
        val p = min(calc * 2.5, 1.0).toFloat()
        return Pair(1.0f - p, p)
    }

}


fun imagemColorida(
    iterations: Long,
    color1: Int,
    color2: Int,
    whiteAndBlackImage: MandelbrotComLimites,
    typeWeight: String = "log"
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
                weightImageWithMath(
                    kMax,
                    kMin,
                    whiteAndBlackImage.kArray[ctg],
                    typeMath = typeWeight
                ).first
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


class MandelbrotComLimites(
    val imgArray: IntArray,
    val kArray: LongArray
)

fun conjuntoBuddhaBrot(
    fator: Int,
    mandelbrotComLimites: MandelbrotComLimites,
    iteracoes: Long,
    rightSuperior: Pair<Float, Float>,
    leftInferior: Pair<Float, Float>,
    width: Int,
    height: Int
): Pair<LongArray, LongArray> {
    val Dy = rightSuperior.second - leftInferior.second
    val Dx = rightSuperior.first - leftInferior.first
    val varPixelsY = Dy / (height * fator)
    val varPixelsX = Dx / (width * fator)
    val stepY = varPixelsY * fator
    val stepX = varPixelsX * fator
    val buddhaBrotAll = LongArray(width * height) { 0L }
    val buddhaBrotBelonged = LongArray(width * height) { 0L }

    // Amostragem: centro de cada pixel
    var yIte = rightSuperior.second - 0.5f * (stepY)
    for (i in 0 until height * fator) {
        var xIte = leftInferior.first + 0.5f * (stepX)
        for (j in 0 until width * fator) {
            var xAnt = 0.0f
            var yAnt = 0.0f
            var xis: Float
            var yps: Float
            val result = pertenceAoMandelbrot(xIte, yIte, iteracoes)
            if (!result.first) {
                for (k in 0..iteracoes) {
                    xis = (xAnt * xAnt) - (yAnt * yAnt) + xIte
                    yps = 2 * xAnt * yAnt + yIte
                    if (xis * xis + yps * yps <= 4.0f) {
                        if (xis in leftInferior.first..rightSuperior.first &&
                            yps in leftInferior.second..rightSuperior.second
                        ) {
                            val pixelX = floor((xis - leftInferior.first) / (stepX)).toInt()
                                .coerceIn(0, width - 1)
                            val pixelY = floor((rightSuperior.second - yps) / (stepY)).toInt()
                                .coerceIn(0, height - 1)

                            if (pixelX in 0 until width && pixelY in 0 until height) {
                                buddhaBrotAll[pixelY * width + pixelX]++
                            }

                            xAnt = xis
                            yAnt = yps
                        }
                    } else {
                        break
                    }
                }
            } else {
            }

            xIte += varPixelsX
        }
        yIte -= varPixelsY
    }

    return Pair(buddhaBrotAll, buddhaBrotBelonged)
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

    var yIte = rightSuperior.second - 0.5f * Dy
    for (i in 0 until heightImageSize) {
        var xIte = leftInferior.first + 0.5f * Dx
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
*/
