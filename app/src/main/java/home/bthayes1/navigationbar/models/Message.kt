package home.bthayes1.navigationbar.models

data class Message(
    private val senderUID : String,
    private val messageText : String,
    private val timeSent : Long
) {
}