/**
 * Created by Aram_ on 13-10-2017.
 */
var express = require('express');
var app = express();

var jwt = require('jsonwebtoken');
var privatekey = 'bestesecretkey';

var user = require('../model/user');

/**
 * POST request om een nieuwe user te registreren
 */
app.post('/register', function (req, res) {
    //haal alle benodigde parameters uit de body van de request
    var firstname = req.body['firstname'];
    var lastname = req.body['lastname'];
    var insertion = req.body['insertion'];
    var username = req.body['username'];
    var password = req.body['password'];

    //maak een nieuwe user aan en sla deze op
    newUser = new user({
        firstName: firstname,
        lastName: lastname,
        insertion: insertion,
        username: username,
        password: password
    }).save(function (err) {
        if (err) {
            console.log(err);
            res.status(400).json({"message": "User is not registered, something went wrong!"});
        } else {
            res.status(200).json({"message": "User registered"});
        }
    });
});

/**
 * POST request om in te loggen in eht systeem
 * Als de username en wachtwoord kloppen geeft het systeem een authorization token terug
 */
app.post('/login', function (req, res) {
    var username = req.body['username'];

    //zoek de user door middel van de username uit debody
    user.find({username: username}, function (err, user) {
        if (!user) {
            res.status(404).json({"message": "Something went wrong. User not found or isnt in the database"});
        } else {
            var password = req.body['password'];

            var jsonArray = JSON.parse(JSON.stringify(user));
            //check of het wachtwoord jusit is
            if (password === jsonArray[0].password) {
                if (!res) {
                    res.status(403).json({"message": "Forbidden!"});
                } else {
                    var profile = {
                        username: username,
                        _id: jsonArray[0]._id
                    };
                    //maak een authorization token aan en geef deze terug
                    var authToken = jwt.sign(profile, privatekey, {
                        expiresIn: 3200
                    });
                    res.status(200).send({
                        authToken: authToken
                    });
                }
            } else {
                res.status(400).json({"message": "Something went wrong with the login (password isnt right)"});
            }
        }
    });
});

/**
 * GET request om alle users terug te krijgen
 */
app.get('/', function (req, res) {
    var authToken = req.headers['authorization'];

    //check of er een token is
    if (!authToken) {
        return res.status(403).send("Token is not valid or is missing...");
    } else {
        //check of de token klopt
        jwt.verify(authToken, privatekey, function (err) {
            if (err) {
                console.log(err);
                res.status(403).json({"Message": "Token is not valid or is missing..."});
            } else {
                //zoek alle users en geef deze terug in json
                user.find({}, function (err, users) {
                    if (err) {
                        console.log(err);
                        res.status(404).json({"Message": "Something went wrong with finding the users.."});
                    } else {
                        res.status(200).json(users);
                    }
                });
            }
        });
    }
});

/**
 * GET request om 1 specifieke user op te halen
 * je geeft hier de username mee in de url
 */
app.get('/:user', function (req, res) {
    var authToken = req.headers['authorization'];

    //check of er een token is
    if (!authToken) {
        return res.status(403).json({"Message": "Token is not valid or is missing..."});
    } else {
        //check of de token klopt
        jwt.verify(authToken, privatekey, function (err) {
            if (err) {
                console.log(err);
                res.status(400).json({"Message": "Something went wrong"});
            } else {
                //zoek de user en geef deze terug in json
                user.findOne({username: req.params.user}, function (err, user) {
                    if (err) {
                        console.log(err);
                        res.status(400).json({"Message": "Something went wrong in finding the user"});
                    } else {
                        res.status(200).json(user);
                    }
                });
            }
        });
    }
});

module.exports = app;