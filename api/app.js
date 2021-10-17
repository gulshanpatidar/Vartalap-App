require('dotenv').config();
const express = require("express");
const app = express();
const moongoose = require('mongoose');
const PORT = process.env.PORT || 5000;
const MONGODB_URI = process.env.MONGODB_URI
const authRoutes = require('./routes/auth');


// setting the header for accessing the api form anyhwhere
app.use((req, res, next) => {
    res.setHeader('Access-Control-Allow-Origin', '*');
    res.setHeader(
      'Access-Control-Allow-Methods',
      'OPTIONS, GET, POST, PUT, PATCH, DELETE'
    );
    res.setHeader('Access-Control-Allow-Headers', 'Content-Type, Authorization');
    next();
  });


app.use(express.json());
app.use('/auth',authRoutes);
app.get('/',(req,res,next) => {
    res.json({ 
        "title" : "Hello fro suru chat api"
    })
})

app.use((error,req,res,next) => {
    console.log(error);
    const status = error.statusCode || 500;
    const message = error.message || 'Server Error';
    const data = error.data;
    res.status(status).json({ message : message,data : data});  
  })


moongoose.connect(MONGODB_URI)
  .then( result => {
    app.listen(PORT,() => {
      console.log("server started");
  })
  })
  .catch(err => {
      console.log(err)
  })