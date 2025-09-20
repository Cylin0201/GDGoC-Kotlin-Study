data class Person(
    val name: String,
    val age: Int? = null
)

fun main(){
    val persons = listOf(
        Person("영희", age = 29),
        Person("철수"),
    )

    val oldset = persons.maxBy {
        it.age ?: 0
    }
    println("가장 나이가 많은 사람: $oldset")
}