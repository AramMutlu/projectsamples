/**
 * Created by Aram_ on 20-10-2017.
 */
var mongoose = require('mongoose');

var Schema = mongoose.Schema;

//Schema voor een nieuwe rating
var ratingSchema = new Schema({
    movieID: {type: Number, required: true},
    username: {type: String, required: true},
    ratingNumber: {type: Number, required: true}
});

module.exports = mongoose.model('ratings', ratingSchema);