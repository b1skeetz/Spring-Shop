const categoryChoose = document.getElementById("category");
console.log(categoryChoose)

// let properties = [];

categoryChoose.onchange = function () {
    const categoryId = categoryChoose.value;
    const url = `http://localhost:8080/products/create/properties?id=${categoryId}`;
    fetch(url)
        .then(async (response) => {
            let responseText = await response.text();
            const propertiesElem = document.getElementById('properties');
            propertiesElem.innerHTML = responseText;
        });
};
