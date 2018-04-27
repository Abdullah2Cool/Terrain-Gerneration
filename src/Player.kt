import processing.core.PConstants

class Player(var x: Float, var y: Float, var rad: Float) {

    fun show() {
        Main.self.apply {
            ellipseMode(PConstants.CORNER)
            fill(255)
            ellipse(x * rad, y * rad, rad, rad)
        }
    }
}