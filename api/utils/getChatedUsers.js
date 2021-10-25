const User  = require('../models/user');
const Chat = require('../models/chat');

async function getChatedUsers(userId) {
    const chatedUsers = [];
    const user = await User.findById(userId); 
    const chatsid = user.chatsid;
    for (let i =0 ;i < chatsid.length;i++) {
        const chat = await Chat.findById(chatsid[i]);
        if ( chat.user1.toString() === userId) {
            chatedUsers.push(chat.user1);
        } else {
            chatedUsers.push(chat.user2);
        }
    }


    return chatedUsers;
    
}

module.exports = getChatedUsers;