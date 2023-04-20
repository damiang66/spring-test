package com.damian.apptest.controllers;

import com.damian.apptest.entidad.Cuenta;
import com.damian.apptest.entidad.TransaccionDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
@Tag("integracion.rt")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CuentaControlerTestRestTemplateTest {
    @Autowired
    private TestRestTemplate client;
    private ObjectMapper mapper;
    @LocalServerPort
    private int puerto;
    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
    }
    private String crearUri(String uri){
        return "http://localhost:"+ puerto+ uri;    }

    @Test
    @Order(1)
    void testTransferir() throws JsonProcessingException {
        //given datos de ejemplo
        TransaccionDto dto = new TransaccionDto();
        dto.setMonto(new BigDecimal("100"));
        dto.setCuentaOrigenId(1L);
        dto.setCuentaDestinoId(2L);
        dto.setBancoId(1L);
//when para
        ResponseEntity<String> respuesta = client.
                postForEntity(crearUri("/api/cuentas/transferir"), dto, String.class);
        System.out.println(puerto);
        String json = respuesta.getBody();
        System.out.println(json);
        //then probar
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, respuesta.getHeaders().getContentType());
        assert json != null;
        assertTrue(json.contains("transferencia realizada con exito"));
        JsonNode jsonNode = mapper.readTree(json);
        assertEquals("transferencia realizada con exito",jsonNode.path("mensaje").asText());
        assertEquals(LocalDate.now().toString(),jsonNode.path("date").asText());
        assertEquals("100",jsonNode.path("transaccion").path("monto").asText());
        assertEquals(1,jsonNode.path("transaccion").path("cuentaOrigenId").asInt());
        Map<String, Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("status","ok");
        response.put("mensaje","transferencia realizada con exito");
        response.put("transaccion", dto);
        assertEquals(mapper.writeValueAsString(response),json);

    }
@Order(2)
    @Test
    void testDetalle() {
        ResponseEntity<Cuenta> respuesta = client.getForEntity(crearUri("/api/cuentas/1"), Cuenta.class);
       Cuenta cuenta = respuesta.getBody();
       assertEquals(HttpStatus.OK,respuesta.getStatusCode());
       assertEquals(MediaType.APPLICATION_JSON, respuesta.getHeaders().getContentType());
       assertNotNull(cuenta);
       assertEquals("damian", cuenta.getPersona());
       assertEquals(1L, cuenta.getId());
       assertEquals("900.00", cuenta.getSaldo().toPlainString());
       assertEquals(new Cuenta(1L, "damian", new BigDecimal("900.00")),cuenta);

    }

    @Test
    @Order(3)
    void testListar() throws JsonProcessingException {
        ResponseEntity<Cuenta[]> respuesta = client.getForEntity(crearUri("/api/cuentas")
                , Cuenta[].class);
        List<Cuenta> cuentas = Arrays.asList(respuesta.getBody());
        assertEquals(2, cuentas.size());
        assertEquals(HttpStatus.OK,respuesta.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, respuesta.getHeaders().getContentType());
        assertEquals("damian",cuentas.get(0).getPersona());
        assertEquals(1L,cuentas.get(0).getId());
        assertEquals("900.00",cuentas.get(0).getSaldo().toPlainString());
        assertEquals("camila",cuentas.get(1).getPersona());
        assertEquals(2L,cuentas.get(1).getId());
        assertEquals("2100.00",cuentas.get(1).getSaldo().toPlainString());

        // otra forma
        JsonNode json = mapper.readTree(mapper.writeValueAsString(cuentas));
        assertEquals(1L, json.get(0).path("id").asLong());
        assertEquals("damian", json.get(0).path("persona").asText());
        assertEquals("900.0", json.get(0).path("saldo").asText());
        assertEquals(2L, json.get(1).path("id").asLong());
        assertEquals("camila", json.get(1).path("persona").asText());
        assertEquals("2100.0", json.get(1).path("saldo").asText());
    }

    @Test
    @Order(4)
    void testGuardar() {
        Cuenta cuenta = new Cuenta(null,"pina",new BigDecimal("3000"));
        ResponseEntity<Cuenta> respuesta = client.postForEntity(crearUri("/api/cuentas"), cuenta, Cuenta.class);
        assertEquals(HttpStatus.CREATED,respuesta.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, respuesta.getHeaders().getContentType());
        Cuenta cuentaCreada = respuesta.getBody();
        assertNotNull(cuentaCreada);
        assertEquals("pina",cuentaCreada.getPersona());
        assertEquals(3L,cuentaCreada.getId());
        assertEquals("3000",cuentaCreada.getSaldo().toPlainString());

    }

    @Test
    @Order(5)
    void testEliminar() {
        ResponseEntity<Cuenta[]> respuesta = client.getForEntity(crearUri("/api/cuentas")
                , Cuenta[].class);
        List<Cuenta> cuentas = Arrays.asList(respuesta.getBody());
        assertEquals(3, cuentas.size());
       // client.delete(crearUri("/api/cuentas/3"));
        ResponseEntity<Void> exchange = client.exchange(crearUri("/api/cuentas/3"), HttpMethod.DELETE, null, Void.class);
        assertEquals(HttpStatus.NO_CONTENT,exchange.getStatusCode());
        assertFalse(exchange.hasBody());
        respuesta = client.getForEntity(crearUri("/api/cuentas")
                , Cuenta[].class);
        cuentas= Arrays.asList(respuesta.getBody());
        assertEquals(2, cuentas.size());
        ResponseEntity<Cuenta> respuestas = client.getForEntity(crearUri("/api/cuentas/3"), Cuenta.class);
        Cuenta cuenta = respuestas.getBody();
        assertEquals(HttpStatus.NOT_FOUND,respuestas.getStatusCode());
        assertFalse(respuestas.hasBody());
    }

    @Test
    @Order(6)
    void testEliminar3() {
    }
}