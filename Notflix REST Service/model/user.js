/**
 * Created by Aram_ on 20-10-2017.
 */
var mongoose = require('mongoose');

var Schema = mongoose.Schema;

//Schema voor een nieuwe user waarbij een insertion (tussenvoegsel) niet nodig is
var userSchema = new Schema({
    firstName: {type: String, required: true},
    insertion: {type: String, required: false},
    lastName: {type: String, required: true},

    username: {type: String, required: true, unique: true},
    password: {type: String, required: true}
});


module.exports = mongoose.model('users', userSchema);