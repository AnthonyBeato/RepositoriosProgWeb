
const functionDomain = () => {
    var data = {key: '37dc829dd6caa19d224b29dfdc36f8f4', q: document.getElementById("dominio").value}
    fetch('https://api.linkpreview.net', {
        method: 'POST',
        mode: 'cors',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data),
    })
        .then(res => res.json())
        .then(response => {
            if(response.error) {
                console.log("ERROR")
                document.getElementById("submit").disabled  = true
            } else {
                document.getElementById("submit").disabled  = false
            }
            document.getElementById("mytitle").innerHTML = response.title
            document.getElementById("mydescription").innerHTML = response.description
            document.getElementById("myimage").src = response.image
            document.getElementById("myurl").innerHTML = response.url
            document.getElementById("myurl").href = response.url
        })
}
document.getElementById("dominio").addEventListener("change", functionDomain);



// Almacenar el token JWT en el almacenamiento local del navegador
localStorage.setItem("token", "tu_token_jwt");

// Hacer una solicitud AJAX incluyendo el token en la cabecera 'Authorization'
$.ajax({
    url: "ruta_protegida",
    method: "GET",
    beforeSend: function(xhr) {
        xhr.setRequestHeader("Authorization", localStorage.getItem("token"));
    },
    success: function(data) {
        // Manejar la respuesta del servidor
    }
});