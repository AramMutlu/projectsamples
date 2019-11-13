/**
 * Created by Aram_ on 20-10-2017.
 */
/**
 * Created by Aram_ on 20-10-2017.
 */
var express = require('express');
var app = express();

var jwt = require('jsonwebtoken');

var mongoose = require('mongoose');

var bodyParser = require('body-parser');
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));

mongoose.connect('mongodb://localhost/notflix');

var database = mongoose.connection;

var user = require('./routes/user');
var movie = require('./routes/movie');
var rating = require('./routes/rating');

app.use(express.static('public'));
app.use('/api/user', user);
app.use('/api/movie', movie);
app.use('/api/rating', rating);

app.use(function (req, res, next) {
    res.header('Access-Control-Allow-Origin', '*');

    res.header('Access-Control-Allow-Methods', 'GET, POST, PUT, DELETE');

    res.header('Access-Control-Allow-Headers', 'content-type');

    next();
});

//listen on port 3000
app.listen(3000, function () {
    console.log('Listening on port 3000!');
});

app.get('/filldatabase', function (req, res) {

    console.log('Filling database!');

    var movie = require('./model/movie');

    newMovie = new movie({
        movieID: 111161,
        title: 'The Shawshank Redemption ',
        dateOfRelease: '2-03-1995',
        duration: 131,
        director: 'Frank Darabont',
        description: 'Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.'
    }).save();

    new movie({
        movieID: 68646,
        title: 'The Godfather',
        dateOfRelease: '18-01-1973',
        duration: 75,
        director: 'Francis Ford Coppola',
        description: 'The aging patriarch of an organized crime dynasty transfers control of his clandestine empire to his reluctant son.'
    }).save();


    res.status(200).json({"Message": "Database is filled!"});
});