import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties

fun Any.isMatch(vararg properties: Any): Boolean {
    val classComponents = this::class.declaredMemberProperties

    return this::class.constructors
            .first()
            .parameters
            .take(properties.size)
            .filterIndexed { i, param -> componentIsNotMatch(classComponents, param, properties, i) }
            .isEmpty()
}

@SuppressWarnings
private fun Any.componentIsNotMatch(classComponents: Collection<KProperty1<out Any, Any?>>, param: KParameter, properties: Array<out Any>, i: Int): Boolean {
    val component = classComponents.first { it.name == param.name }
    return properties[i] != "_" && properties[i] != (component as KProperty1<Any, *>).get(this)
}

data class User(val firstName: String, val lastName: String, val username: String, var age: Int)

data class Moderator(val level: Int)

fun main(args: Array<String>) {
    val user = User("Viktor", "Blinov", "victorpomidor", 24)
    println(patternMatchingExample(user))

}

fun patternMatchingExample(user: Any): String {
    return when {
        user is User && user.isMatch("Viktor", "_", "_", 24) -> "it's Viktor 24 y.o."
        user is User && user.isMatch("Pavel") -> "it's Pavel"
        user is Moderator && user.isMatch(2) -> "it's moderator of 2 level"
        else -> "unknown"
    }
}
