const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const userSchema = new Schema({
    username:{
     type:String,
     require:true
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