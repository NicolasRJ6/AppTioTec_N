package com.tiotec.microservicio.consejo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ConsejoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ConsejoRepository repository;

    @BeforeEach
    void limpiarBase() {
        repository.deleteAll();
    }

    @Test
    void creaUnConsejoYLoDevuelveEnLaLista() throws Exception {
        Consejo consejo = new Consejo("Usa una contrasena larga", "Mezcla letras, numeros y simbolos");

        mockMvc.perform(post("/api/consejos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(consejo)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.titulo").value("Usa una contrasena larga"));

        mockMvc.perform(get("/api/consejos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void rechazaUnConsejoSinTitulo() throws Exception {
        Consejo consejo = new Consejo("", "Descripcion valida");

        mockMvc.perform(post("/api/consejos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(consejo)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void obtieneUnConsejoPorId() throws Exception {
        Consejo guardado = repository.save(new Consejo("Verifica los enlaces", "Desconfia de enlaces acortados"));

        mockMvc.perform(get("/api/consejos/{id}", guardado.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Verifica los enlaces"));
    }

    @Test
    void respondeNotFoundCuandoNoExiste() throws Exception {
        mockMvc.perform(get("/api/consejos/{id}", 999))
                .andExpect(status().isNotFound());
    }

    @Test
    void actualizaUnConsejo() throws Exception {
        Consejo guardado = repository.save(new Consejo("Titulo original", "Descripcion original"));
        Consejo cambios = new Consejo("Titulo actualizado", "Descripcion actualizada");

        mockMvc.perform(put("/api/consejos/{id}", guardado.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cambios)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Titulo actualizado"));

        mockMvc.perform(get("/api/consejos/{id}", guardado.getId()))
                .andExpect(jsonPath("$.descripcion").value("Descripcion actualizada"));
    }

    @Test
    void eliminaUnConsejo() throws Exception {
        Consejo guardado = repository.save(new Consejo("Temporal", "Se va a eliminar"));

        mockMvc.perform(delete("/api/consejos/{id}", guardado.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/consejos/{id}", guardado.getId()))
                .andExpect(status().isNotFound());
    }
}
