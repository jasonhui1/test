//Author Alfred
//Run on page startup
$(document).ready(function () {
    getSpending();
    getIncome();
});


//Author Alfred
//Request all spending
function getSpending() {
    let spendingHtml = "";
    $.ajax({
        url: "/transaction/get/spending",
        type: "GET",
        success: spendList => {
            if (spendList.hasOwnProperty("error")) {
                //server has returned an error
                alert(spendList.error);
            } else {
                //Success
                //Loop through each transaction returned in the json file
                for (let spend of spendList) {
                    spendingHtml += renderSpending(spend);
                }
                $("#spending_data").html(spendingHtml);
                $('#spending_table').DataTable();
            }
        }
    });
}

//Author Alfred
//Gets all income entries
function getIncome() {
    let incomeHtml = "";
    $.ajax({
        url: "/transaction/get/income",
        type: "GET",
        success: incomeList => {
            if (incomeList.hasOwnProperty("error")) {
                //server has returned an error
                alert(incomeList.error);
            } else {
                //Success
                //Loop through each transaction returned in the json file
                for (let spend of incomeList) {
                    incomeHtml += renderSpending(spend);
                }
                $("#income_data").html(incomeHtml);
                $('#income_table').DataTable();
            }
        }
    });
}

//Author Alfred
//Return time in readable format
function timeConverter(timestamp){
    //Convert unix timestamp to a date object
    let a = new Date(timestamp);
    let hour = a.getHours();
    //If minutes are below 10, display as 01 rather than 1
    let min = (a.getMinutes() < 10 ? "0" : "") + a.getMinutes();
    let time = hour + ":" + min;
    return time;
}

//Author Alfred
//Return date in a readable format
function dateConverter(timestamp){
    let a = new Date(timestamp);
    let year = a.getFullYear();
    let months = ["Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"];
    //Covert month number to months shorthand by using the array above
    let month = months[a.getMonth()];
    let date = a.getDate();
    let time = date + " " + month + " " + year;
    return time;
}

//Author Alfred
//Creates the table entry to display our data
function renderSpending(spending){
    return  `<tr>` +
        `<td>${spending.name}</td>` +
        `<td>${spending.description}</td>` +
        `<td>${spending.typeName}</td>` +
        `<td>${dateConverter(spending.date) + " " + timeConverter(spending.date)}</td>` +
        `<td>${spending.amount/100}</td>` +
        `</tr>`;
}

//Author Ceri
//checks if all forms are completed and simulates button press
function submitIncome(){

    loginForm = $(".incomeForm"); //Select all forms
    buttons = $(".btn-income-submit"); //Select the hidden submit button in all forms
    event.preventDefault();
    //Hide the submit successful info
    $('#informDetails').attr('hidden',true);
    for(i= 0; i  < loginForm.length; i++){
        currentForm = loginForm.eq(i);
        button = buttons.eq(i);
        button.click();

    }

}

//Author Ceri
//sends data to the database
function addIncome(event, filledForm){

    form = $(filledForm);
    divForm = $(".income_columns").has(filledForm)
    event.preventDefault();
    $.ajax({
        url: "/transaction/submit/income",   //url location of request handler
        type: "POST",   //Type of request
        data: form.serialize(),    //extract data from form
        success: response => {  //If a response is received from server
            //Remove the form after submitted
            divForm.remove();
            //Show submitted successful to user
            $('#incomeDetails').removeAttr('hidden');
        }
    });




}
