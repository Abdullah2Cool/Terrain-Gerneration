class Player(var x: Int, var y: Int, var length: Float) {
    fun show() {
        Main.self.apply {
            fill(0f, 0f, 0f)
            rect(x * tileSize, y * tileSize, length, length)
            fill(255f, 255f, 255f)
//            rect(x * tileSize, y * tileSize, length / 2, length / 2)
            ellipse(x * tileSize + length / 2, y * tileSize + length / 2, length - 4, length - 4)
        }
    }
}