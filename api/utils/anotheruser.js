const Chat = require('../models/chat');

const getAnotherUser = async (chatid,userId) => {

    const chat = await Chat.findById(chatid);
    if ( chat.user1.toString() === userId) {
        return chat.user2.toString();
    } else {
        return chat.user1.toString();
    }
}
module.exports = getAnotherUser;