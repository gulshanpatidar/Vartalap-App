const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const userSchema = new Schema({
    username:{
     type:String,
     require:true
    },
    fullname : {
      type : String,
      require : true
    },
    imageurl : {
       type : String,
       default : ""
    },
    imagename : {
       type : String,
       default : ""
    },
    pubkey: { 
      type : String,
      default : ""
    },
    password:{
        type:String,
        require:true
    },
    email:{
        type:String,
        require:true
    },
    chatsid : [
        {
            type:Schema.Types.ObjectId,
            ref : 'Chat'
        }
    ]
})

module.exports = mongoose.model('User', userSchema);