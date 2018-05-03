class Player(var x: Int, var y: Int, var length: Float) {
    fun show() {
        Main.self.apply {
//            ellipseMode(PConstants.CENTER)
//            ellipse((x) * tileSize + tileSize / 2, y * tileSize + tileSize / 2, tileSize, tileSize)
            fill(0f, 0f, 0f)
            rect(x * tileSize, y * tileSize, length, length)
            fill(255f, 255f, 255f)
            rect(x * tileSize, y * tileSize, length / 2, length / 2)
        }
    }
}