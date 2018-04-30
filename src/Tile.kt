class Tile(var x: Float, var y: Float, var size: Float, var myData: Pair<Triple<Float, Float, Float>, Int>) {
    var myColor = myData.first
    var type = myData.second
    fun draw() {
        Main.self.apply {
//            fill(myColor.first, myColor.second, myColor.third)
            fill(255 - myColor.first, 255 - myColor.second, 255 - myColor.third)
            rect(x, y, size, size)
//            fill(myColor.first, myColor.second, myColor.third)
//            val s2 = size + 2
//            ellipse(x + s2 / 2, y + s2 / 2, s2, s2)
        }
    }

    fun setData(data: Pair<Triple<Float, Float, Float>, Int>) {
        myColor = data.first
        type = data.second
    }
}