//Author Alfred
//Run on page startup
$(document).ready(function () {
    getIncome();
    getSpending();
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
        `<td>${'£' + (spending.amount/100).toFixed(2)}</td>` +
        `</tr>`;
}

//Author Jason
//Change the icon when the different option is selected in the selecteed type drop down box
function changeCateIcon(selectedCate){

    //icon class
    var icons = {Food: "fas fa-utensils", Travel: "fas fa-bus", none: "fas", Entertainment: "fas fa-gamepad", Groceries: "fas fa-shopping-basket", Shopping: "fas fa-shopping-cart", Other:"fas fa-question"};
    var value = selectedCate.value;
    //Find the icon in the right form
    $("form").has(selectedCate).find(".optionsIcon").addClass(icons[value]).removeAttr('hidden');

}

//Author Jason
//Add a new column when the user clicks the add column button
function addInputColumn(clickedButton){

    //Select the form that has this button
    divForm = $(".spending_columns").has(clickedButton);
    //Content of a new column
    $(divForm).after('<div class="spending_columns col-md-5 mx-auto"> <form class="spendingForm" onsubmit="addSpending(event,this)"> <div class="form-group mb-3"> <label> Amount</label> <div class="input-group"> <div class="input-group-prepend" > <span class="input-group-text" style="color:black; width:40px">£</span> </div> <input type="number" class="form-control" name="amount" min="0" max="21474836.47" step=".01" placeholder="Transaction amount" required> </div> </div> <div class="form-group mb-3"> <label> Name</label> <input type="text" class="form-control" placeholder="Transaction name" name="name" required> </div> <div class="form-group mb-3"> <label> Description (optional) </label> <input type="text" class="form-control" placeholder="Transaction description" name="description" > </div> <div class="form-group mb-3"> <label> Select a category</label> <div class="input-spending"> <div class="input-group"> <select required name="type" class="form-control custom-select inline" onchange="changeCateIcon(this)"> <option value="" selected disabled hidden>Select a category</option> <option value="Entertainment">Entertainment</option> <option value="Shopping">Shopping</option> <option value="Groceries">Groceries </option> <option value="Food">Food </option> <option value="Travel">Travel </option> <option value="Other">Other </option> </select> <div class="input-group-append" > <span class="input-group-text" style="color:black; width:40px"> <i style="width: 16px" class="optionsIcon fas" hidden></i> </span> </div> </div> </div> </div> <div class="form-group mb-3"> <label> Date</label> <input type="datetime-local" class="form-control spending-date" name="date"  required> </div> <div class="form-group mb-5"> <button type="button" class="btn btn-primary mx-auto" onclick="addInputColumn(this)"> Add New Column</button> <button type="button" class="btn btn-secondary mx-auto" onclick="removeInputColumn(this)"> Remove Column</button> </div> <button type="submit" class="btn btn-primary btn-spending-submit" hidden>Submit Form</button> </form> </div>')
    //Set the date to the current time
    setFormDate(divForm.next());

}

//Author Jason
//Add a new column when there is no more column
function addInputColumnFirst(){

    //Select the wrapper class of the columns
    divContainer = $("#flex-container-spending");
    $(divContainer).append('<div class="spending_columns col-md-5 mx-auto"> <form class="spendingForm" onsubmit="addSpending(event,this)"> <div class="form-group mb-3"> <label> Amount</label> <div class="input-group"> <div class="input-group-prepend" > <span class="input-group-text" style="color:black; width:40px">£</span> </div> <input type="number" class="form-control" name="amount" min="0" step=".01" placeholder="Transaction amount" required> </div> </div> <div class="form-group mb-3"> <label> Name</label> <input type="text" class="form-control" placeholder="Transaction name" name="name" required> </div> <div class="form-group mb-3"> <label> Description (optional) </label> <input type="text" class="form-control" placeholder="Transaction description" name="description" > </div> <div class="form-group mb-3"> <label> Select a category</label> <div class="input-spending"> <div class="input-group"> <select required name="type" class="form-control custom-select inline" onchange="changeCateIcon(this)"> <option value="" selected disabled hidden>Select a category</option> <option value="Entertainment">Entertainment</option> <option value="Shopping">Shopping</option> <option value="Groceries">Groceries </option> <option value="Food">Food </option> <option value="Travel">Travel </option> <option value="Other">Other </option> </select> <div class="input-group-append" > <span class="input-group-text" style="color:black; width:40px"> <i style="width: 16px" class="optionsIcon fas" hidden></i> </span> </div> </div> </div> </div> <div class="form-group mb-3"> <label> Date</label> <input type="datetime-local" class="form-control spending-date" name="date"  required> </div> <div class="form-group mb-5"> <button type="button" class="btn btn-primary mx-auto" onclick="addInputColumn(this)"> Add New Column</button> <button type="button" class="btn btn-secondary mx-auto" onclick="removeInputColumn(this)"> Remove Column</button> </div> <button type="submit" class="btn btn-primary btn-spending-submit" hidden>Submit Form</button> </form> </div>')
    //Select the div form just created
    divForm = $(".spendingForm").eq(0);
     //Set the date to the current time
    setFormDate(divForm);

}

//Author Jason
//Remove a column when the user clicks the remove column button
function removeInputColumn(clickedButton){

    //Select the div form that this this button
    divForm = $(".spending_columns").has(clickedButton);
    //Remove this form
    divForm.remove();
    checkEmptyColumn();
}

//Author Jason
//Simulate click, submit in all forms when the user clicks the submit button
function submitSpending(){

    form = $(".spendingForm"); //Select all forms
    buttons = $(".btn-spending-submit"); //Select the hidden submit button in all forms
    event.preventDefault();
    //Hide the submit successful info
    $('#informDetails').attr('hidden',true);
    //Loop all forms and submit them
    for(i= 0; i  < form.length; i++){
        currentForm = form.eq(i);
        button = buttons.eq(i);
        button.click();

    }
}

//Author Jason
//Submit the form and sent to database
function addSpending(event, filledForm){

    form = $(filledForm);
    divForm = $(".spending_columns").has(filledForm)
    event.preventDefault();
    $.ajax({
        url: "/transaction/submit/add",   //url location of request handler
        type: "POST",   //Type of request
        data: form.serialize(),    //extract data from form
        success: response => {  //If a response is received from server

         }
    });

    //Remove the form after submitted
    divForm.remove()
    checkEmptyColumn();
    //Show submitted successful to user
    $('#informDetails').removeAttr('hidden');


}

//Author Jason
//Check if there is anymore columns and add a column if true
function checkEmptyColumn(){
    form = $(".spending_columns");
    if(form.length == 0){
        addInputColumnFirst();
    }
}

var today;

//Author Jason
function calculateCurrentTime(){
     today = new Date();
    var dd = String(today.getDate()).padStart(2, '0');
    var mm = String(today.getMonth() + 1).padStart(2, '0'); //January is 0!
    var yyyy = today.getFullYear();
    var timezone = today.getTimezoneOffset();
    var hour = String("0" + (today.getUTCHours() - Math.floor(timezone/60))).slice(-2);
    var min = String("0" + (today.getUTCMinutes() + Math.floor(timezone%60))).slice(-2);

    today = yyyy + '-' + mm + '-' + dd + 'T' + hour + ':' + min; //Format needed for the date value
    setDate();
}

//Author Jason
//Set the date input to the current time when the modal is shown
function setDate(){
    field = $(".spending-date")
    for (i = 0; i < field.length; i++){
        field.eq(i).val(today);
    }
}

//Set the date to the current time to the new form created
function setFormDate(form){
    //Select the date field in the form just created
    field = $(form).find(".spending-date");
    field.val(today);
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



