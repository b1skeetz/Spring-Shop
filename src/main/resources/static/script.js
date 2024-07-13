const categoryChoose = document.getElementById("category");
console.log(categoryChoose)

// let properties = [];

categoryChoose.onchange = function () {
    alert('abc')
    const categoryId = categoryChoose.value;
    const url = `http://localhost:8080/products/create/properties?id=${categoryId}`;
    let request = new XMLHttpRequest(); // fetch или axios
    request.open("GET", url);
    request.onload = function () {
        console.log(request.response)
        properties = request.response;
    };
};
