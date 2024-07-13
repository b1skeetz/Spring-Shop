const categoryChoose = document.querySelector("select");
let properties = [];

categoryChoose.onchange = function () {
    const categoryId = categoryChoose.value;
    const url = `http://localhost:8080/products/create/properties?id=${categoryId}`;
    let request = new XMLHttpRequest();
    request.open("GET", url);
    request.onload = function () {
        properties = request.response;
    };
};

alert("555");
