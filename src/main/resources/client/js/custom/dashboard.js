//Author Alfred
//Run on page startup
var budget = -1
$(document).ready(function () {

    getIncome();
    getSpending();
    getBudget();

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
    $(divForm).after('<div class="spending_columns col-md-5 mx-auto"> <form class="spendingForm" onsubmit="addSpending(event,this)"> <div class="form-group mb-3"> <label class="custom-control pl-0"> Amount <span class="input-group mt-2"> <span class="input-group-prepend" > <span class="input-group-text" style="color:black; width:40px">£</span> </span> <input type="number" class="form-control" name="amount" min="0" max="21474836.47" step=".01" placeholder="Transaction amount" required> </span> </label> </div> <div class="form-group mb-3"> <label class="custom-control pl-0"> Name <input type="text" class="form-control mt-2" placeholder="Transaction name" name="name" required> </label> </div> <div class="form-group mb-3"> <label class="custom-control pl-0"> Description (optional) <input type="text" class="form-control mt-2" placeholder="Transaction description" name="description" > </label> </div> <div class="form-group mb-3"> <label class="custom-control pl-0"> Select a category <span class="input-spending mt-2"> <span class="input-group"> <select required name="type" class="form-control custom-select inline" onchange="changeCateIcon(this)"> <option value="" selected disabled hidden>Select a category</option> <option value="Entertainment">Entertainment</option> <option value="Shopping">Shopping</option> <option value="Groceries">Groceries </option> <option value="Food">Food </option> <option value="Travel">Travel </option> <option value="Other">Other </option> </select> <span class="input-group-append" > <span class="input-group-text" style="color:black; width:40px"> <i style="width: 16px" class="optionsIcon fas" hidden></i> </span> </span> </span> </span> </label> </div> <div class="form-group mb-3"> <label class="custom-control pl-0"> Date <input type="datetime-local" class="form-control spending-date mt-2" name="date" max="2035-12-31T23:59" required onblur="change_end_date_min(this)"> </label> </div> <div class="form-group mb-3"> <div class="form-check"> <label class="container ml-0"> <input class="form-check-input" type="checkbox" onchange="ShowRecurringForm(this)">Recurring payment? </label> </div> </div> <div class="recurring_form" hidden> <label class="custom-control pl-0"> Payment Interval <span class="form-group mb-3 row ml-0"> <input type="number" min="0.1" max="2147483647" step="0.1" class="form-control recurring_interval_time mt-2 col-4" onchange="setTimeInterval(this)"> <select  name="type" class="form-control custom-select col-4 mt-2 recurring_interval_type ml-2" onchange="setTimeInterval(this)"> <option value="hour" selected>Hour</option> <option value="day">Day</option> <option value="month">Month</option> <option value="year">Year</option> </select> </span> </label> <input type="number" class="time_interval_in_hours" value="0" step= "0.1" name="recurring_interval" hidden> <div class="form-group mb-3"> <label class="custom-control pl-0"> Recurring payment end date <input type="datetime-local" class="form-control recurring_end_date mt-2" name="end_date" max="2035-12-31T23:59"> </label> </div> </div> <div class="form-group mb-5"> <button type="button" class="btn btn-primary mx-auto" onclick="addInputColumn(this)"> Add New Column</button> <button type="button" class="btn btn-secondary mx-auto" onclick="removeInputColumn(this)"> Remove Column</button> </div> <button type="submit" class="btn btn-primary btn-spending-submit" hidden>Submit Form</button> </form> </div>')


    //Set the date to the current time
    setFormDate(divForm.next());

}

//Author Jason
//Add a new column when there is no more column
function addInputColumnFirst(){

    //Select the wrapper class of the columns
    divContainer = $("#flex-container-spending");
    $(divContainer).append('<div class="spending_columns col-md-5 mx-auto"> <form class="spendingForm" onsubmit="addSpending(event,this)"> <div class="form-group mb-3"> <label class="custom-control pl-0"> Amount <span class="input-group mt-2"> <span class="input-group-prepend" > <span class="input-group-text" style="color:black; width:40px">£</span> </span> <input type="number" class="form-control" name="amount" min="0" max="21474836.47" step=".01" placeholder="Transaction amount" required> </span> </label> </div> <div class="form-group mb-3"> <label class="custom-control pl-0"> Name <input type="text" class="form-control mt-2" placeholder="Transaction name" name="name" required> </label> </div> <div class="form-group mb-3"> <label class="custom-control pl-0"> Description (optional) <input type="text" class="form-control mt-2" placeholder="Transaction description" name="description" > </label> </div> <div class="form-group mb-3"> <label class="custom-control pl-0"> Select a category <span class="input-spending mt-2"> <span class="input-group"> <select required name="type" class="form-control custom-select inline" onchange="changeCateIcon(this)"> <option value="" selected disabled hidden>Select a category</option> <option value="Entertainment">Entertainment</option> <option value="Shopping">Shopping</option> <option value="Groceries">Groceries </option> <option value="Food">Food </option> <option value="Travel">Travel </option> <option value="Other">Other </option> </select> <span class="input-group-append" > <span class="input-group-text" style="color:black; width:40px"> <i style="width: 16px" class="optionsIcon fas" hidden></i> </span> </span> </span> </span> </label> </div> <div class="form-group mb-3"> <label class="custom-control pl-0"> Date <input type="datetime-local" class="form-control spending-date mt-2" name="date" max="2035-12-31T23:59" required onblur="change_end_date_min(this)"> </label> </div> <div class="form-group mb-3"> <div class="form-check"> <label class="container ml-0"> <input class="form-check-input" type="checkbox" onchange="ShowRecurringForm(this)">Recurring payment? </label> </div> </div> <div class="recurring_form" hidden> <label class="custom-control pl-0"> Payment Interval <span class="form-group mb-3 row ml-0"> <input type="number" min="0.1" max="2147483647" step="0.1" class="form-control recurring_interval_time mt-2 col-4" onchange="setTimeInterval(this)"> <select  name="type" class="form-control custom-select col-4 mt-2 recurring_interval_type ml-2" onchange="setTimeInterval(this)"> <option value="hour" selected>Hour</option> <option value="day">Day</option> <option value="month">Month</option> <option value="year">Year</option> </select> </span> </label> <input type="number" class="time_interval_in_hours" value="0" step= "0.1" name="recurring_interval" hidden> <div class="form-group mb-3"> <label class="custom-control pl-0"> Recurring payment end date <input type="datetime-local" class="form-control recurring_end_date mt-2" name="end_date" max="2035-12-31T23:59"> </label> </div> </div> <div class="form-group mb-5"> <button type="button" class="btn btn-primary mx-auto" onclick="addInputColumn(this)"> Add New Column</button> <button type="button" class="btn btn-secondary mx-auto" onclick="removeInputColumn(this)"> Remove Column</button> </div> <button type="submit" class="btn btn-primary btn-spending-submit" hidden>Submit Form</button> </form> </div>')
    //Select the div form just created
    divForm = $(".spendingForm").eq(0);
    //Set the date to the current time
    setFormDate(divForm);
    change_end_date_min(form)

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
    len = form.length;
    //Hide the submit successful info
    $('#informDetails').attr('hidden',true);
    //Loop all forms and submit them
//    for(i= 0; i  < form.length; i++){
    for(i = len -1; i >= 0; i--){
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
    var hour = String("0" + ((today.getUTCHours() - Math.floor(timezone/60)) % 24)).slice(-2);
    var min = String("0" + ((today.getUTCMinutes() + Math.floor(timezone%60)) % 60)).slice(-2);

    today = yyyy + '-' + mm + '-' + dd + 'T' + hour + ':' + min; //Format needed for the date value
    setDate();

}

//Author Jason
//Set the date input to the current time when the modal is shown
//Add validation to end date (must be after start date
function setDate(){
    spending_date = $("form").find(".spending-date");
    end_date = $("form").find(".recurring_end_date")
    for (i = 0; i < spending_date.length; i++){
        spending_date.eq(i).val(today);
        change_end_date_min(spending_date.eq(i));

    }
}

//Author Jason
//Set the date to the current time to the new form created
function setFormDate(form){
    //Select the date field in the form just created
    spending_date = $(form).find(".spending-date");
    spending_date.val(today);
    change_end_date_min(spending_date)

}

//Author Jason
//Show the recurring fields if the payment is recurring
function ShowRecurringForm(cb){

    Block = $(".spending_columns").has(cb).find(".recurring_form")
    field1 = Block.find(".recurring_interval_time")
    field2 = Block.find(".recurring_interval_type")
    field3 = Block.find(".recurring_end_date")
    if(cb.checked){

        Block.removeAttr("hidden")
        field1.attr("required", true)
        field2.attr("required", true)
        field3.attr("required", true)

    } else {

        Block.attr("hidden",true)
        field1.removeAttr("required")
        field2.removeAttr("required")
        field3.removeAttr("required")
    }

}

//Author Jason
//Set end date must be later than start date
function change_end_date_min(input){
    Block = $("form").has(input)
    Block.find(".recurring_end_date").attr({"min": input.val()})

}

//Author Jason
//Convert interval to second and make sure it doesnt exceed the maximum integer bits
function setTimeInterval(input){

    Block = $(".recurring_form").has(input)
    TimeBlock = Block.find(".recurring_interval_time")
    Time = TimeBlock.val()
    Type = Block.find(".recurring_interval_type").val()
    FillBlock = Block.find(".time_interval_in_hours")
    convertType = {hour : 1, day: 24, month: 720, year: 8760}
    second = Time*convertType[Type]
    FillBlock.val(second)
    TimeBlock.attr({"max": (2147483640/convertType[Type]).toFixed(1)})

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

//Author Jason
//add/change budget to the database
function addBudget(event, filledForm){
    form = $(filledForm);
    event.preventDefault();
    console.log(budget)
    //if a budget is set before, add this budget, else update it
    if(budget < 0){
        $.ajax({
            url: "budget/add",   //url location of request handler
            type: "POST",   //Type of request
            data: form.serialize(),    //extract data from form
            success: budgetAmount => {  //If a response is received from server
                budget = budgetAmount
                ('.modal').modal('hide') //Close the modal
                updateShowBudget(budgetAmount) //update the display amount
            }

        });

    } else {
         $.ajax({
            url: "budget/change",   //url location of request handler
            type: "POST",   //Type of request
            data: form.serialize(),    //extract data from form
            success: budgetAmount => {  //If a response is received from server
                budget = budgetAmount
                $('.modal').modal('hide') //Close the modal
                 updateShowBudget(budgetAmount) //update the display amount
            }

         });
    }

}

//Author Jason
//get budget from the database
function getBudget(){
    $.ajax({
        url: "budget/get",   //url location of request handler
        type: "POST",   //Type of request
        success: budgetAmount => {  //If a response is received from server
            budget = budgetAmount
            updateShowBudget(budgetAmount)
        }

    });

}

//Author Jason
//update the display
function updateShowBudget(budget){
    Button = $('.budgetButton')
    if(budget >= 0){
        $("#showBudget").html("£" + budget/100)
        for(i = 0; i < Button.length; i++){
            Button.eq(i).html("Change")
        }
    } else {
        for(i = 0; i < Button.length; i++){
            Button.eq(i).html("Add")
        }
    }
}

//Author Jason
//simulate submit and validation for the budget form
function submitBudget(){

   $("#budgetFormButton").click();
}




