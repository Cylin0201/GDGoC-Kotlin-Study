# 5장 람다를 사용한 프로그래밍

## 5.1 람다식과 멤버 참조
함수를 값처럼 다루기... 클래스를 선언하고 그 클래스의 인스턴서를 함수에 넘기는 것이 아니라 함수를 직접 다른 함수에 전달한다.
이를 통해 간결한 코드를 만들 수 있음.

```kotlin
//5.1 object 선언으로 리스너 구현하기
button.setOnClickListener(object : OnClickListener{
    override fun onClick(v :View) = println("I was clicked!")
})

//5.2 람다로 리스너 구현하기
button.setOnClickListenr {
    println("I was clicked!")
}
```
5.2 코드는 자바 익명 내부 클래스와 같은 역할을 하지만 훨씬 간결하고 읽기 쉽다.

### 컬렉션에서 람다
for 루프를 통해 Person 객체를 담은 컬렉션에서 가장 큰 나이의 객체를 뽑아내는 람다식을 작성하면 아래와 같다.
```kotlin
//5.4 maxByNull 함수를 사용해 컬렉션 검색
fun main(){
    val people = listOf(Person("Alice", 29), Person("Bob", 31))
    println(people.maxByOrNull{it.age}) // age를 비교해서 가장 큰 원소 찾기
}
```
훨씬 간결하고, 보기에 편한 것을 알 수 있다.

모든 컬렉션에 대해 maxByOrNull 함수를 통해 가장 큰 원소를 찾기 위해 비교에 사용할 값을 돌려주는 함수이다. 
람다가 인자를 하나만 받고 그 인자에 대한 구체적 이름을 붙이고 시지 않기 때문에 it라는 암시적 이름을 사용한다.

### 람다식의 문법
코틀린에서 람다식은 항상 중괄호로 둘러싸여 있다. 인자 주변에 괄호가 없다는 것이 중요하다. 
```kotlin
fun main() {
    val sum = { x: Int, y: Int -> x + y }
    println(sum(1, 2)) //3
}
```
식이 필요한 부분에서 run이 아주 유용하다. 코드의 일부분을 블록으로 둘러싸 실행할 필요가 있다면 run을 사용하라.
run은 인자로 받은 람다를 실행해 주는 라이브러리 함수다.

위 예제에서 코틀린이 코드를 줄여쓸 수 있게 제공했던 기능을 제거하고 정식으로 람다를 작성하면 아래와 같다.
```kotlin
//1.
people.maxByOrNull({p: Person -> p.age })

//2. 함수 호출 시 맨 뒤에 있는 인자가 람다식이라면 그 람다를 괄호 밖으로 빼낼 수 있다는 문법적인 관습 적용 
people.maxByOrNull(){p: Person -> p.age }

//3. 빈 괄호 삭제
people.maxByOrNull{ p:Person -> p.age }
```
마지막 3번 코드가 가장 읽기 쉽다. 람다가 함수의 유일한 인자라면 괄호 없이 람다를 바로 쓰는 것이 더 좋다. 

```kotlin
//4. 람다 파라미터 타입 제거하기
people.maxByOrNull {p -> p.age} //컴파일러가 파라미터 타입 추론

//5. 디폴트 파라미터 이름 it 사용하기
people.maxByOrNull { it.age }
```

>  **[노트]**
> 
> it를 사용하면 코드를 간단하게 만들 수는 있지만, 이를 남용해서는 안된다. 
> 특히 람다 안에 람다가 내포되는 경우 각 람다의 파라미터를 명시하는 편이 낫다.
> 파라미터를 명시하지 않으면 각각의 it가 가리키는 파라미터가 어떤 람다에 속했는지 파악하기 어렵다.


### 멤버 참조
람다를 사용해 코드 블록을 다른 함수에 인자로 넘기려고 한 코드가 이미 함수로 선언된 경우에는 어떻게 할까? 코틀린은 자바와 마찬가지로 함수를 값으로 바꿀 수 있다.

```kotlin
val getAge = Person::age

//val getAge = {person: Person -> person.age}와 동일.
```
와 같은 문법을 사용한다. 이처럼 ::를 사용하는 식을 멤버 참조라 부른다. 멤버 참조는 정확히 한 메소드를 호출하거나 한 프로퍼티에 접근하는 함수 값을 만들어준다.

최상위 함수나 프로퍼티를 참조할 때에는 클래스 이름을 생략하고 ::으로 참조를 바로 시작한다. 생성자 참조를 사용하기 위해서 :: 뒤에 클래스 이름을 넣으면 생성자 참조를 만들 수 있다.

## 5.2 자바의 함수형 인터페이스 사용: 단일 추상 메서드
### 람다를 자바 메서드의 파라미터로 전달
코틀린에서 함수형 인터페이스를 파라미터로 받는 모든 자바 메서드에 람다를 전달할 수 있다.

