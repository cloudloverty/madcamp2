//dependencies
var express = require('express');
var mongoose = require('mongoose');
var bodyParser = require('body-parser');
var mongodb = require('mongodb');
//connect to mongoDB:
 mongoose.connect('mongodb://127.0.0.1:27017/Contact',{useNewUrlParser: true});


//express
var app = express();
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));
var MongoClient = mongodb.MongoClient;
// routes
app.use('/api', require('C://Users//Administrator//node-v12.5.0-win-x64//node.js//savedatainserver//api'));
// app.post('/addcontacts', (request, response, next)=>{ // add contact
//     var post_data = request.body;
//
//     //var plaint_password = post_data.password;cd
//     //var hash_data = saltHashPassword(plaint_password);
//
//     //var password = hash_data.passwordHash;  //pa
//     ssword hash
//     //var salt = hash_data.salt;  //save salt

//     var id = post_data.id;
//     var name = post_data.name;
//     var phonenumber = post_data.phonenumber;
//     var email = post_data.email;
//
//
//     var contactinfo = {
//         'name':name,
//         'phonenumber':phonenumber,
//         'email': email
//
//         //'salt': salt,
//     };
//     var db = client.db('Contact');
//     var db2 = client.db('user-practice');
//     //check exists email
//     db2.collection('register') // register
//         .find({'email': email}).count(function(err, number){
//         if(number==0){
//             response.json('Email not exists cannot insert contact');
//             console.log('Email not exists cannot insert contact');
//         }else{
//             db.contacts
//                 .insertOne(contactInfo, function(error, res){
//                     response.json('put contact detail success');
//                     console.log('put contact detail success');
//                 })
//         }
//     })
//     //insert data
//
//

// })
// console.log("b");
//
    MongoClient.connect('mongodb://127.0.0.1:27017', {useNewUrlParser: true},function(err,client){
        if(err) console.log("cannot connect mongodb");
        else{
            app.get('/getdata', (request, response,next) => {
                console.log("can connect mongodb");
                if (err)
                    throw err;
                var query = {};
                var db = client.db('Contact');


                db.collection('contacts').find({}).toArray(function (err, result) {
                    if (err)
                        console.log(err);
                    else{

                        console.log("success", response);
                        response.json(result);// end send
                        // client.close();
                    }

                })
            });

        }
});
//
//



//start server:
app.listen(3000,function(){
    console.log('server is run on port 3000');
});
