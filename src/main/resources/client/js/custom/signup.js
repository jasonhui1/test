//Author: Alfred Jones

function signUpRequest() {
    const loginForm = $('#signUpForm');  //Form element used to login
    //TODO Add password strength validation
    loginForm.submit(event => {
        event.preventDefault(); //Stop the default handling of the form submit
        $.ajax({
            url: '/user/add',   //url location of request handler
            type: 'POST',   //Type of request
            data: loginForm.serialize(),    //extract data from form
            success: response => {  //If a response is received from server
                if (response.startsWith("Error:")) {
                    alert(response);    //Display the error message
                }else if(response.startsWith("BadDetails:")){
                    //TODO Highlight fields with bad data rather than just displaying a message
                    alert(response);
                }else {
                    window.location.href = "/login";    //redirect to dashboard
                }
            }
        });
    });
}