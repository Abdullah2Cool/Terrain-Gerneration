import processing.core.PConstants

class Player(var x: Float, var y: Float, var rad: Float) {

    fun show() {
        Main.self.apply {
            ellipseMode(PConstants.CENTER)
            fill(0f, 0f, 0f)
            ellipse((x) * rad + rad / 2, y * rad + rad / 2, rad, rad)
        }
    }
}