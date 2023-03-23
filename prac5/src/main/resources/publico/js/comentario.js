

document.querySelectorAll('.eliminar-comentario').forEach(el => {
    el.addEventListener('click', e => {
        e.preventDefault();
        const url = e.target.href;
        fetch(url, {method: 'GET'})
            .then(response => response.text())
            .then(html => {
                // Actualizar la sección de comentarios con la respuesta HTML
                const comentariosDiv = document.querySelector('#comentarios');
                comentariosDiv.innerHTML = html;
            })
            .catch(error => console.error(error));
    });
});
