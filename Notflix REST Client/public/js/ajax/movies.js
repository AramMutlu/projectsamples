/**
 * Created by Aram_ on 31-10-2017.
 */
var movielist;

/**
 * AJAX call om alle movies op te halen en te laten zien op de website
 */
$(document).ready(function () {
    $.ajax({
        type: 'GET',
        url: '/api/movie/',
        dataType: 'json',
        success: function (movies) {

            //print elke movie
            for (i = 0; i < movies.length; i++) {
                $('#movies-container').append(
                    $('<div class="row well"> <div class="row"> <div class="col-md-3" id="image' + i + '"></div> <div class="col-md-9"> <div class="row"> <div class="col-md-12"><p><b>Title: </b> ' + movies[i].title + '</p></div> </div> <div class="row"> <div class="col-md-12"><p><b>Release Date: </b>' + movies[i].dateOfRelease + '</p></div> </div> <div class="row"> <div class="col-md-12"><p><b>Duration (in minutes): </b>' + movies[i].duration + '</p></div> </div> <div class="row"> <div class="col-md-12"><p><b>Director: </b>' + movies[i].director + '</p></div> </div> <div class="row"> <div class="col-md-12"><p><b>Description: </b>' + movies[i].description + '</p></div> </div> <div class="row"> <div class="col-md-12" id="rating-container' + i + '"></div> </div> <div class="row"> <div class="col-md-12" id="rating-user-container' + i + '"></div> </div> <div class="row"> <div class="col-md-12"><p><b>Add/update your rating: </b> <input type="number" min="1" max="5" id="ratingNumber' + i + '" hint="Rating"><button id="button" value=' + i + ' name="button">submit</button></p> </div> </div> </div> </div> </div>')
                );
            }
            movielist = movies;
            images(movies);
            rating(movies);
            //kijk of iemand is ingelogd, zo ja print de userrating
            if (localStorage.getItem("authToken") !== null && typeof localStorage.getItem("authToken") !== undefined){
                userRating(movies);
            }


            /**
             * AJAX call om een rating toe te voegen of te wijzigen
             */
            $("button").click(function () {
                var fired_button = $(this).val();
                if (localStorage.getItem("authToken") !== null && typeof localStorage.getItem("authToken") !== undefined) {
                    $.ajax({
                        type: 'GET',
                        url: '/api/rating/' + movies[fired_button].movieID + '/' + localStorage.getItem("username"),
                        dataType: 'json',
                        async: false,
                        headers: {
                            authorization: localStorage.getItem("authToken")
                        },
                        success: function (rating) {
                            //Als de user al een rating heeft toegevoegd, wijzig deze dan
                            if (typeof rating[0] !== 'undefined' && typeof rating[0].movieID !== 'undefined') {

                                var ratingNumber = $("#ratingNumber" + fired_button).val();

                                $.ajax({
                                    type: 'PUT',
                                    url: '/api/rating/' + movies[fired_button].movieID + '/' + localStorage.getItem("username"),
                                    dataType: 'json',
                                    async: false,
                                    headers: {
                                        authorization: localStorage.getItem("authToken")
                                    },
                                    data: {
                                        ratingNumber: ratingNumber
                                    },
                                    success: function () {
                                        location.reload();
                                    },
                                    error: function () {
                                    }
                                });
                            //als er nog geen rating is, voeg deze dan toe
                            } else {
                                var ratingNumber = $("#ratingNumber" + fired_button).val();

                                $.ajax({
                                    type: 'POST',
                                    url: '/api/rating/' + movies[fired_button].movieID + '/' + localStorage.getItem("username"),
                                    dataType: 'json',
                                    async: false,
                                    headers: {
                                        authorization: localStorage.getItem("authToken")
                                    },
                                    data: {
                                        ratingNumber: ratingNumber
                                    },
                                    success: function () {
                                        location.reload();
                                    },
                                    error: function () {
                                    }
                                });
                            }

                        },
                        error: function () {

                        }
                    });
                } else{
                    alert("You have to log in to use this function!");
                }
            });
        },
        error: function () {
            console.log('Error');
        }
    });


});


