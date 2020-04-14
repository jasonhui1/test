//Original author: Alfie Jones
//Adapted for use to change details by: Matthew Johnson
function changeDetailsRequest(){
    const changeDetailsForm = $("#changeDetailsForm"); //form for which details to be changed are entered
        if (checkPassword()) {

            $.ajax({
                url: '/user/amend',   //url location of request handler
                type: 'POST',   //Type of request
                data: changeDetailsForm.serialize(),    //extract data from form

                success: response => {
                    if (response.startsWith("Error")){
                        alert("Details could not be changed! Please try again.");
                    }

                    if (response.startsWith("Email")){
                        alert("Email already in use - Details weren't changed!");
                    }

                    if (response.startsWith("Success")){
                        alert("Details have been updated!");
                    }
                }
            });

        }
}

function deleteFunction() {
    if (confirm("Are you sure you want to delete your account?")) {
        $.ajax({
            url: "/user/delete",   //url location of request handler
            type: "POST",   //Type of request
            success: response => {  //If a response is received from server
                window.location.href = "../"; //make go to login page
            }

        });

    }
}



//author Alfie Jones
//Checks the passwords to ensure they're valid
//TODO check strength of the passwords
function checkPassword() {
    const inputPass1 = $("#newPasswordInput1");
    const inputPass2 = $("#newPasswordInput2");

    if(inputPass1.val() !== inputPass2.val()){
        alert("The two passwords aren't matching - Please re-enter and try again");
        return false;
    }
    return true;
}