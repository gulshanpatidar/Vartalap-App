const jwt = require('jsonwebtoken');

module.exports = (req,res,next) => {
    const authHeader = req.get('Authorization');
    if( !authHeader){
        const error = new Error('Not Authenticated');
        error.statusCode = 401;
        throw error;
    } 
    const token = authHeader.split(' ')[1];
    let decodedToken;
    try {
        decodedToken = jwt.verify(token,'Suruchatd5be0d441d557f372d9bb81a193be014a6e6c90065bcdcSecret');
    }catch (err){
     err.statusCode = 500;
     throw err;
    }
    if (!decodedToken) {
        const error = new Error('Not Authenticated');
        error.statusCode = 401;
        throw error
    }
    req.userId =  decodedToken.userId;
    next();
}