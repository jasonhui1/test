//Author: Alfred Jones
function logoutRequest() {
        $.ajax({
            url: '/user/logout',   //url location of request handler
            type: 'POST',   //Type of request
            success: response => {  //If a response is received from server
                window.location.href = "../"
            }
        });
}