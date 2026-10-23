package com.example.apptiotec_n.model

data class Pregunta(
    val id: Int,
    val categoria: String,
    val texto: String,
    val recomendacion: String
) {
    companion object {

        val categorias = listOf(
            "Actualizaciones",
            "Contraseñas",
            "Respaldos",
            "Hábitos"
        )

        val lista = listOf(
            Pregunta(
                id = 1,
                categoria = "Actualizaciones",
                texto = "¿Mantienes actualizado el sistema y las aplicaciones?",
                recomendacion = "Activa las actualizaciones automáticas del sistema"
            ),
            Pregunta(
                id = 2,
                categoria = "Actualizaciones",
                texto = "¿Instalas aplicaciones solo desde la tienda oficial?",
                recomendacion = "Instala aplicaciones solo desde la tienda oficial"
            ),
            Pregunta(
                id = 3,
                categoria = "Contraseñas",
                texto = "¿Usas una contraseña distinta en cada cuenta?",
                recomendacion = "Cambia las contraseñas repetidas por una distinta en cada cuenta"
            ),
            Pregunta(
                id = 4,
                categoria = "Contraseñas",
                texto = "¿Tienes activada la verificación en dos pasos?",
                recomendacion = "Activa la verificación en dos pasos en tu correo principal"
            ),
            Pregunta(
                id = 5,
                categoria = "Contraseñas",
                texto = "¿Guardas las contraseñas en un gestor y no en papel?",
                recomendacion = "Guarda las contraseñas en un gestor en vez de anotarlas"
            ),
            Pregunta(
                id = 6,
                categoria = "Respaldos",
                texto = "¿Tienes una copia de seguridad de tus archivos importantes?",
                recomendacion = "Haz una copia de seguridad de tus archivos importantes"
            ),
            Pregunta(
                id = 7,
                categoria = "Respaldos",
                texto = "¿Sabes cómo recuperar una cuenta si olvidas la contraseña?",
                recomendacion = "Revisa los datos de recuperación de tus cuentas"
            ),
            Pregunta(
                id = 8,
                categoria = "Hábitos",
                texto = "¿Revisas los permisos que pide una aplicación antes de instalarla?",
                recomendacion = "Revisa los permisos antes de instalar una aplicación"
            ),
            Pregunta(
                id = 9,
                categoria = "Hábitos",
                texto = "¿Evitas abrir enlaces de remitentes desconocidos?",
                recomendacion = "No abras enlaces de remitentes que no conozcas"
            ),
            Pregunta(
                id = 10,
                categoria = "Hábitos",
                texto = "¿Usas una red Wi-Fi de confianza para hacer trámites con datos?",
                recomendacion = "Evita hacer trámites con datos en redes Wi-Fi públicas"
            )
        )

        val opciones = listOf(
            "Sí" to 10,
            "A veces" to 5,
            "No" to 0
        )
    }
}
