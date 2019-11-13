/**
 * Created by Aram_ on 14-10-2017.
 */
var supertest = require('supertest');
var should = require('should');
var server = supertest.agent("http://localhost:3000");
var authToken;

describe("User registreren", function () {

    it("Het zou een user moeten registreren", function (done) {
        server.post("/api/user/register")
            .send({
                "firstname" : "userTest",
                "insertion" : "userTest",
                "lastname" : "userTest",
                "username" : "userTest",
                "password" : "userTest"
            })
            .expect("Content-type", /json/)
            .expect(200)
            .end(function (err, res) {
                done(err);
            });
    });
});

describe("Inloggen en een token krijgen", function () {

    it("Het zou moeten inloggen en een token teruggeven", function (done) {
        server.post("/api/user/login")
            .send({"username": "userTest", "password": "userTest"})
            .expect("Content-type", /json/)
            .expect(200)
            .end(function (err, res) {
                authToken = res.body['authToken'];
                //!should(authToken).empty();
                done(err);
            });
    });
});

describe("Users ophalen", function () {
    var token;

    it("Het zou moeten inloggen en een token teruggeven", function (done) {
        server.post("/api/user/login")
            .send({"username": "userTest", "password": "userTest"})
            .expect("Content-type", /json/)
            .expect(200)
            .end(function (err, res) {
                token = res.body['authToken'];
                //!should(authToken).empty();
                done(err);
            });
    });

    it("Het zou alle users moeten teruggeven", function (done) {
        server.get("/api/user/")
            .set({"authorization": token})
            .expect("Content-type", /json/)
            .expect(200)
            .end(function (err, res) {
                done(err);
            });
    });

    it("Het zou fout moeten gaan omdat je een verkeerde Token mee geeft", function (done) {
        server.get("/api/user/")
            .set({"authorization": "verkeerdeToken"})
            .expect("Content-type", /json/)
            .expect(403)
            .end(function (err, res) {
                done(err);
            });
    });

    it("Het zou 1 gerbuiker moeten terug geven", function (done) {
        server.get("/api/user/userTest")
            .set({"authorization": token})
            .expect("Content-type", /json/)
            .expect(200)
            .end(function (err, res) {
                done(err);
            });
    });

    it("Het zou fout moeten gaan omdat je een verkeerde Token mee geeft", function (done) {
        server.get("/api/user/userTest")
            .set({"authorization": "verkeerdeToken"})
            .expect("Content-type", /json/)
            .expect(400)
            .end(function (err, res) {
                done(err);
            });
    });
});

describe("Alle films teruggeven", function () {
    it("Zou alle films moeten teruggeven", function (done) {
        server.get("/api/movie/")
            .expect("Content-type", /json/)
            .expect(200)
            .end(function (err, res) {
                done(err);
            });
    });
});

describe("moet 1 movie teruggeven", function () {

    it("Het zou 1 movie terug moeten geven", function (done) {
        server.get("/api/movie/111161")
            .expect("Content-type", /json/)
            .expect(200)
            .end(function (err, res) {
                done(err);
            });
    });

    it("Het zou niks moeten teruggeven (movieID bestaat niet)", function (done) {
        server.get("/api/movie/40")
            .expect("Content-type", /json/)
            .expect(200)
            .end(function (err, res) {
                done(err);
            });
    });
});

describe("Rating toevoegen", function () {
    var token;

    it("Het zou moeten inloggen en een token teruggeven", function (done) {
        server.post("/api/user/login")
            .send({"username": "userTest", "password": "userTest"})
            .expect("Content-type", /json/)
            .expect(200)
            .end(function (err, res) {
                token = res.body['authToken'];
                //!should(authToken).empty();
                done(err);
            });
    });

    it("Het zou een rating moeten toevoegen", function (done) {
        server.post("/api/rating/111161/userTest")
            .set({"authorization": token})
            .send({"ratingNumber": 2})
            .expect("Content-type", /json/)
            .expect(200)
            .end(function (err, res) {
                done(err);
            });
    });

    it("Het zou geen rating moeten toevoegen (verkeerde token)", function (done) {
        server.post("/api/rating/111161/userTest")
            .set({"authorization": 'verkeerdeToken'})
            .send({"ratingNumber": 2})
            .expect("Content-type", /json/)
            .expect(400)
            .end(function (err, res) {
                done(err);
            });
    });
});

describe("Ratings van een movie ophalen", function () {
    var token;

    it("Het zou moeten inloggen en een token teruggeven", function (done) {
        server.post("/api/user/login")
            .send({"username": "userTest", "password": "userTest"})
            .expect("Content-type", /json/)
            .expect(200)
            .end(function (err, res) {
                token = res.body['authToken'];
                //!should(authToken).empty();
                done(err);
            });
    });

    it("Zou en lijst met ratings van een movie ophalen", function (done) {
        server.get("/api/rating/111161")
            .set({"authorization": token})
            .expect("Content-type", /json/)
            .expect(200)
            .end(function (err, res) {
                done(err);
            });
    });

    it("Het zou een averge rating moeten terug geven", function (done) {
        server.get("/api/rating/111161")
            .expect("Content-type", /json/)
            .expect(200)
            .end(function (err, res) {
                done(err);
            });
    });
});

