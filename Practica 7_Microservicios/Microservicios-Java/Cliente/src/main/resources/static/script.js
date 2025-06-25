const API_URL = "http://localhost:8083"; // API Gateway

// Obtener la cartelera y la disponibilidad de boletos
async function obtenerPeliculas() {
    try {
        const response = await fetch(`${API_URL}/inventario/peliculas`);
        if (response.ok) {
            const peliculas = await response.json();
            mostrarPeliculas(peliculas);
        } else {
            document.getElementById("peliculas").innerText = "Error al obtener la cartelera";
        }
    } catch (error) {
        document.getElementById("peliculas").innerText = "No se pudo conectar al servidor";
    }
}

// Mostrar las películas en el frontend
function mostrarPeliculas(peliculas) {
    const peliculasDiv = document.getElementById("peliculas");
    peliculasDiv.innerHTML = ''; // Limpiar el contenedor antes de mostrar las películas

    peliculas.forEach((pelicula) => {
        const peliculaDiv = document.createElement("div");
        peliculaDiv.classList.add("producto");

        peliculaDiv.innerHTML = `
            <label for="cantidad-${pelicula.id}">${pelicula.titulo} - Boletos disponibles: ${pelicula.boletosDisponibles}</label>
            <input type="number" id="cantidad-${pelicula.id}" min="0" max="${pelicula.boletosDisponibles}" value="0" />
        `;

        peliculasDiv.appendChild(peliculaDiv);
    });
}

// Realizar la compra de boletos
async function comprarBoletos() {
    const nombre = document.getElementById("nombreCliente").value;
    
    if (!nombre) {
        document.getElementById("mensaje").innerText = "Por favor, ingresa tu nombre.";
        return;
    }

    const compras = [];
    const peliculas = document.querySelectorAll("#peliculas div");

    peliculas.forEach((peliculaDiv) => {
        const peliculaId = peliculaDiv.querySelector("input").id.split("-")[1];
        const cantidad = document.getElementById(`cantidad-${peliculaId}`).value;

        if (cantidad > 0) {
            compras.push({
                titulo: peliculaDiv.querySelector("label").innerText.split(" - ")[0].trim(),
                cantidad: parseInt(cantidad),
            });
        }
    });

    if (compras.length === 0) {
        document.getElementById("mensaje").innerText = "Selecciona al menos una película.";
        return;
    }

    const data = {
        nombreCliente: nombre,
        boletos: compras,
    };

    for (const compra of compras) {
        const response = await fetch(`${API_URL}/inventario/comprar/${encodeURIComponent(compra.titulo)}/${compra.cantidad}`, {
            method: "PUT",
        });
    
        if (!response.ok) {
            document.getElementById("mensaje").innerText = "Error al procesar la compra para " + compra.titulo;
            return;
        }
    }

    // Mensaje de confirmación después de realizar la compra
    document.getElementById("mensaje").innerText = `Compra exitosa para las películas: ${compras.map(compra => compra.titulo).join(", ")}.`;

    // Refrescar disponibilidad
    obtenerPeliculas();
}

// Inicializar cartelera
obtenerPeliculas();
