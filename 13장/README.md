# 13장. DSL 만들기 (Domain Specific Language)
## 13.1 API to DSL
DSL의 궁극적인 목표는 코드의 가독성과 유지 보수성을 가장 좋게 유지하는 것이다.
특히 클래스 간 상호작용이 일어나는 API를 훌륭하게 만들어내는 것이 중요하다.

### 13.1.1 DSL
가장 잘 알려진 DSL은 SQL과 정규식이다. DSL이 제공하는 기능을 제한함으로써 오히려 더 효율적으로 자신의 목표를 달성할 수 있다는 점이 있다.
DSL를 통해 범용 언어(Java, Kotlin)를 사용하는 경우보다 특정 영역에 대한 연산을 더 간결하게 기술할 수 있다.

### 13.1.3 DSL의 구조
코틀린 DSL에서는 보통 람다를 내포시키거나 메서드 호출을 연쇄시키는 방식으로 구조를 만든다. SQL -> Kotlin DSL 예제에서도 살표 볼 수 있다.
이러한 구조의 장점은 같은 맥락을 매 함수 호출 시마다 반복하지 않고도 재사용할 수 있다는 것이다.

```kotlin
//13.1 람다를 인자로 받는 buildString() 정의
fun buildString(
    buildAction: (StringBuilder) -> Unit
): String{
    val s = buildString()
    buildAction(sb) //람다 인자로 StrubgBuilder 인스턴스를 넘긴다.
    return sb.toString()
}

fun main(){
    val s = buildString { 
        it.append("Hello, ")
        it.append("World!")
    }
    print(s) //Hello, World!
}
```
좀더 편한 방식으로 하기 위해 수신 객체  지정 람다로 바꾸면 아래와 같다.

```kotlin
//13.2 수신 객체 지정 람다를 파라미터로 받는 buildString()
fun buildString(
    builderAction: StringBuilder.() -> Unit
): String{
    val sb = StringBuilder()
    sb.builderAction()
    return sb.toString()
}

fun main(){
    val s = buildString { 
        this.append("Hello, ")
        append("World!") //this 생략 가능
    }
    print(s)
}
```

**HTML 빌더 생략**

## 13.4 실전 코틀린 DSL
### 테스트 프레임워크의 should 함수
코테스트 DSL에서 중위 호출을 활용하는 방식을 살표보자. 아래의 예제에서 testKPrefix 테스트는 s 변수의 값이 K로 시작하지 않으면 실패한다.

```kotlin
//13.18 코테스트 DSL로 단언문 표현하기

class PrefixTest{
    @Test
    fun testKPrefix(){
        val s = "kotlin."uppercase()
        s should strtWith("K")
    }
}
```
이런 문법을 DSL로 사용하려면 should 함수 선언 앞에 infix 변경자를 붙여야 한다.

### SQL을 위한 내부 DSL 
익스포즈드에서 데이터베이스 구조를 정의하는 예제는 아래와 같다.
```kotlin
object Country: Table(){
    val id = integer("id").autoIncrement()
    val name = varchar("name", 50)
    override val primaryKey = PrimaryKey(id)
}
```

이 선언은 DB 테이블과 대응된다. 

```kotlin
//익스포즈드에서 두 테이블 조인하기
val reult = (Country innerJoin Customer)
    .select{Country.name eq "USA"}  //WHERE Country.name = "USA"라는 SQL 코드에 해당.
result.forEach{println(it[Customer.name])}
```