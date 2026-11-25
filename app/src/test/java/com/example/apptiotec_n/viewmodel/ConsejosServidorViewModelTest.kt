package com.example.apptiotec_n.viewmodel

import com.example.apptiotec_n.data.model.ConsejoServidor
import com.example.apptiotec_n.data.repository.ConsejosServidorRepository
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import java.io.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class ConsejosServidorViewModelTest : StringSpec({

    beforeTest {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    afterTest {
        Dispatchers.resetMain()
    }

    val consejos = listOf(
        ConsejoServidor(1, "Respaldo", "Haz copias periódicas"),
        ConsejoServidor(2, "Contraseñas", "Usa claves únicas")
    )

    "al iniciar carga los consejos desde el microservicio" {
        val repositorio = mockk<ConsejosServidorRepository>()
        coEvery { repositorio.listar() } returns consejos

        val viewModel = ConsejosServidorViewModel(repositorio)

        viewModel.uiState.value.cargando shouldBe false
        viewModel.uiState.value.consejos shouldBe consejos
        viewModel.uiState.value.error shouldBe null
    }

    "si el microservicio no responde deja la lista vacía y muestra el aviso" {
        val repositorio = mockk<ConsejosServidorRepository>()
        coEvery { repositorio.listar() } throws IOException("sin conexión")

        val viewModel = ConsejosServidorViewModel(repositorio)

        viewModel.uiState.value.consejos.shouldBeEmpty()
        viewModel.uiState.value.error shouldBe
            "No se pudo conectar con el microservicio. Revisa que esté en ejecución y que la URL sea accesible desde el teléfono."
    }

    "guardar crea el consejo cuando no tiene id y refresca la lista" {
        val repositorio = mockk<ConsejosServidorRepository>()
        val nuevo = ConsejoServidor(0, "Wi-Fi", "Usa una red de confianza")
        coEvery { repositorio.listar() } returnsMany listOf(emptyList(), consejos)
        coEvery { repositorio.crear(nuevo) } returns nuevo.copy(id = 3)

        val viewModel = ConsejosServidorViewModel(repositorio)
        viewModel.guardar(nuevo)

        coVerify { repositorio.crear(nuevo) }
        viewModel.uiState.value.consejos shouldBe consejos
        viewModel.uiState.value.guardando shouldBe false
        viewModel.uiState.value.error shouldBe null
    }

    "guardar actualiza el consejo cuando ya tiene id" {
        val repositorio = mockk<ConsejosServidorRepository>()
        val editado = consejos[0].copy(titulo = "Respaldo semanal")
        coEvery { repositorio.listar() } returns consejos
        coEvery { repositorio.actualizar(editado) } returns editado

        val viewModel = ConsejosServidorViewModel(repositorio)
        viewModel.guardar(editado)

        coVerify { repositorio.actualizar(editado) }
        coVerify(exactly = 0) { repositorio.crear(any()) }
    }

    "eliminar pide borrar al microservicio y refresca la lista" {
        val repositorio = mockk<ConsejosServidorRepository>()
        coEvery { repositorio.listar() } returnsMany
            listOf(consejos, listOf(consejos[1]))
        coEvery { repositorio.eliminar(1L) } just Runs

        val viewModel = ConsejosServidorViewModel(repositorio)
        viewModel.eliminar(1L)

        coVerify { repositorio.eliminar(1L) }
        viewModel.uiState.value.consejos shouldBe listOf(consejos[1])
        viewModel.uiState.value.error shouldBe null
    }

    "si falla el guardado conserva la lista y muestra el aviso" {
        val repositorio = mockk<ConsejosServidorRepository>()
        val nuevo = ConsejoServidor(0, "Wi-Fi", "Usa una red de confianza")
        coEvery { repositorio.listar() } returns consejos
        coEvery { repositorio.crear(nuevo) } throws IOException("sin red")

        val viewModel = ConsejosServidorViewModel(repositorio)
        viewModel.guardar(nuevo)

        viewModel.uiState.value.consejos shouldBe consejos
        viewModel.uiState.value.guardando shouldBe false
        viewModel.uiState.value.error shouldBe
            "No se pudo guardar el consejo. Intenta de nuevo."
    }
})
