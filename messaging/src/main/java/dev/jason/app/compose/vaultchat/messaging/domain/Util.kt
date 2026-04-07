package dev.jason.app.compose.vaultchat.messaging.domain

object Util {

    val otherUserUid = Property<String>()
    val usersOnline = Property<List<String>>()

    class Property<T>(private var value: T? = null) {
        fun get() = value
        fun set(value: T) {
            this.value = value
        }

        fun reset() {
            this.value = null
        }
    }
}