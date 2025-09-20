fun main(){
    val input = readln()
    val name = if(input.isNotBlank()) input else "Kotlin"
    println("Hello, $name!")
}