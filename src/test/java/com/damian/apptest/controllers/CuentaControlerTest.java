package com.damian.apptest.controllers;

import com.damian.apptest.datos.Datos;
import com.damian.apptest.service.CuentaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(CuentaControler.class)
class CuentaControlerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CuentaService service;

    @Test
    void detalle() throws Exception {
        //given
        when(service.findById(1L)).thenReturn(Datos.crearCuenta001().orElseThrow());
        //when
        mockMvc.perform(get("/api/cuentas/1").contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.persona").value("Damian"))
                .andExpect(jsonPath("$.saldo").value("1000"));
        verify(service).findById(1L);
    }

    @Test
    void transferir() {
    }
}