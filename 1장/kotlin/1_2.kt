@SpringBootApplication

class DemoApplication

fun main(args: Array<String>){
    runApplication<DemoApplication>(*args)
}

@RestControlller
class GreetingResource{
    @Getmapping
    fun index(): List<Greeting> = listOf(
        Greeting(1, "Hello!"),
        Greeting(2, "Bonjour!"),
        Greeting(3, "Guten Tag!"),
    )
}

data class Greeting(val id: Int, val text: String)