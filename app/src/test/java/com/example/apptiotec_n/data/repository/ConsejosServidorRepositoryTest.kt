package com.example.apptiotec_n.data.repository

import com.example.apptiotec_n.data.model.ConsejoServidor
import com.example.apptiotec_n.data.remote.ConsejoApiService
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk

class ConsejosServidorRepositoryTest : StringSpec({

    val consejo = ConsejoServidor(1, "Respaldo", "Haz copias periódicas")

    "listar devuelve los consejos del microservicio" {
        val api = mockk<ConsejoApiService>()
        coEvery { api.listar() } returns listOf(consejo)

        ConsejosServidorRepository(api).listar() shouldBe listOf(consejo)
        coVerify { api.listar() }
    }

    "crear envía el consejo y devuelve el creado con su id" {
        val api = mockk<ConsejoApiService>()
        val creado = consejo.copy(id = 10)
        coEvery { api.crear(consejo) } returns creado

        ConsejosServidorRepository(api).crear(consejo) shouldBe creado
        coVerify { api.crear(consejo) }
    }

    "actualizar envía el id en la ruta y el cuerpo con los cambios" {
        val api = mockk<ConsejoApiService>()
        val editado = consejo.copy(titulo = "Respaldo semanal")
        coEvery { api.actualizar(1L, editado) } returns editado

        ConsejosServidorRepository(api).actualizar(editado) shouldBe editado
        coVerify { api.actualizar(1L, editado) }
    }

    "eliminar pide borrar el consejo por su id" {
        val api = mockk<ConsejoApiService>()
        coEvery { api.eliminar(1L) } just Runs

        ConsejosServidorRepository(api).eliminar(1L)
        coVerify { api.eliminar(1L) }
    }
})
