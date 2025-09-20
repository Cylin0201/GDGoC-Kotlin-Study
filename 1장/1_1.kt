data class Person(
    val name: String,
    val age: Int? = null
)

fun main(){
    val persons = listOf(
        Person("영희", age = 29),
        Person("철수"),
    )
}