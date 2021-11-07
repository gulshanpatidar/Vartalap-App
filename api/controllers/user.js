const User  = require('../models/user');
const Chat = require('../models/chat');
const getChatedUsers = require('../utils/getChatedUsers');
const getAnotherUser = require('../utils/anotheruser');
const cloudinary = require('cloudinary').v2;

// function for getting users chat 
exports.getUserChats = async (req,res,next) => {
    const userChats = [];
    try {
        const user = await User.findById(req.userId);
        const userchatsId = user.chatsid;
        
        for (let i = 0;i < userchatsId.length;i++) {
           const anotheruserId = await getAnotherUser(userchatsId[i],req.userId)
           const chatteduser = await User.findById(anotheruserId);  
           userChats.push({
               fullname : chatteduser.fullname,
               id : chatteduser._id,
               username: chatteduser.username,
               chatid : userchatsId[i],
               imageurl : chatteduser.imageurl,
               pubkey: chatteduser.pubkey
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


// function for getting messages related to a particular chat id
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

       const users = await User.find({ _id : { $nin : chatedUsers}})
    
       const resUsers = users.map( user => ({
           fullname : user.fullname,
           username: user.username,
           id : user._id ,
           imageurl : user.imageurl
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
    const { text,image,createdAt,chatId} = req.body;
    try {
      const chat = await Chat.findById(chatId);
      const messages = chat.messages;
      
      messages.push({ text:text,image:image,senderId:req.userId, createdAt: createdAt})
      await chat.save();
      res.status(200).json({
         "Message" : "message added successfully."
      })
    }
    catch (e){
        if(! err.statusCode) {
            err.statusCode = 500
        }
         next(err);
       } 
}


// function for handling the image upload feature
exports.userImageupload = async (req,res,next) => {
  try {
     const imageurl = req.file.path;
     const imagename = req.file.filename;
     const user = await User.findById(req.userId);
     if (user.imageurl) {
        await cloudinary.uploader.destroy(user.imagename);
     }
     user.imageurl = imageurl;
     user.imagename = imagename;
     await user.save();
     res.status(200).json({
             imageurl : imageurl
         }
     )
  }  catch (e){
    if(! err.statusCode) {
        err.statusCode = 500
    }
     next(err);
   } 
}

// function for sending image in message
exports.sendImage = async (req,res,next) => {
    try {
       const image = req.file.path;
       res.status(200).json({
               image : image
           }
       )
    }  catch (e){
      if(! err.statusCode) {
          err.statusCode = 500
      }
       next(err);
     } 
  }

// function for updating the fullname
exports.updateFullName = async (req,res,next) => {
    try {
       const updatedFullName = req.body.fullname;
       const user = await User.findById(req.userId);
       user.fullname = updatedFullName;
       await user.save();
       res.status(200).json({
               "Message" : "FullName Updated Successfully"
           }
       )
    }  catch (e){
      if(! err.statusCode) {
          err.statusCode = 500
      }
       next(err);
     } 
  }