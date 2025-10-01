# 7장. 널이 될 수 있는 값
## 7.1 NPE를 피하고 값이 없는 경우 처리: 널 가능성
널 가능성은 NPE를 피할 수 있게 돕는 코틀린 타입 시스템의 특성이다.
코틀린을 포함한 최신 언어에서 null에 대한 접근 방법은 가능한 이 문제를 실행 시점에서 컴파일 시점으로 옮기는 것이다.
널이 될 수 있는지 여부를 타입 시스템에 추가함으로써 컴파일러가 컴파일 시에 미리 감지해서 실행 시점에 발생할 수 있는 예외의 가능성을 줄일 수 있다.

## 7.2 널이 딜 수 있는 타입으로 Nullable Variable 명시
```Java
    int strlen(String s){
        return s.length();
}
```
위 함수는 안전하지 않다. s에 null 값이 들어오면 NPE가 발생하기 때문이다.

```kotlin
    fun strlen(s: String) = s.length;
    //이 함수의 파라미터로 null이 들어오는 것은 금지된다. 컴파일 오류 발생함
```

만약에 위 코드에서 null 값을 허용한다면 어떻게 표시할까? 

```kotlin
    fun strlen(s: String?) = s.length;
    //타입 뒤에 ?를 붙이면 null 참조를 저장할 수 있다는 의미임
```
어떤 널이 될 수 있는 타입의 값이 있다면 그 값에 수행할 수 있는 연산의 종류가 제한된다. 예를 들어 널 허용인 타입의 메서드를 호출할 수는 없다.

## 7.3 타입의 의미?
자바의 타입 시스템은 null을 제대로 다루지 못한다. 변수에 선언된 타입이 있지만 null 여부를 추가로 검사하기 전에는 그 변수에 대해 어떤 연산을 수행할 수 있을지 알 수 없다.
**NPE가 존재하는 이유..!!**

코틀린의 널이 될 수 있는 타입은 이런 문제에 대한 해법을 제공한다. 
널이 될 수 있는/없는 타입을 구분하면 어떤 연산이 가능할지 명확히 이해할 수 있다. 

## 7.4 안전한 호출 연산자로 null 검사와 메서드 호출 합치기 : ?.
코틀린이 제공하는 가장 유용한 도구로 안전한 호출 연산자 ?.가 있다. **?.**는 null 검사와 메서드 호출을 한 연산으로 수행한다.

메서드 호출뿐 아니라 프로퍼티를 읽거나 쓸 때도 안전한 호출을 사용할 수 있다.

아래 코드를 참고하자.
```kotlin
//7.2 Nullable Property를 다루기 위해
class Employee(val name: String, val manager: Employee?)

fun managerName(employee: Employee) : String? = employee.manager?.name

fun main(){
    val ceo = Employee("Da Boss", null)
    val developer = Employee("Bob Smith", ceo)
    println(managerName(developer)) //Da Boss
    println(managerName(ced))   //null
}
```

## 7.5 엘비스 연산자로 null에 대한 기본 값 제공 : ?:
코틀린은 **엘비스 연산자라**는 null 대신 사용할 기본값을 지정할 때 편리하게 사용할 수 있는 연산자를 제공한다.
(?:)

다음은 엘비스 연산자를 사용하는 방법이다.

```kotlin
fun greet(name: String) {
    val recipient: String = name ?: "unnamed" //s가 null이면 결과는 빈 문자열이다.
    println("Hello, $recipient")
}
```

코틀린에서는 return이나 throw 등도 식이기 때문에 엘비스 연산자의 오른쪽에 return, throw 등을 넣을 수 있다.
엘비스의 연산자의 왼쪽 값이 null이 되면 함수가 즉시 어떤 값을 반환하거나 예외를 던진다. 

아래는 엘비스 연산자를 활용해서 지정한 사람의 회사 주소를 라벨에 인쇄하는 함수이다.

```kotlin
//7.5 throw와 엘비스 연산자와 함께 사용하기
class Address(val streetAddress: Address, val zipCode: Ont, val city: String, val country: String)

class Company(val name: String, val address: Address?)

class Person(val name: String, val company: Company?)

fun printShippingLabel(person: Person){
    val address = person.company?.address
        ?:throw IllegaArgumentExecption("No address") // 주소가 없으면 예외를 던짐
    with(address){
        println(streetAddress)
        println("$zipCode $city, $country");
    }
}
```
위 함수는 모든 정보가 제대로 있으면 주소를 출력한다. 주소가 없으면 NPE가 아닌 의미 있는 오류를 발생시킨다.
주소가 있다면 라벨은 제대로 구성된다. 

