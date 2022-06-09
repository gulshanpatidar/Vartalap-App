const User  = require('../models/user');
const Chat = require('../models/chat');
const getAnotherUser = require('../utils/anotheruser');


async function getChatedUsers(userId) {
    const chatedUsers = [];
    const user = await User.findById(userId); 
    const chatsid = user.chatsid;
    for (let i =0 ;i <chatsid.length;i++) {
        chatedUsers.push(await getAnotherUser(chatsid[i],userId));
    }
    chatedUsers.push(userId);
    return chatedUsers;
    
}

module.exports = getChatedUsers;