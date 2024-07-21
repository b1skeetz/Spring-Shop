const categoryChoose = document.getElementById("category");
console.log(categoryChoose)

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

const buttonSubmit = document.getElementById("submitting");

const propertiesInputs = document.querySelectorAll('#propName');
const propertiesValuesFromInputs = []

buttonSubmit.onclick = () => {
    propertiesInputs.forEach(prop => propertiesValuesFromInputs.push(prop.textContent))
    console.log(propertiesValuesFromInputs)
}

$.ajax({
    type: "POST",
    url: "/products",
    data: {
        propValues: propertiesValuesFromInputs,
    },
    success: function(response) {
        console.log(response.data())
    },
    error: function(e) {
        alert('Error: ' + e);
    }
});