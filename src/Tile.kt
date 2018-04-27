class Tile(var x: Float, var y: Float, var size: Float, var myData: Pair<Triple<Float, Float, Float>, Int>) {
    var myColor = myData.first
    var type = myData.second
    fun draw() {
        Main.self.apply {
            fill(myColor.first, myColor.second, myColor.third)
            rect(x, y, size, size)
        }
    }

    fun setData(data: Pair<Triple<Float, Float, Float>, Int>) {
        myColor = data.first
        type = data.second
    }
}