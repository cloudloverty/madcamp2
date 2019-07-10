//dependencies
var restful = require('node-restful');
var mongoose = restful.mongoose;


var statusSchema = new mongoose.Schema({
    name: String,
    phonenumber: String,
    email: String
}); //데이터 저장되어있는곳이 모델
// return models:

module.exports=restful.model('Contact',statusSchema);
