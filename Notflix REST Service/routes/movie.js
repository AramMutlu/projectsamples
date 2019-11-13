/**
 * Created by Aram_ on 20-10-2017.
 */
var express = require('express');
var app = express();

var movie = require('../model/movie');

/**
 * GET request om alle films in het systeem terug te krijgen.
 */
app.get('/', function (req, res) {
    // zoek alle films in het systeem
    movie.find({}, function (err, results) {
        if (err) {
            console.log(err);
            res.status(400).json({"Message": "Something went wrong with searching for the movies"});
        } else {
            //geef alle movies terug
            res.status(200).json(results);
        }
    });
});

/**
 * GET request om 1 specifieke film op te halen door middel van de movieID in de url van de request.
 * movieID wordt meegegeven in de url
 */
app.get('/:movie', function (req, res) {
    // Vind één film, met de meegegeven parameter (movieID)
    movie.findOne({movieID: req.params.movie}, function (err, movie) {
        if (err) {
            console.log(err);
            res.status(400).json({"Message": "Something went wrong with searching for the movie"});
        } else {
            res.status(200).json(movie);
        }
    });
});

module.exports = app;