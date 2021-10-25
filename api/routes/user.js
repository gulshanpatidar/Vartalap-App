const express = require('express');
const { getAppUsers,startChat, addMessage,getUserChats,getChatMessages }  = require('../controllers/user');
const isAuth = require('../middleware/isAuth');

const router = express.Router();

router.get('/userchats',isAuth,getUserChats);
router.get('/chatmessages',isAuth,getChatMessages)
router.get('/appusers',isAuth,getAppUsers);
router.post('/startchat',isAuth,startChat);
router.post('/message',isAuth,addMessage)

module.exports = router;