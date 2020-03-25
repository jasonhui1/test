//Original author: Alfie Jones
//Adapted for use to change details by: Matthew Johnson
function changeDetailsRequest(){
    const changeDetailsForm = $("#changeDetailsForm"); //form for which details to be changed are entered
    changeDetailsForm.submit(event => {
        event.preventDefault(); //Stop the default handling of the form submit
        if (checkPassword()) {
            $.ajax({
                url: "/user/amend",   //url location of request handler
                type: "POST",   //Type of request
                data: changeDetailsForm.serialize(),    //extract data from form
                success: response => {  //If a response is received from server
                    if (response.startsWith("Error:")) {
                        alert(response);    //Display the error message
                    } else if (response.startsWith("BadDetails:")) {
                        //TODO Highlight fields with bad data rather than just displaying a message
                        alert(response);
                    }
                }
            });
            //TODO Figure out how to redirect to dashboard after
            window.location.href = "/dashboard";
        }
    });
}

function deleteFunction() {
    if (confirm("Are you sure you want to delete your account?")) {
        $.ajax({
            url: "/user/delete",   //url location of request handler
            type: "POST",   //Type of request

        });
        window.location.href = "/login" //make go to login page
    }
}



//author Alfie Jones
//Checks the passwords to ensure they're valid
//TODO check strength of the passwords
function checkPassword() {
    const inputPass1 = $("#newPasswordInput1");
    const inputPass2 = $("#newPasswordInput2");

    if(inputPass1.val() !== inputPass2.val()){
        alert("The two passwords aren't matching");
        return false;
    }
    return true;
}