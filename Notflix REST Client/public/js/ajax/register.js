/**
 * Created by Aram_ on 31-10-2017.
 */
var $modalAnimateTime = 300;
var $msgAnimateTime = 150;
var $msgShowTime = 2000;

$(document).ready(function() {
    /**
     * Een functie die uit wordt gevoerd als en op de registreer knop wordt gedrukt.
     */
    $("#register-form").submit(function (event) {
        event.preventDefault();

        var username = $("#register_username").val();
        var password = $("#register_password").val();
        var firstName = $("#register_firstname").val();
        var prefix = $("#register_prefix").val();
        var lastName = $("#register_lastname").val();




        // Als de prefix leeg is,
        // Doe een request om een user te maken zonder prefix
        if (prefix == "") {
            $.ajax({
                method: "POST",
                url: "/api/user/register",
                dataType: "json",
                data:
                    {
                        lastname: lastName,
                        firstname: firstName,
                        username: username,
                        password: password
                    },
                success: function () {
                    msgChange($('#div-register-msg'), $('#icon-register-msg'), $('#text-register-msg'), "success", "glyphicon-ok", "Register OK");
                },
                error: function () {
                }
            });
            // Als de prefix niet leeg is,
            // Doe een request om een user te maken mét prefix
        } else {
            $.ajax({
                method: "POST",
                url: "/api/user/register",
                dataType: "json",
                data:
                    {
                        lastname: lastName,
                        insertion: prefix,
                        firstname: firstName,
                        username: username,
                        password: password
                    },
                success: function () {
                    msgChange($('#div-register-msg'), $('#icon-register-msg'), $('#text-register-msg'), "success", "glyphicon-ok", "Register OK");
                },
                error: function () {
                }
            });
        }

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