package com.damian.apptest.controllers;

import com.damian.apptest.entidad.Cuenta;
import com.damian.apptest.entidad.TransaccionDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import static org.junit.jupiter.api.Assertions.*;
@Tag("integracion.wc")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
    @Order(3)
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
                // then
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(resultado ->{
                    try {
                        JsonNode json = mapper.readTree(resultado.getResponseBody());
                        assertEquals("transferencia realizada con exito",json.path("mensaje").asText());
                        assertEquals(1L,json.path("transaccion").path("cuentaOrigenId").asLong());
                        assertEquals(LocalDate.now().toString(),json.path("date").asText());
                        assertEquals("100",json.path("transaccion").path("monto").asText());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
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

    @Test
    @Order(1)
    void testDetalle() {
        webTestClient.get().uri("/api/cuentas/1").exchange()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
               // .expectBody().consumeWith(respuesta->{

            //    })
                .expectBody()
                .jsonPath("$.persona").isEqualTo("damian")
                .jsonPath("$.saldo").isEqualTo(1000);

    }
    @Order(2)
    @Test
    void testDetalle1() {
        webTestClient.get().uri("/api/cuentas/2").exchange()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                // .expectBody().consumeWith(respuesta->{

                //    })
                .expectBody(Cuenta.class)
                .consumeWith(respuesta->{
                   Cuenta cuenta = respuesta.getResponseBody();
                   assertEquals("camila",cuenta.getPersona());
                   assertEquals("2000.00",cuenta.getSaldo().toPlainString());
                });

    }

    @Test
    @Order(4)
    void testListar() {
        webTestClient.get().uri("/api/cuentas").exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].persona").isEqualTo("damian")
                .jsonPath("$[0].id").isEqualTo(1)
                .jsonPath("$[0].saldo").isEqualTo(900)
                .jsonPath("$[1].persona").isEqualTo("camila")
                .jsonPath("$[1].id").isEqualTo(2)
                .jsonPath("$[1].saldo").isEqualTo(2100)
                .jsonPath("$").isArray()
                .jsonPath("$").value(hasSize(2));
    }
    @Test
    @Order(5)
    void testListar2() {
        webTestClient.get().uri("/api/cuentas").exchange()
                .expectStatus().isOk()
                .expectBodyList(Cuenta.class)
                .consumeWith(respuesta->{
                    List<Cuenta> cuentas = respuesta.getResponseBody();
                    assertNotNull(cuentas);
                    assertEquals(2, cuentas.size());
                    assertEquals("damian",cuentas.get(0).getPersona());
                    assertEquals(1L,cuentas.get(0).getId());
                    assertEquals(900,cuentas.get(0).getSaldo().intValue());
                    assertEquals("camila",cuentas.get(1).getPersona());
                    assertEquals(2L,cuentas.get(1).getId());
                    assertEquals("2100.0",cuentas.get(1).getSaldo().toPlainString());
                })
                .hasSize(2);

    }

    @Test
    @Order(6)
    void testGuardar() {
        //given
        Cuenta cuenta = new Cuenta(null,"josefina",new BigDecimal("3000"));
        //when
        webTestClient.post().uri("/api/cuentas").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(cuenta)
                .exchange()
        //then
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.persona").isEqualTo("josefina")
                .jsonPath("$.saldo").isEqualTo(3000);

    }
    @Test
    @Order(7)
    void testGuardar1() {
        //given
        Cuenta cuenta = new Cuenta(null,"pina",new BigDecimal("3000"));
        //when
        webTestClient.post().uri("/api/cuentas").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(cuenta)
                .exchange()
                //then
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Cuenta.class)
                .consumeWith(respuesta->{
                    Cuenta cuentaRespuesta= respuesta.getResponseBody();
                    assertNotNull(cuentaRespuesta);
                    assertEquals("pina",cuentaRespuesta.getPersona());
                    assertEquals(3000,cuentaRespuesta.getSaldo().intValue());
                });


    }

    @Test
    @Order(8)
    void testEliminar() {
     webTestClient.get().uri("/api/cuentas").exchange().expectBodyList(Cuenta.class)
                     .hasSize(4);
        webTestClient.delete().uri("/api/cuentas/3")
                .exchange()
                .expectStatus().isNoContent()
                .expectBody().isEmpty();
        webTestClient.get().uri("/api/cuentas").exchange().expectBodyList(Cuenta.class)
                .hasSize(3);
        webTestClient.get().uri("/api/cuentas/3").exchange()
              //  .expectStatus().is5xxServerError();
                .expectStatus()
                .isNotFound()
                .expectBody()
                .isEmpty();
    }
}