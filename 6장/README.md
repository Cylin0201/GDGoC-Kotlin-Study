# 6장 컬렉션과 시퀀스
## 6.1 컬렉션에 대한 함수형 API 
### 원소 제거와 변환: filter와 map
어떤 술어를 바탕으로 컬렉션의 원소를 걸러내거나, 컬렉션의 각 원소를 다른 형태로 변환하고 싶다면 filter와 map을 적절히 사용하자.

```kotlin
data class Person(val name: String, val age: Int)

fun main(){
    val list = listOf(1, 2, 3, 4)
    println(list.filter{ it % 2 == 0})
}
```
filter를 통한 결과는 입력된 컬렉션에서 주어진 술어(짝수인지)를 만족하는 원소로만 이뤄진 새로운 컬렉션이다.

map은 입력 컬렉션의 원소를 변환할 수 있게 해준다.
map은 주어진 함수를 컬렉션의 각 원소에 적용하고 그 결과값을 새 컬렉션에 모아준다. 

```kotlin
fun main(){
    val list = listOf(1, 2, 3, 4)
    println(list.map{it * it})
}
```
결과는 각 원소가 주어진 함수에 따라 변환되는 같은 개수의 원소가 들어있는 새로운 컬렉션이다. 

### 컬렉션 값 누적: reduce와 fold
reduce와 fold는 컬렉션의 정보를 종합하는 데 사용한다. 즉, 원소로 이뤄진 컬렉션을 받아서 한 값을 반환한다.
이 값은 누적기를 통해 점진적으로 만들어진다. 개발자의 람다는 각 원소에 대해 호출되며 새로운 누적 값을 반환한다.

```kotlin 
//reduce 예제
fun main(){
    val list = listOf(1, 2, 3, 4)
    println(list.reduce{ acc, element -> acc + element })
    
    println(list.reduce{ acc, element -> acc * element })
}
```
fold 함수는 reduce와 비슷하지만 컬렉션 첫 원소를 누적 값으로 시작하는 것이 아니라, 임의의 시작 값을 선택할 수 있다.

```kotlin
fun main(){
    val people = listOf(
        Person("Alex", 29), Person("Natalia", 28)
    )
    val folded = people.fold("") {acc, person -> 
        acc + person.name
    }
    println(folded) //AlexNatalia
}
```

### 컬렉션에 술어 적용: all, any, none, count, find
all, any, none 연산은 컬렉션의 모든 원소가 어떤 조건을 만족하는지 판단하는 연산이다.
count 함수는 조건을 만족하는 원소의 개수를 반환하며, find 함수는 조건을 만족하는 첫 번째 원소를 반환한다.

```kotlin
val canBeInClub27 = {p: Person -> p.age <= 27}

fun main(){
    val people = listOf(Person("Alex", 27), Person("Natalia", 31))
    //1. all
    println(people.all(canBeInClub27)) //false
    
    //2. any
    println(people.any(canBeInClub27)) //true
    
    //3. count
    println(people.cout(canBeInClub27)) //1
    
    //4. find: 술어를 만족하는 첫 번째 원소 반환
    println(people.find(canBeInClub27)) //Person(name=Alice, age=27)
}
```
### 리스트를 분할해 리스트의 쌍으로 만들기: partition
partition 함수를 통해 어떤 술어를 만족하는 그룹과 그렇지 않은 그룹으로 나눌 수 있다. 

### 리스트를 여러 그룹으로 이뤄진 맵으로 바꾸기: groupBy
partition 함수가 반환하는 '참' '거짓' 그룹으로만 분리할 수 없는 경우가 있다. 어떤 특성에 따라 여러 그룹으로 나누고 싶을 수 있다.
이 때 사용하는 함수가 groupBy이다.

```kotlin
fun main(){
    val people = listOf()...
    println(people.groupBy{it.age}) // 각 나이를 키로 갖는 맵이 만들어진다.
}
```