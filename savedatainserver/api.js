var express = require('express');
var router = express.Router();

// get models:
var Status = require('C://Users//Administrator//node-v12.5.0-win-x64//node.js//savedatainserver//models//status');  //call data in ctstus in model

//routes:
Status.methods(['get','post','put','delete']);
Status.register(router,'/status');

// return router:
module.exports = router;