```Java
void postponeComputation(int delay, Runnable computation);
```
코틀린에서는 이 함수를 호출할 때 람다를 인자로 보낼 수 있다. 컴파일러는 자동으로 람다를 Runnable 인스턴스로 변환한다.
```kotlin
postponeComputation(1000){println(42)} // 전체 프로그램에 Runnable 인스턴스가 하나만 만들어진다.
```
컴파일러는 개발자 대신 익명 클래스 인스턴스를 만들고 람다를 그 인스턴스의 유일한 추상 메서드의 본문으로 만들어준다.

### SAM 변환: 람다를 함수형 인터페이스로 명시적 변환
SAM(Single Abstract Method) 생성자는 컴파일러가 생성한 함수로 람다를 단일 추상 메서드 인터페이스의 인스턴스로 명시적으로 변환한다.
만약에 함수형 인터페이스의 인스턴스를 반환해야 하는 함수가 있다면 람다를 직접 반환할 수 없는데, 대신에 람다를 SAM 생성자로 감싸야 한다.

```kotlin
//5.12 값을 반환하기 위해 SAM 생성자 사용하기
fun createAllDoneRunnable() :Runnable{ 
    return Runnable{print("All done!")} //사용하려는 함수형 인터페이스의 이름과 동일!
}
fun main(){
    createAllDoneRunnable().run()
}
```

## 5.3 코틀린에서 SAM 인터페이스 정의: fun interface
코틀린에서는 fun interface를 정의하면 자신만의 함수형 인터페이스를 정의할 수 있다.
코틀린의 함수형 인터페이스는 정확히 하나의 추상 메서드만을 포함하지만 다른 비추상 메서드를 여럿 가질 수 있다.

```kotlin
//5.14 추상메서드가 단 하나만 들어있는 코틀린 함수형 인터페이스
fun interface IntCondition {
    fun check(i: Int): Boolean
    fun checkString(s: String) = check(s.toInt())
    fun checkChar(c: Char) = check(c.digitToInt())
}
```
아래는 IntCondition을 파라미터로 받는 checkCondition 함수를 보여준다. 
이 함수를 람다를 직접 전달하거나 올바른 타입의 함수에 대한 참조를 전달할 수 있다.

```kotlin
fun checkCondition(i: Int, condition: IntCondition): Boolean {
    return condition.check(i)
}

fun main() {
    checkCondition(1) { it % 2 != 0 } //람다를 직접 사용
    val isOdd: (Int) -> Boolean = {it % 2 != 0}
    checkCondition(1, isOdd)    //... 시그니처가 일치하는 람다에 대한 참조를 사용할 수 없다.
}
```
## 5.4 수신 객체 지정 람다: with, apply, also
### with 함수
코틀린에서는 어떤 객체의 이름을 반복하지 않고도 그 객체에 대한 다양한 연산을 수행하는 기능을 제공한다. 
이 때 with이라는 라이브러리 함수를 통해 제공하는데, 아래 코드를 참고하자.

```kotlin
//5.16 알파벳 만들기 
fun alphabet() : String{
    val result = StringBuilder()
    for (letter in 'A'..'Z'){
        result.append(letter)
    }
    result.append("\nNow I know the alphabet!")
    return result.toString()
}

// 5.17 with을 사용해 알파벳 만들기
fun alphabet() : String{
    val sb = StringBuilder()
    return with(sb){    //sb가 this가 된다.
        for (letter in 'A'..'Z'){
            this.append(letter)
        }
        this.append("\nNow I know the alphabet!")
        this.toString() //with 결과를 반환한다.
    }
}
```
여기서 with의 첫 번째 인자는 sb이고, 두 번째 인자는 람자이다.
물론 this를 생략할 수도 있다.

### apply 함수 
apply 함수는 거의 with과 동일하게 작동한다. 유일한 차이는 apply는 항상 자신에 전달된 객체를 반환한다는 점뿐이다.
```kotlin
//5.20 apply를 사용해 알파벳 만들기
fun alphabet() = StringBuilder().apply{
    for (letter in 'A'..'Z'){
        append(letter)
    }
    append("\nNow I know the alphabet!")
}.toString()
```
apply를 임의의 타입의 확장 함수로 호출할 수 있다. apply를 호출한 객체는 apply에 저달딘 람다의 수신 객체가 된다. 
이러한 결과는 StringBuilder이므로 나중에 toString을 호출해서 String 객체를 얻을 수 있다.

buildString, buildList, buildSet, buildMap의 함수는 String이나 컬렉션을 생성하지만 생성 과정에서는 가변 컬렉션인 것 처럼 다루고 싶을 때 도움이 되는 빌더 함수이다.

### also
also도 마찬가지로 수신 객체를 받으며, 그 수신 객체애 대해 특정 동작을 수행하고 다시 돌려준다. 
주된 차이는 also의 람다 안에서는 수신 객체를 인자로 참조한다는 점이다.

```kotlin
//5.24 also를 사용해 효과를 추가로 적용하기
fun main(){
    val fruits = listOf("Apple", "Banana", "Cherry")
    val uppercaseFruits = mutableListOf<String>()
    val reversedLongFruits = fruits
        .map{it.uppercase()}
        .also{uppercaseFruits.addAll(it)}
        .filter {it.length > 5}
        .also {println(it)}
        .reversed()
}

```