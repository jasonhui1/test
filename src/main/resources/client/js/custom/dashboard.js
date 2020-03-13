//Author: Alfred Jones
function signOutRequest() {
        $.ajax({
            url: '/user/signout',   //url location of request handler
            type: 'POST',   //Type of request
        });
}