## 7.6 예외를 발생시키지 않고 안전하게 타입 캐스트하기 : as?
코틀린에서 as? 연산자는 어떤 값을 지정한 타입으로 변환한다. as?는 값을 대상 타입으로 변환할 수 없으면 null을 반환한다.
안전한 캐스트를 사용할 때 일반적인 패턴은 캐스트를 수행한 뒤에 엘비스 연산자를 사용하는 것이다.

```kotlin
class Person(val firstName: String, val lastName: String) {
    override fun equals(other: Any?): Boolean {
        val otherPerson = other as? Person ?: return false
        //타입이 서로 일치하지 않으면 false를 반환한다.
        
        return otherPerson.lastName == lastName &&
                otherPerson.firstName == firstName;
    }
}
```
## 7.7 널 아님 단언: !!
느낌표를 이중(!!)으로 사용하면 어떤 값이든 널이 아닌 타입으로 강제로 바꿀 수 있다.
만약 str!!에 null이 있다면 NPE가 던져진다.

아래는 널 아님 단언을 사용해 널이 될 수 있는 인자를 널이 아닌 타입으로 변환하는 예시이다.

```kotlin
fun ignoreNulls(str: String?){
    val strNotNull : String = str!! // 예외는 이 지점을 가리킨다.
    println(strNotNull.length)
}

fun main(){
    ignoreNulls(null)
    //NPE 발생!
}
```
!!는 근본적으로 컴파일러에게 "나는 이 값이 null이 아님을 잘 알고 있다. 내가 잘못 생각했다면 예외가 발생해도 감수하겠다."라고 말하는 것이다.


## 7.8 let 함수
let함수를 사용하면 널이 될 수 있는 식을 더 쉽게 다룰 수 있다. let 함수를 안전한 호출 연산자와 함께 사용하면 원하는 식을 평가해서 null인지 검사한 다음에 그 결과를 변수에 넣는 작업을 간단한 식을 사용해 한꺼번에 처리할 수 있다.
```kotlin
email?.let{email -> sendEmailTo(email)}
```
let 함수는 이메일 주소 값이 널이 아닌 경우에만 호출된다. 따라서 다음 예제의 람다 안에서는 널이 아닌 타입으로 email를 사용할 수 있다.


## 7.11 타입 파라미터의 널 가능성
코틀린에서 함수나 클래스의 모든 타입 파라미터는 기본적으로 null이 될 수 있다.
```kotlin
fun<T> printHashCode(t: T){
    println(t?.hashCode()) //t가 널이 될 수 있으므로 안전한 호출을 써야만 한다.
}

fun main(){
    printHashCode(null) //null    
}
```

## 7.12 널 가능성과 자바
자바는 널 가능성을 지원하지 않는데, 이를 지원하는 코틀린과 함께 사용된다면 어떤 일이 생길까? 
자바 코드에도 어노테이션으로 표시된 널 가능성 정보가 있는데, 이런 정보가 코드에 있으면 코틀린도 그 정보를 활용한다. 
따라서 자바의 @Nullable String은 코틀린 쪽에서 볼 때 String?과 같고, 자바의 @NotNull String은 코틀린 쪽에서 볼 때 String과 같다.

하지만 자바에서 널 가능성 어노테이션이 없는 경우가 있는데, 그런 경우 자바의 타입은 코틀린의 플랫폼 타입이 된다.

### 7.12.1 플랫폼 타입
플랫폼 타입은 코틀린이 널 관련 정보를 알 수 없는 타입을 말한다. 그 타입을 널이 될 수 있는 타입으로 처리해도 되고 널이 될 수 없는 타입으로 처리해도 된다.
이는 자바와 마찬가지로 플랫폼 타입에 대해 수행하는 모든 연산에 대한 책임이 온전히 개발자에게 있다는 의미이다.

아래의 자바 코드를 참고하자.
```Java
@RequiredArgsConstructor
@Getter
public char Person{
    private final String name;
}
```
getName()은 null을 반환할까 아닐까? 코틀린 컴파일러는 이 경우 널 가능성에 대해 전혀 알지 못한다. 따라서 널 가능성을 개발자가 직접 처리해야만 한다.

```kotlin
fun yellAtSafe(person: Person){
    println((person,name ?: "Anyone").uppercase() + "!!!")  //null 검사하기
}
```
위와 같이 null 값을 제대로 처리하는 것이 실행 시점에 예외를 발생시키지 않는 것이다.
따라서 자바와 함께 코틀린을 사용한다면 해당 자바 메서드의 문서나 구현 코드를 자세하 살펴봐서 그 메서드가
null을 반환할지 알아내고 이에 대한 null 검사를 추가해야 한다.
