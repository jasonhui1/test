//Author: Alfred Jones

function loginRequest() {
    const loginForm = $('#loginForm');  //Form element used to login
    const header = $('#badDetails');    //<h2> element containing bad details message
    header.css("visibility", "hidden");
    loginForm.submit(event => {
        event.preventDefault(); //Stop the default handling of the form submit
        $.ajax({
            url: '/user/login',   //url location of request handler
            type: 'POST',   //Type of request
            data: loginForm.serialize(),    //extract data from form
            success: response => {  //If a response is received from server
                if (response.startsWith("Error:")) {
                    alert(response);    //Display the error message
                    header.css("visibility", "hidden"); //Hide the bad details error message
                }else if(response === ("BadDetails")){
                    header.css("visibility", "visible");    //Show the bad details error message
                }else {
                    header.css("visibility", "hidden");     //Hide the bad details error message
                    Cookies.set("sessionToken", response);  //Store session token in cookie
                    window.location.href = "/dashboard";    //redirect to dashboard
                }
            }
        });
    });
}