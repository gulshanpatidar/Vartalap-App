const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const chatSchema = new Schema({
    user1:{
       type : Schema.Types.ObjectId,
       ref: 'User'
    },
    user2:{
        type: Schema.Types.ObjectId,
        ref : 'User'
    },
    messages : [
      {
          text : {
              type : String,
              require : true
          },
          image : {
              type : String,
              default : ""
          },
          senderId : {
              type : Schema.Types.ObjectId,
              ref : 'User'
          },
          createdAt : {
              type : Number,
              require:true
          }
      }
    ]
})

module.exports = mongoose.model('Chat', chatSchema);