const express = require('express');
const { getAppUsers,startChat, addMessage,getUserChats,getChatMessages,userImageupload }  = require('../controllers/user');
const isAuth = require('../middleware/isAuth');
const multer = require('multer');
const { storage } = require('../cloudinary');
const upload = multer({storage});

const router = express.Router();

router.get('/userchats',isAuth,getUserChats);
router.get('/chatmessages/:chatId',isAuth,getChatMessages)
router.get('/appusers',isAuth,getAppUsers);
router.post('/startchat',isAuth,startChat);
router.post('/message',isAuth,addMessage)
router.post('/image',upload.single('image'),isAuth,userImageupload);

module.exports = router;