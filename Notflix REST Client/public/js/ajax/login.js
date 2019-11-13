/**
 * Created by Aram_ on 31-10-2017.
 */
/**
 * Functie die aan wordt geroepen zodra het document wordt geladen.
 */
var $modalAnimateTime = 300;
var $msgAnimateTime = 150;
var $msgShowTime = 2000;

/**
 * AJAX call om in te loggen
 */
$(document).ready(function() {
    $("#login-form").submit(function (event) {
        // Zorg dat de form zelf geen request doet
        event.preventDefault();

        // Haal de username en password op van de site
        var username = $("#login_username").val();
        var password = $("#login_password").val();

        // Ajax POST request om in te loggen en een Token terug te krijgen.
        $.ajax({
            method: "POST",
            url: "/api/user/login",
            dataType: "json",
            data: {
                username: username,
                password: password
            },
            success: function (data) {
                //sla het token op in de localStorage
                localStorage.setItem("username", username);
                localStorage.setItem("authToken", data['authToken']);
                msgChange($('#div-login-msg'), $('#icon-login-msg'), $('#text-login-msg'), "success", "glyphicon-ok", "Login OK");
            },
            error: function (err) {
                console.log(err);
            }
        });
    });
});

function msgChange($divTag, $iconTag, $textTag, $divClass, $iconClass, $msgText) {
    var $msgOld = $divTag.text();
    msgFade($textTag, $msgText);
    $divTag.addClass($divClass);
    $iconTag.removeClass("glyphicon-chevron-right");
    $iconTag.addClass($iconClass + " " + $divClass);
    setTimeout(function() {
        msgFade($textTag, $msgOld);
        $divTag.removeClass($divClass);
        $iconTag.addClass("glyphicon-chevron-right");
        $iconTag.removeClass($iconClass + " " + $divClass);
    }, $msgShowTime);
}
function msgFade ($msgId, $msgText) {
    $msgId.fadeOut($msgAnimateTime, function() {
        $(this).text($msgText).fadeIn($msgAnimateTime);
    });
}