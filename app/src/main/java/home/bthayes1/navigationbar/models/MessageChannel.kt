package home.bthayes1.navigationbar.models

data class MessageChannel(
    private val users : MutableList<User>,
    private val channelID : String,
    private val messages : MutableList<Message>){
    /**
     * A chat channel is a chat activity that includes the following data:
     *     1. UID of included users
     *     2. Channel ID
     *     3. ListOf<Messages>
     *
     *     Once a chat is opened, a new chat channel document will be opened in each Users collection
     *          db.add("new message channel document") to  uid.collection and add another document
     *          to other UID collection
     *          db.add(message) to collection("message channel id"
     *          message channel document:
     *              -List of messages
     *
     *     Add a message to existing channel:
     *          find conversation id
     *          add message to list
     *
     *     Receive chat channel:
     *      chat channel recycler view will listen for new channel ids added to user channel collection
     *     Receive message:
     *      message recycler view will listen for new messages being added to channel message list
     *      if sender UID == current userID, then message will be on right side, else it is on left
     *
     *     Message includes:
     *     -Sender UID
     *     -message text
     *     -time sent
     *
     *
     *
     */

}
