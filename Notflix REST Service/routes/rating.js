/**
 * Created by Aram_ on 21-10-2017.
 */
var express = require('express');
var app = express();

var jwt = require('jsonwebtoken');
var privatekey = 'bestesecretkey';

var rating = require('../model/rating');

/**
 * POST request om een rating toe te voegen.
 * De parameters worden meegegeven in de url van de request.
 */
app.post('/:movieID/:username', function (req, res) {
    var authToken = req.headers['authorization'];

    //check of er een token is meegegeven
    if (!authToken) {
        return res.status(403).json({"Message": "Token not valid or is missing..."});
    } else {
        jwt.verify(authToken, privatekey, function (err) {
            if (err) {
                console.log(err);
                res.status(400).json({"Message": "Something went wrong in the request"});
            } else {
                //haal de rating uit de body van de request
                var ratingNumber = req.body['ratingNumber'];
                if (ratingNumber > 5) {
                    ratingNumber = 5;
                }
                //maak een nieuwe rating aan
                newRating = new rating({
                    username: req.params.username,
                    ratingNumber: ratingNumber,
                    movieID: req.params.movieID
                });

                //sla de rating op
                newRating.save(function (err) {
                    if (err) {
                        console.log(err);
                        res.status(400).json({"Message": "Something went wrong with saving the Rating..."});
                    } else {
                        res.status(200).json({"Message": "Rating saved successfuly"});
                    }
                });
            }
        });
    }
});

/**
 * GET request om een gemmidelde rating op te halen van een movie.
 * Hierbij moet movieID mee worden gegeven in de url
 */
app.get('/:movieID', function (req, res) {
    var authToken = req.headers['authorization'];

    //gebruik movieID om de rating van de movie te vinden
    rating.find({movieID: req.params.movieID}, function (err, results) {
        if (err) {
            console.log(err);
            res.status(400).json({"Message": "Something went wrong in finding the ratings"});
        } else {
            //reken het gemiddelde uit en stuur deze op
            var avg = 0;
            var counter = 0;
            for (i = 0; i < results.length; i++) {
                if (results[i].ratingNumber !== null) {
                    avg = avg + results[i].ratingNumber;
                    counter++;
                }

            }
            avg = (avg / counter);
            //geef de rating terug in json
            res.status(200).json(avg);
        }
    });

});

/**
 * GET request om alle ratings van een user te krijgen
 */
app.get('/:username', function (req, res) {
    var authToken = req.headers['authorization'];

    //check of er een token is
    if (!authToken) {
        return res.status(403).json({"Message": "Token not valid or is missing..."});
    } else {
        jwt.verify(authToken, privatekey, function (err) {
            if (err) {
                console.log(err);
                res.status(400).json({"Message": "Something went wrong in the request"});
            } else {
                var username = req.params.username;
                //gebruik de username of alle ratings van de user te zoeken
                rating.find({username: username}, function (err, results) {
                    if (err) {
                        console.log(err);
                        res.send(400).json({"Message": "Something went wrong in finding the ratings of this user"});
                    } else {
                        //geef alle ratings terug van de user in json
                        res.status(200).json(results);
                    }
                });
            }
        });
    }
});
/**
 * GET request om een rating van een user van een bepaalde movie op te halen.
 */
app.get('/:movieID/:username', function (req, res) {
    var authToken = req.headers['authorization'];

    //check of er een token is
    if (!authToken) {
        return res.status(403).json({"Message": "Token not valid or is missing..."});
    } else {
        jwt.verify(authToken, privatekey, function (err) {
            if (err) {
                console.log(err);
                res.status(400).json({"Message": "Something went wrong in the request"});
            } else {
                var username = req.params.username;
                var movieID = req.params.movieID;
                //haal de benodigde parameters uit de url van de request
                rating.find({movieID: movieID, username: username}, function (err, result) {
                    if (err) {
                        console.log(err);
                        res.status(400).json({"Message": "Something went wrong while deleting the rating"});
                    } else {
                        res.status(200).json(result);
                    }
                });
            }
        });
    }
});

/**
 * DELETE request om een rating te verwijderen uit het systeem
 */
app.delete('/:movieID/:username', function (req, res) {
    var authToken = req.headers['authorization'];

    //check of er een token is
    if (!authToken) {
        return res.status(403).json({"Message": "Token not valid or is missing..."});
    } else {
        jwt.verify(authToken, privatekey, function (err) {
            if (err) {
                console.log(err);
                res.status(400).json({"Message": "Something went wrong in the request"});
            } else {
                //haal de benodigde parameters uit de url van de request
                //zoek de rating op en verwijder deze
                rating.remove({movieID: req.params.movieID, username: req.params.username}, function (err) {
                    if (err) {
                        console.log(err);
                        res.status(400).json({"Message": "Something went wrong while deleting the rating"});
                    } else {
                        res.status(200).json({"message": "Rating deleted"});
                    }
                });
            }
        });
    }
});

/**
 * PUT request om een rating de wijzigen
 */
app.put('/:movieID/:username', function (req, res) {
    var authToken = req.headers['authorization'];

    //check of er een token is
    if (!authToken) {
        return res.status(403).json({"Messsage": "Token not valid or is missing..."});
    } else {
        jwt.verify(authToken, privatekey, function (err) {
            if (err) {
                console.log(err);
                res.status(400).json({"Message": "Something went wrong in the request"});
            } else {
                var ratingNumber = req.body['ratingNumber'];
                if (ratingNumber > 5) {
                    ratingNumber = 5;
                }
                //haal de benodigde paramaters uit de url van de request
                //zoek de oude rating op en update deze
                rating.findOneAndUpdate({username: req.params.username, movieID: req.params.movieID}, {
                    $set: {
                        ratingNumber: ratingNumber
                    }
                }, function (err, result) {
                    if (err) {
                        return res.status(400).json({"Message": "Something went wrong with updateing the rating"});
                    } else {
                        return res.status(200).json({"Message": "Rating update succesfull"});
                    }
                });
            }
        });
    }
});


module.exports = app;