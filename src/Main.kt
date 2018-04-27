import processing.core.PApplet
import processing.core.PConstants
import kotlin.math.roundToInt

class Main : PApplet() {
    var scrWidth: Int = 1200
    var scrHeight: Int = 800
    var tileSize: Int = 10
    var rows: Int = scrWidth / tileSize
    var cols: Int = scrHeight / tileSize
    var xOff = 0f
    var yOff = 0f
    var speed = 1f
    var allKeys = BooleanArray(512) {
        false
    }

    var Grid = Array(cols) {
        val y = it.toFloat()
        Array(rows) {
            val x = it.toFloat()
            Tile(x * tileSize.toFloat(), y * tileSize.toFloat(), tileSize.toFloat(), getType(x, y))
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
        if (frameCount % 4 == 0) {
            when {
                allKeys[PConstants.UP] -> {
                    if (!checkMove(player.x, player.y - 1)) yOff -= speed
                }
                allKeys[PConstants.DOWN] -> {
                    if (!checkMove(player.x, player.y + 1)) yOff += speed
                }
                allKeys[PConstants.LEFT] -> {
                    if (!checkMove(player.x - 1, player.y)) xOff -= speed
                }
                allKeys[PConstants.RIGHT] -> {
                    if (!checkMove(player.x + 1, player.y)) xOff += speed
                }
            }
            for (y in 0 until cols) {
                for (x in 0 until rows) {
                    Grid[y][x].setData(getType(x + xOff, y + yOff))
                }
            }
        }
        for (y in 0 until cols) {
            for (x in 0 until rows) {
                Grid[y][x].draw()
            }
        }

        player.show()

        println(frameRate)
    }

    fun getType(x: Float, y: Float): Pair<Triple<Float, Float, Float>, Int> {
        val n = noise(x, y) * 255f
        val r:Float
        val g:Float
        val b:Float
        var type = 0
        when (n) {
            in 0..100 -> {
                r = 63f
                g = 37f
                b = 11f
                type = 0
            }
            in 100..190 -> {
                r = 27f
                g = 183f
                b = 11f
                type = 1
            }
            else -> {
                r = 173f
                g = 5f
                b = 13f
                type = 3
            }
        }
        return Pair(Triple(r, g, b), type)
    }

    override fun keyPressed() {
        allKeys[keyCode] = true
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