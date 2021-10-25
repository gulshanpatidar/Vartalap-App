const User  = require('../models/user');
const Chat = require('../models/chat');

// function for seeing the list with users in the database
export const getUsers = (req,res,next) => {
    
    try {
       const page = req.body.page || 0;

       const users = await User.find().skip(page * 20).limit(20);
       res.status(200).json({
           users : users,    
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
export const startChat = async (req,res,next) => {
      
    try {
      const chat = new Chat({user1: req.userId,user2 : req.body.user});
      await chat.save();

      const user1 = await User.find(req.userId);
      const user1ChatsId = user1.chatsId;
      user1ChatsId.push(chat._id);
      await user1.save();

      const user2 = await User.find(req.body.user);
      const user2ChatsId = user2.chatsId;
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
export const addMessage = async (req,res,next) => {
    const chatId = req.body.chatId;
    const { text,createdAt} = req.body;
    try {
      const chat = await Chat.findOne(chatId);
      const messages = chat.messages;
      messages.push({ text: text, senderId:req.userId, createdAt: createdAt})
      await chat.save();
      res.status(200).json({
         "Message" : "message added successfully"
      })
    }
    catch (e){
        const error = new Error();
        next(err);
    }
}