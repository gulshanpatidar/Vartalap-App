const User = require('../models/user');
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');
const { validationResult } = require('express-validator');


// function for user signup
exports.userSignup = async (req,res,next) => {

    const errors = validationResult(req);
    if(!errors.isEmpty()) {
        const error = new Error('Validation failed,entered data is incorrect');
        error.statusCode = 422;
        return next(error);
    }
    const username = req.body.username;
    const email = req.body.email;
    const password = req.body.password;
    try {
        const hashedPw = await bcrypt.hash(password, 12);
        const user = new  User({
            username:username,
            email:email,
            password:hashedPw
        })
       await user.save();
       res.status(200).json({
           message:"User Signup Successfully"
       })
    }catch (err) {
        if (!err.statusCode) {
          err.statusCode = 500;
        }
        next(err);
      }
}

// function for user login
exports.userLogin = async (req,res,next) => {
 const username = req.body.username;
 const password = req.body.password;
 let loadedUser;
 try {
 const user = await User.findOne({username:username});
 if (!user) {
     const error = new Error('No user found.');
     error.statusCode = 422;
     return next(error);
 }
 loadedUser = user;
 const isEqual = await bcrypt.compare(password, user.password);
 if (!isEqual) {
   const error = new Error('Wrong password!');
   error.statusCode = 401;
   return next(error);
 }
 const token = jwt.sign(
   {
     email: loadedUser.email,
     userId: loadedUser._id.toString()
   },
   'Suruchatd5be0d441d557f372d9bb81a193be014a6e6c90065bcdcSecret',
   { expiresIn: '23h' }
 );

 res.status(200).json({ token: token, userId: loadedUser._id.toString()});
 } catch (err) {
    if(! err.statusCode) {
        err.statusCode = 500
    }
     next(err);
   }
}