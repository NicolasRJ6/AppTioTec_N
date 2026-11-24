package com.example.apptiotec_n.data.repository

import com.example.apptiotec_n.data.local.ConsejoDao
import com.example.apptiotec_n.data.local.ConsejoEntity
import com.example.apptiotec_n.data.model.Consejo
import com.example.apptiotec_n.data.remote.ApiService
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import java.io.IOException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf

class ConsejosRepositoryTest : StringSpec({

    fun repositorio(
        api: ApiService = mockk(),
        dao: ConsejoDao = mockk()
    ) = ConsejosRepository(consejoDao = dao, api = api)

    "observarConsejos convierte las entidades guardadas en modelos" {
        val dao = mockk<ConsejoDao>()
        every { dao.observarTodos() } returns flowOf(
            listOf(
                ConsejoEntity(1, "Respaldo", "Haz copias periódicas", 100L),
                ConsejoEntity(2, "Contraseñas", "Usa claves únicas", 100L)
            )
        )

        val resultado = repositorio(dao = dao).observarConsejos().first()

        resultado shouldBe listOf(
            Consejo(1, "Respaldo", "Haz copias periódicas"),
            Consejo(2, "Contraseñas", "Usa claves únicas")
        )
    }

    "observarUltimaActualizacion entrega la fecha guardada" {
        val dao = mockk<ConsejoDao>()
        every { dao.observarUltimaActualizacion() } returns flowOf(123L)

        repositorio(dao = dao).observarUltimaActualizacion().first() shouldBe 123L
    }

    "sincronizar descarga la API y reemplaza la copia local" {
        val api = mockk<ApiService>()
        val dao = mockk<ConsejoDao>()
        coEvery { api.obtenerConsejos() } returns listOf(
            Consejo(1, "Respaldo", "Haz copias periódicas"),
            Consejo(2, "Contraseñas", "Usa claves únicas")
        )
        val enviados = slot<List<ConsejoEntity>>()
        coEvery { dao.reemplazarTodos(capture(enviados)) } just Runs

        val cantidad = repositorio(api = api, dao = dao).sincronizar()

        cantidad shouldBe 2
        enviados.captured shouldHaveSize 2
        enviados.captured.map { it.titulo } shouldBe listOf("Respaldo", "Contraseñas")
        enviados.captured.map { it.actualizadoEn }.distinct() shouldHaveSize 1
    }

    "si la API falla no borra la copia local" {
        val api = mockk<ApiService>()
        val dao = mockk<ConsejoDao>()
        coEvery { api.obtenerConsejos() } throws IOException("sin conexión")

        shouldThrow<IOException> {
            repositorio(api = api, dao = dao).sincronizar()
        }

        coVerify(exactly = 0) { dao.reemplazarTodos(any()) }
    }
})