/**
 * Function met een AJAX call die de rating van de user ophaalt en print
 * @param movies lijst met alle movies
 */
function userRating(movies) {
    for (a = 0; a < movies.length; a++) {
        $.ajax({
            type: 'GET',
            url: '/api/rating/' + movies[a].movieID + '/' + localStorage.getItem("username"),
            dataType: 'json',
            async: false,
            headers: {
                authorization: localStorage.getItem("authToken")
            },
            success: function (rating) {
                //kijk of er een rating is, zo ja print deze
                if (typeof rating[0] !== 'undefined' && typeof rating[0].ratingNumber !== 'undefined' && rating[0].ratingNumber !== null) {
                    $('#rating-user-container' + a + '').append(
                        "<p><b>User rating: </b>" + rating[0].ratingNumber + " out of 5     <button id='button-delete' value='"+ a+ "' name='button-delete'>Delete Rating</button></p>"
                    );

                    //AJAX call om een rating te verwijderen
                    $("button-delete").click(function () {
                        var fired_button = $(this).val();
                        $.ajax({
                            type: 'DELETE',
                            url: '/api/rating/' + movies[fired_button].movieID + '/' + localStorage.getItem("username"),
                            dataType: 'json',
                            async: false,
                            headers: {
                                authorization: localStorage.getItem("authToken")
                            },
                            success: function (rating) {

                                if (typeof rating[0] !== 'undefined' && typeof rating[0].movieID !== 'undefined') {

                                    var ratingNumber = $("#ratingNumber" + fired_button).val();

                                    $.ajax({
                                        type: 'PUT',
                                        url: '/api/rating/' + movies[fired_button].movieID + '/' + localStorage.getItem("username"),
                                        dataType: 'json',
                                        async: false,
                                        headers: {
                                            authorization: localStorage.getItem("authToken")
                                        },
                                        success: function () {
                                            location.reload();
                                        },
                                        error: function () {
                                        }
                                    });
                                }
                            }, error: function () {
                            }
                        });
                    });
                }
            }, error: function () {
            }
        });
    }
}

/**
 * Function met een AJAX call om de gemiddelde rating op te halen.
 * @param movies Lijst met alle movies
 */
function rating(movies) {
    for (x = 0; x < movies.length; x++) {
        $.ajax({
            type: 'GET',
            url: '/api/rating/' + movies[x].movieID,
            dataType: 'json',
            async: false,
            headers: {
                authorization: localStorage.getItem("authToken")
            },
            success: function (average) {
                //Als er een average is, print deze
                if (typeof average !== 'undefined' && average !== null) {
                    $('#rating-container' + x + '').append(
                        "<p><b>Rating (avg): </b>" + average + " out of 5</p>"
                    );
                }
            },
            error: function () {
            }
        });
    }
}

/**
 * Function met een AJAX call die de images ophaalt van de myapifilms database ophaalt
 * @param movies
 */
function images(movies) {
    for (j = 0; j < movies.length; j++) {
        var id = movies[j].movieID.toString();
        var newID = "0";
        if (id.length < 7) {
            for (k = 1; k < (7 - id.length); k++) {
                newID += "0";
            }
            newID += id;
        }

        $.ajax({
            type: 'GET',
            url: 'http://www.myapifilms.com/imdb/idIMDB?idIMDB=tt' + newID + '&token=15740a94-7463-4b0b-b25a-e6b44b920ebe',
            dataType: 'json',
            async: false,
            success: function (posterURL) {
                //print de poster/image
                var url = posterURL.data.movies[0].urlPoster;
                $('#image' + j + '').append(
                    $('<img src="' + url + '">')
                );
            },
            error: function () {
                console.log('Error');
            }
        });
    }
}