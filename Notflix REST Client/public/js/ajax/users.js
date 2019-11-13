/**
 * Created by Aram_ on 1-11-2017.
 */
/**
 * AJAX call om alle users op te halen en te printen
 */
$(document).ready(function () {
    $.ajax({
        type: 'GET',
        url: '/api/user/',
        dataType: 'json',
        headers : {
            authorization: localStorage.getItem("authToken")
        },
        success: function (users) {

            //print de user op de website
            for (i = 0; i < users.length; i++) {
                $('#users-container').append(
                    $('<div class="row well"> <div class="row"> <div class="col-md-3"></div> <div class="col-md-9"> <div class="row"> <div class="col-md-12"><p><b>Firstname: </b> '+ users[i].firstName + '</p></div> </div> <div class="row"> <div class="col-md-12"><p><b>Prefix: </b>'+ users[i].insertion + '</p></div> </div> <div class="row"> <div class="col-md-12"><p><b>LastName: </b>'+ users[i].lastName + '</p></div> </div> <div class="row"> <div class="col-md-12"><p><b>Username: </b>'+ users[i].username + '</p></div> </div> </div> </div> </div> </div>')
                );
            }

        },
        error: function () {
            console.log('Error');
        }
    });
});