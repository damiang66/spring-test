package com.damian.apptest;

import com.damian.apptest.entidad.Cuenta;
import com.damian.apptest.repositorio.CuentaRepositorio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@DataJpaTest
public class IntegracionJpaTest {
    @Autowired
    CuentaRepositorio cuentaRepositorio;

    @Test
    void testFindById() {
        Optional<Cuenta> cuenta = cuentaRepositorio.findById(1L);
        assertTrue(cuenta.isPresent());
        assertEquals("damian", cuenta.orElseThrow().getPersona());

    }

    @Test
    void testFindByPersona() {
        Optional<Cuenta> cuenta = cuentaRepositorio.findByPersona("damian");
        assertTrue(cuenta.isPresent());
        assertEquals("damian", cuenta.orElseThrow().getPersona());
        assertEquals("1000.00", cuenta.orElseThrow().getSaldo().toPlainString());

    }

    @Test
    void testFindByPersonaException() {
        Optional<Cuenta> cuenta = cuentaRepositorio.findByPersona("p");
        assertThrows(NoSuchElementException.class, () -> {
            cuenta.orElseThrow();
        });
        assertFalse(cuenta.isPresent());

    }

    @Test
    void testFindAll() {
        List<Cuenta>cuentas = cuentaRepositorio.findAll();
        assertFalse(cuentas.isEmpty());
        assertEquals(2,cuentas.size());

    }

    @Test
    void testSave() {
        //given
        Cuenta cuentaPepe = new Cuenta(null,"pepe", new BigDecimal("1000.50"));

        //when
        Cuenta cuenta =cuentaRepositorio.save(cuentaPepe);
        //Cuenta cuenta =cuentaRepositorio.findByPersona("pepe").orElseThrow();
        //then
        assertEquals("pepe", cuenta.getPersona());
        assertEquals("1000.50", cuenta.getSaldo().toPlainString());
    }
    @Test
    void testEditar() {
        //given
        Cuenta cuentaPepe = new Cuenta(null,"pepe", new BigDecimal("1000.50"));

        //when
        Cuenta cuenta =cuentaRepositorio.save(cuentaPepe);
        //Cuenta cuenta =cuentaRepositorio.findByPersona("pepe").orElseThrow();
        //then
        assertEquals("pepe", cuenta.getPersona());
        assertEquals("1000.50", cuenta.getSaldo().toPlainString());
        //when
        cuenta.setSaldo(new BigDecimal("2000"));
        Cuenta editada=cuentaRepositorio.save(cuenta);
        //then
        assertEquals("2000" ,editada.getSaldo().toPlainString());
        assertEquals("pepe", editada.getPersona());
    }

    @Test
    void testEliminar() {
        //given
        Cuenta cuenta = cuentaRepositorio.findById(1L).orElseThrow();
        assertEquals("damian",cuenta.getPersona());
        cuentaRepositorio.delete(cuenta);

        assertThrows(NoSuchElementException.class,()->{
            cuentaRepositorio.findByPersona("damian").orElseThrow();
            cuentaRepositorio.findById(1L).orElseThrow();
        });
        assertEquals(1,cuentaRepositorio.findAll().size());



    }
}
