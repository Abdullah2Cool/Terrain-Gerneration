import processing.core.PApplet
import processing.core.PConstants

class Main : PApplet() {
    var scrWidth: Int = 1200
    var scrHeight: Int = 800
    var tileSize: Float = 10f
    var rows: Int = scrWidth / tileSize.toInt()
    var cols: Int = scrHeight / tileSize.toInt()
    var xOff = 0f
    var yOff = 0f
    var speed = 1f
    var resolution = 170f
    var sampleSize = tileSize * 8
    var angle = 0f
    var changes = HashMap<Pair<Int, Int>, Int>()
    var allKeys = BooleanArray(512) {
        false
    }

    var Grid = Array(cols) {
        val y = it
        Array(rows) {
            val x = it
            Tile(x, y, tileSize, getData(x / tileSize, y / tileSize))
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
        player = Player(Grid[cols / 2][rows / 2].x, Grid[cols / 2][rows / 2].y, tileSize)
//        player = Player(0, 0, tileSize)
    }

    override fun draw() {
        background(0)
        noStroke()


        for (i in 0 until speed.toInt()) {
            for (y in 0 until cols) {
                for (x in 0 until rows) {
                    val newX = (x + xOff)
                    val newY = (y + yOff)
                    Grid[y][x].setData(getData(newX, newY))
                }
            }

            val offset = (player.length / tileSize).toInt()
            val up = checkMove(0, offset)
            val down = checkMove(1, offset)
            val left = checkMove(2, offset)
            val right = checkMove(3, offset)
            when {
                allKeys[PConstants.UP] -> if (up) yOff -= 1
                allKeys[PConstants.DOWN] -> if (down) yOff += 1
                allKeys[PConstants.LEFT] -> if (left) xOff -= 1
                allKeys[PConstants.RIGHT] -> if (right) xOff += 1
            }
        }

        for (y in 0 until cols) {
            for (x in 0 until rows) {
                Grid[y][x].draw()
            }
        }

        player.show()
//        showLight()
        angle += 0.002f
        if (frameRate < 20) println(frameRate)
    }

    fun showLight() {
        val lightSize = 10
        loadPixels()
        loop@ for (y in 0 until height) {
            for (x in 0 until width) {
                val index = x + y * width
                if (x > width / lightSize && x < (width - width / lightSize) && y > (height / lightSize) && y < (height - height / lightSize)) {
                    val c = pixels[index]
                    val r = red(c)
                    val g = green(c)
                    val b = blue(c)
                    val radius = 0.78f
//                var dist = (abs(x - width / 2f) + abs(y - height / 2f)) * radius
                    var dist = dist(x.toFloat(), y.toFloat(), width / 2f, height / 2f) * radius
                    pixels[index] = color(r - dist, g - dist, b - dist)
                } else {
                    pixels[index] = color(0f)
                }

            }
        }
        updatePixels()
    }

    fun getData(x: Float, y: Float): Pair<Triple<Float, Float, Float>, Int> {

        if (changes.containsKey(Pair(x.toInt(), y.toInt()))) {
            return Pair(Triple(0f, 0f, 0f), 3)
        }

        val n = noise(x / sampleSize, y / sampleSize) * 255f
        var r = 0f
        var g = 0f
        var b = 0f
        var type = 0

//        resolution = sin(angle) * 255 + 1

        var t = n - (n % resolution)
        val depth = 100
        val u = (n / depth)
        when {
            n < 100 -> {
                r = 0 * u + t
                g = 102 * u + t
                b = 255 * u + t
                type = 3
            }
            else -> {
                r = 51 * u + t
                g = 204 * u + t
                b = 51 * u + t
            }
        }

        return Pair(Triple(r, g, b), type)
    }

    override fun mouseDragged() {
        val m = Pair((mouseX / tileSize).toInt(), (mouseY / tileSize).toInt())
        for (y in 0 until cols) {
            for (x in 0 until rows) {
                val key = Pair((x + xOff).toInt(), (y + yOff).toInt())
                val index = Pair(x, y)
                if (m == index) {
                    if (changes.containsKey(key)) {
//                        println("Already changed.")
//                        println(changes)
                    } else {
                        changes[key] = 0
                    }
                }
            }
        }


    }

    override fun keyPressed() {
        allKeys[keyCode] = true
//        println(keyCode)
//        println("Resolution: $resolution")
//        println(xOff, yOff)
        when (keyCode) {
            87 -> sampleSize *= 1f + 0.01f
            83 -> sampleSize *= 1f - 0.01f
        }
    }

    override fun keyReleased() {
        allKeys[keyCode] = false
    }

    fun checkMove(dir: Int, offset: Int): Boolean {
        return when (dir) {
            0 -> // up
            {
                var verdict = true
                for (j in 0 until offset) {
                    val x = player.x + j
                    val y = player.y - 1
                    if (!isMovable(x, y)) {
                        Grid[y][x].myColor = Triple(255f, 0f, 0f)
                        verdict = false
                    }
                }
                verdict
            }
            1 -> // down
            {
                var verdict = true
                for (j in 0 until offset) {
                    val x = player.x + j
                    val y = player.y + offset
                    if (!isMovable(x, y)) {
                        Grid[y][x].myColor = Triple(255f, 0f, 0f)
                        verdict = false
                    }
                }
                verdict
            }
            2 -> // left
            {
                var verdict = true
                for (j in 0 until offset) {
                    val x = player.x - 1
                    val y = player.y + j
                    if (!isMovable(x, y)) {
                        Grid[y][x].myColor = Triple(255f, 0f, 0f)
                        verdict = false
                    }
                }
                verdict
            }
            else -> // right
            {
                var verdict = true
                for (j in 0 until offset) {
                    val x = player.x + offset
                    val y = player.y + j
                    if (!isMovable(x, y)) {
                        Grid[y][x].myColor = Triple(255f, 0f, 0f)
                        verdict = false
                    }
                }
                verdict
            }
        }
    }

    fun isMovable(x: Int, y: Int): Boolean {
        return Grid[y][x].type != 3
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