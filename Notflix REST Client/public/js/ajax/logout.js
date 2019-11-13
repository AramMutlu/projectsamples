/**
 * Created by Aram_ on 2-11-2017.
 */

/**
 * Als er op de uitlog button wordt geklikt wordt de localstorage leeg gemaakt.
 */
$(document).ready(function() {
    $("#button-logout").click(function () {
       localStorage.removeItem("authToken");
       localStorage.removeItem("username");
       location.reload();
    });
});