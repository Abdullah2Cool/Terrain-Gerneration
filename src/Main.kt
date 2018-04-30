import processing.core.PApplet
import processing.core.PConstants
import kotlin.math.roundToInt

class Main : PApplet() {
    var scrWidth: Int = 1280
    var scrHeight: Int = 800
    var tileSize: Int = 10
    var rows: Int = scrWidth / tileSize
    var cols: Int = scrHeight / tileSize
    var xOff = 10000f
    var yOff = 10000f
    var speed = 1f
    var resolution = 1
    var allKeys = BooleanArray(512) {
        false
    }

    var Grid = Array(cols) {
        val y = it.toFloat()
        Array(rows) {
            val x = it.toFloat()
            Tile(x * tileSize.toFloat(), y * tileSize.toFloat(), tileSize.toFloat(), getType(x / tileSize, y / tileSize))
        }
    }

    lateinit var player: Player

    override fun settings() {
        size(scrWidth, scrHeight)
//        loop@ for (y in 0 until cols) {
//            for (x in 0 until rows) {
//                if (Grid[y][x].type == 0) {
//                    player = Player(x.toFloat(), y.toFloat(), tileSize.toFloat())
//                    println("Here")
//                    break@loop
//                }
//            }
//        }
        Grid[cols / 2][rows / 2].setData(Pair(Triple(0f, 0f, 0f), 0))
        player = Player(rows / 2f, cols / 2f, tileSize.toFloat())
    }

    override fun draw() {
        background(0)
        noStroke()
        if (frameCount % 2 == 0) {
            if (allKeys[PConstants.UP]) {
                if (!checkMove(player.x, player.y - 1)) yOff -= speed
            } else if (allKeys[PConstants.DOWN]) {
                if (!checkMove(player.x, player.y + 1)) yOff += speed
            } else if (allKeys[PConstants.LEFT]) {
                if (!checkMove(player.x - 1, player.y)) xOff -= speed
            } else if (allKeys[PConstants.RIGHT]) {
                if (!checkMove(player.x + 1, player.y)) xOff += speed
            }
            for (y in 0 until cols) {
                for (x in 0 until rows) {
                    val newX = (x + xOff)
                    val newY = (y + yOff)
//                    val bHorizontal = (newX % 1 == 0f)
//                    val bVertical = (newY % 1 == 0f)
                    Grid[y][x].setData(getType(newX / tileSize / 2, newY / tileSize / 2))
                }
            }
        }
        for (y in 0 until cols) {
            for (x in 0 until rows) {
                Grid[y][x].draw()
            }
        }

        player.show()

//        println(frameRate)
    }

    fun getType(x: Float, y: Float): Pair<Triple<Float, Float, Float>, Int> {
        val n = noise(x, y) * 255f
        var r = 0f
        var g = 0f
        var b = 0f
        var type = 0

        val t = n - (n % resolution)
        val depth = 100
        if (n < 100) {
            r = 0f * (n / depth) + t
            g = 0f * (n / depth) + t
            b = 255f * (n / depth) + t
            type = 3
        } else {
//            r = t / 5
//            g = t
//            b = 255 - t
            r = 50 * (n / depth) + t
            g = 255 * (n / depth) + t
            b = 0 * (n / depth) + t
        }

//        when (n) {
//            in 0..51 -> {
////                r = 255f
////                g = 174f
////                b = 3f
//                r = 51f
//                g = 51f
//                b = 51f
//                type = 1
//            }
//            in 51..102 -> {
////                r = 230f
////                g = 127f
////                b = 13f
//                r = 102f
//                g = 102f
//                b = 102f
//                type = 1
//            }
//            in 102..153 -> {
////                r = 254f
////                g = 78f
////                b = 0f
//                r = 153f
//                g = 153f
//                b = 153f
//                type = 1
//            }
//            in 153..204 -> {
////                r = 255f
////                g = 15f
////                b = 128f
//                r = 204f
//                g = 204f
//                b = 204f
//                type = 1
//            }
//            in 204..255 -> {
////                r = 233f
////                g = 25f
////                b = 15f
//                r = 255f
//                g = 15f
//                b = 128f
//                type = 3
//            }
//        }
        return Pair(Triple(r, g, b), type)
    }

    override fun keyPressed() {
        allKeys[keyCode] = true
//        println(keyCode)
        if (keyCode == 87) {
            resolution++
        } else if (keyCode == 83) {
            resolution--
        }
    }

    override fun keyReleased() {
        allKeys[keyCode] = false
    }

    fun checkMove(x: Float, y: Float): Boolean {
        return Grid[y.roundToInt()][x.roundToInt()].type == 3
    }

    companion object {
        lateinit var self: Main
    }

    init {
        self = this
    }
}

fun main(args: Array<String>) {
    val processingArgs = arrayOf("mySketch")
    val mySketch = Main()
    PApplet.runSketch(processingArgs, mySketch)
}