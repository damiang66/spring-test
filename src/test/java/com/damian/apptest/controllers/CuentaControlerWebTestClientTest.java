package com.damian.apptest.controllers;

import com.damian.apptest.entidad.TransaccionDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CuentaControlerWebTestClientTest {
    @Autowired
    private WebTestClient webTestClient;
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
      mapper = new ObjectMapper();
    }

    @Test
    void testTransferir() throws JsonProcessingException {
        // given
        TransaccionDto dto = new TransaccionDto();
        dto.setCuentaOrigenId(1L);
        dto.setCuentaDestinoId(2L);
        dto.setBancoId(1L);
        dto.setMonto(new BigDecimal("100"));
        Map<String, Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("status","ok");
        response.put("mensaje","transferencia realizada con exito");
        response.put("transaccion", dto);
        // when
        webTestClient.post().uri("/api/cuentas/transferir")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.mensaje").isNotEmpty()
               .jsonPath("$.mensaje").value(is("transferencia realizada con exito"))
                .jsonPath("$.mensaje").value(valor->{
                    assertEquals("transferencia realizada con exito",valor);
                })
                .jsonPath("$.mensaje").isEqualTo("transferencia realizada con exito")
                .jsonPath("$.transaccion.cuentaOrigenId").isEqualTo(dto.getCuentaOrigenId())
                .jsonPath("$.date").isEqualTo( LocalDate.now().toString())
                .json(mapper.writeValueAsString(response));


    }
}