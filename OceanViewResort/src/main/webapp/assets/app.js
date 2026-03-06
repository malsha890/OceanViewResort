// Sidebar active menu

const menuItems = document.querySelectorAll(".sidebar ul li");

menuItems.forEach(item => {
item.addEventListener("click", () => {

menuItems.forEach(i => i.classList.remove("active"));
item.classList.add("active");

});
});


// Card counter animation

const counters = document.querySelectorAll(".card p");

counters.forEach(counter => {

let value = 0;
let target = parseInt(counter.innerText);

let interval = setInterval(()=>{

value++;

counter.innerText = value;

if(value >= target){
clearInterval(interval);
}

},20);

});


// Simple table search

function searchTable(inputId, tableId){

let input = document.getElementById(inputId);
let filter = input.value.toLowerCase();
let rows = document.querySelectorAll("#"+tableId+" tbody tr");

rows.forEach(row=>{
row.style.display =
row.innerText.toLowerCase().includes(filter) ? "" : "none";
});

}