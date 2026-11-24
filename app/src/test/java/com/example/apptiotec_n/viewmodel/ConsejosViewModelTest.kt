package com.example.apptiotec_n.viewmodel

import android.app.Application
import com.example.apptiotec_n.data.model.Consejo
import com.example.apptiotec_n.data.repository.ConsejosRepository
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import java.io.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class ConsejosViewModelTest : StringSpec({

    beforeTest {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    afterTest {
        Dispatchers.resetMain()
    }

    fun crearViewModel(
        repositorio: ConsejosRepository,
        consejos: MutableStateFlow<List<Consejo>> = MutableStateFlow(emptyList()),
        actualizadoEn: MutableStateFlow<Long?> = MutableStateFlow(100L)
    ): ConsejosViewModel {
        every { repositorio.observarConsejos() } returns consejos
        every { repositorio.observarUltimaActualizacion() } returns actualizadoEn
        return ConsejosViewModel(mockk<Application>(relaxed = true), repositorio)
    }

    "al iniciar muestra la copia local y sincroniza con la API" {
        val repositorio = mockk<ConsejosRepository>()
        val consejos = MutableStateFlow(
            listOf(Consejo(1, "Respaldo", "Haz copias periódicas"))
        )
        coEvery { repositorio.sincronizar() } returns 1

        val viewModel = crearViewModel(repositorio, consejos)

        viewModel.uiState.value.cargando shouldBe false
        viewModel.uiState.value.consejos shouldBe consejos.value
        viewModel.uiState.value.actualizadoEn shouldBe 100L
        viewModel.uiState.value.sincronizando shouldBe false
        viewModel.uiState.value.error shouldBe null
    }

    "si la API falla conserva los consejos guardados y muestra el aviso" {
        val repositorio = mockk<ConsejosRepository>()
        val consejos = MutableStateFlow(
            listOf(Consejo(1, "Respaldo", "Haz copias periódicas"))
        )
        coEvery { repositorio.sincronizar() } throws IOException("sin red")

        val viewModel = crearViewModel(repositorio, consejos)

        viewModel.uiState.value.consejos shouldBe consejos.value
        viewModel.uiState.value.error shouldBe
            "No se pudieron actualizar los consejos. Revisa tu conexión."
    }

    "si no hay copia local y falla la API deja la lista vacía con error" {
        val repositorio = mockk<ConsejosRepository>()
        coEvery { repositorio.sincronizar() } throws IOException("sin red")

        val viewModel = crearViewModel(repositorio)

        viewModel.uiState.value.consejos.shouldBeEmpty()
        viewModel.uiState.value.error shouldBe
            "No se pudieron actualizar los consejos. Revisa tu conexión."
    }

    "sincronizar se puede reintentar y limpia el error" {
        val repositorio = mockk<ConsejosRepository>()
        val consejos = MutableStateFlow<List<Consejo>>(emptyList())
        coEvery { repositorio.sincronizar() } throws IOException("sin red")
        val viewModel = crearViewModel(repositorio, consejos)

        val nuevos = listOf(Consejo(2, "Contraseñas", "Usa claves únicas"))
        consejos.value = nuevos
        coEvery { repositorio.sincronizar() } returns 1

        viewModel.sincronizar()

        viewModel.uiState.value.consejos shouldBe nuevos
        viewModel.uiState.value.error shouldBe null
    }
})
