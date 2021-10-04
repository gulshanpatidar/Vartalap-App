const express = require("express");
const app = express();
const PORT = process.env.PORt || 5000;


app.get('/',(req,res,next) => {
    res.json({ 
        "title" : "Hello fro suru chat api"
    })
})
app.listen(PORT, () => {
    console.log("server started");
})