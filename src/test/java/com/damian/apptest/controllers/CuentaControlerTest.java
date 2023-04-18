package com.damian.apptest.controllers;

import com.damian.apptest.datos.Datos;
import com.damian.apptest.entidad.Cuenta;
import com.damian.apptest.entidad.TransaccionDto;
import com.damian.apptest.service.CuentaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(CuentaControler.class)
class CuentaControlerTest {
   // http://localhost:8080/swagger-ui/index.htm ruta de swagger
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CuentaService service;
    ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        mapper= new ObjectMapper();
    }

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
    void transferir() throws Exception {
        //given
        TransaccionDto dto = new TransaccionDto();
        dto.setCuentaOrigenId(1L);
        dto.setCuentaDestinoId(2L);
        dto.setMonto(new BigDecimal("100"));
        dto.setBancoId(1L);
        System.out.println(mapper.writeValueAsString(dto));
        Map<String, Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("status","ok");
        response.put("mensaje","transferencia realizada con exito");
        response.put("transaccion", dto);
        System.out.println(mapper.writeValueAsString(response));
        //when
        mockMvc.perform(post("/api/cuentas/transferir").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))

        //then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.date").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.mensaje").value("transferencia realizada con exito"))
                .andExpect(jsonPath("$.transaccion.cuentaOrigenId").value(1L))
                .andExpect(content().json(mapper.writeValueAsString(response)));


    }

    @Test
    void testListar() throws Exception {
        //given
        List<Cuenta>cuentas = Arrays.asList(Datos.crearCuenta001().orElseThrow(),Datos.crearCuenta002().orElseThrow());
        when(service.findAll()).thenReturn(cuentas);
        // When
        mockMvc.perform(get("/api/cuentas").contentType(MediaType.APPLICATION_JSON))

        //then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].persona").value("Damian"))
                .andExpect(jsonPath("$[1].persona").value("Camila"))
                .andExpect(jsonPath("$",hasSize(2)))
                .andExpect(content().json(mapper.writeValueAsString(cuentas)));



    }

    @Test
    void testGuardar() throws Exception {
        //given
        Cuenta cuenta = new Cuenta(null,"pepe",new BigDecimal("1000"));
        when(service.save(any())).then(invocation -> {
         Cuenta c = invocation.getArgument(0);
         c.setId(3L);
         return c;
        });
        // when
        mockMvc.perform(post("/api/cuentas").contentType(MediaType.APPLICATION_JSON)
                //then
                .content(mapper.writeValueAsString(cuenta)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id",is(3)))
                .andExpect(jsonPath("$.persona",is("pepe")))
                .andExpect(jsonPath("$.saldo",is(1000)));
        verify(service).save(any());

    }
}