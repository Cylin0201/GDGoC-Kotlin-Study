class User(val id: Int, val name: String, val address: String)

fun saveUser(user: User){
    if (user.name.isEmpty()){
        throw IllegalArgumentException(
            "Can't save user ${user.id}: empty Name"
        )
    }
    if (user.address.isEmpty()){
        throw IllegalArgumentException(
            "Can't save user ${user.address}: empty address"
        )
    }
}

fun main(){
    saveUser(User(1, "", ""))
    //예외 발생!!
}