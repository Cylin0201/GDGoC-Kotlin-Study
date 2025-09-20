enum class Color(
    val r: Int,
    val g: Int,
    val b: Int
) {
    RED(255, 0, 0),       // 빨강
    ORANGE(255, 165, 0),  // 주황
    YELLOW(255, 255, 0),  // 노랑
    GREEN(0, 128, 0),     // 초록
    BLUE(0, 0, 255),      // 파랑
    INDIGO(75, 0, 130),   // 남색
    VIOLET(238, 130, 238); // 보라

    fun rgb() = (r * 256 + g) * 256 + b
    fun printColor() = println("$this is ${rgb()}")
}

fun main(){
    println(Color.BLUE.rgb())
    Color.GREEN.printColor()

}
