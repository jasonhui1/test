//Author: Alfred Jones

function signUpRequest() {
    const loginForm = $("#signUpForm");  //Form element used to login

    const inputPass1 = $("#passwordInput1");
    const inputPass2 = $("#passwordInput2");

    //TODO Add password strength validation

    loginForm.submit(event => {
        inputPass2.setCustomValidity(inputPass2.value !== inputPass1.value ? "Passwords do not match." : "");
        event.preventDefault(); //Stop the default handling of the form submit
        if (checkPassword()) {
            $.ajax({
                url: "/user/add",   //url location of request handler
                type: "POST",   //Type of request
                data: loginForm.serialize(),    //extract data from form
                success: response => {  //If a response is received from server
                    if (response.startsWith("Error:")) {
                        alert(response);    //Display the error message
                    } else if (response.startsWith("BadDetails:")) {
                        //TODO Highlight fields with bad data rather than just displaying a message
                        alert(response);
                    } else {
                        window.location.href = "/login";    //redirect to dashboard
                    }
                }
            });
        }
    });
}

//Checks the passwords to ensure they're valid
//TODO check strength of the passwords
function checkPassword() {
    const inputPass1 = $("#passwordInput1");
    const inputPass2 = $("#passwordInput2");

    if(inputPass1.val() !== inputPass2.val()){
        alert("The two passwords aren't matching");
        return false;
    }
    return true;
}

