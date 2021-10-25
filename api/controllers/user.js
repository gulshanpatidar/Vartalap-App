const User  = require('../models/user');
const Chat = require('../models/chat');
const getChatedUsers = require('../utils/getChatedUsers');


// function for getting users chat 
exports.getUserChats = async (req,res,next) => {
    const userChats = [];
    try {
        const user = await User.findById(req.userId);
        const userchatsId = user.chatsid;
        
        for (let i = 0;i < userchatsId.length;i++) {
           const chatteduser = await User.findById(userchatsId[i]);  
           userChats.push({
               username : chatteduser.username,
               id : chatteduser._id,
               chatid : userchatsId[i]
           })
        }

        res.status(200).json({
            users : userChats
        })
    } 
    catch (err) {
        if(! err.statusCode) {
            err.statusCode = 500
        }
         next(err);
    }
}


exports.getChatMessages = async (req,res,next) => {
  
    try {
        const chatId = req.params.chatId;
        const chat = await Chat.findById(chatId);
        res.status(200).json({
            messages : chat.messages
        })

    }catch (err) {
        if(! err.statusCode) {
            err.statusCode = 500
        }
         next(err);
       }
}


// function for seeing the list with users in the database
exports.getAppUsers = async (req,res,next) => {
    
    try {
       // calling a function for getting the array of users that have already chated with current user
       const chatedUsers = await getChatedUsers(req.userId);
       const page = req.body.page || 0;

       const users = await User.find({ _id : { $nin : [...chatedUsers,req.userId]}}).skip(page * 20).limit(20);
       const resUsers = users.map( user => ({
           username : user.username,
           id : user._id 
       }))
       res.status(200).json({
           users : resUsers  
       })

    }
    catch (err) {
        if(! err.statusCode) {
            err.statusCode = 500
        }
         next(err);
       }
    
}

// function for starting chat with another user
exports.startChat = async (req,res,next) => {
      
    try {
      const chat = new Chat({user1: req.userId,user2 : req.body.user});
      await chat.save();

      const user1 = await User.findById(req.userId);
      const user1ChatsId = user1.chatsid;
      user1ChatsId.push(chat._id);
      await user1.save();

      const user2 = await User.findById(req.body.user);
      const user2ChatsId = user2.chatsid;
      user2ChatsId.push(chat._id);
      await user2.save();

      res.status(200).json({
          "message" : "Started chat successfully."
      })
  
    }
    catch (err) {
        if(! err.statusCode) {
            err.statusCode = 500
        }
         next(err);
       } 
}

// function for adding message to the database
exports.addMessage = async (req,res,next) => {
    const chatId = req.body.chatId;
    const { text,createdAt} = req.body;
    try {
      const chat = await Chat.findById(chatId);
      const messages = chat.messages;
      messages.push({ text: text, senderId:req.userId, createdAt: createdAt})
      await chat.save();
      res.status(200).json({
         "Message" : "message added successfully"
      })
    }
    catch (e){
        if(! err.statusCode) {
            err.statusCode = 500
        }
         next(err);
       } 
}