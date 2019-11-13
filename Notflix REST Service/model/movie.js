/**
 * Created by Aram_ on 20-10-2017.
 */
var mongoose = require('mongoose');

var Schema = mongoose.Schema;

//Schema voor een nieuwe movie
var movieSchema = new Schema({
    movieID: {type: Number, required: true},
    titel: {type: String, required: true},
    dateOfRelease: {type: String, required: true},
    duration: {type: Number, required: true},
    director: {type: String, required: true},
    description: {type: String, required: true}
});

module.exports = mongoose.model('movies', movieSchema);