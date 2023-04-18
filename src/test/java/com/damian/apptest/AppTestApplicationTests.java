package com.damian.apptest;

import com.damian.apptest.datos.Datos;
import com.damian.apptest.entidad.Banco;
import com.damian.apptest.entidad.Cuenta;
import com.damian.apptest.exception.DineroInsuficiente;
import com.damian.apptest.repositorio.BancoRepositorio;
import com.damian.apptest.repositorio.CuentaRepositorio;
import com.damian.apptest.service.CuentaService;
import com.damian.apptest.service.CuentaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
class AppTestApplicationTests {
	//@Mock
	@MockBean
CuentaRepositorio cuentaRepositorio;
	//@Mock
	@MockBean
BancoRepositorio bancoRepositorio;
	//@InjectMocks
	@Autowired
CuentaServiceImpl service;

	@BeforeEach
	void setUp() {
	//	cuentaRepositorio = mock(CuentaRepositorio.class);
	//	bancoRepositorio = mock(BancoRepositorio.class);
	//	service = new CuentaServiceImpl(cuentaRepositorio,bancoRepositorio);
	//	Datos.CUENTA_001.setSaldo(new BigDecimal("1000"));
//		Datos.CUENTA_002.setSaldo(new BigDecimal("2000"));
//		Datos.BANCO_001.setTotal(0);
	}

	@Test
	void contextLoads() {
		when(cuentaRepositorio.findById(1L)).thenReturn(Datos.crearCuenta001());
		when(cuentaRepositorio.findById(2L)).thenReturn(Datos.crearCuenta002());
		when(bancoRepositorio.findById(1L)).thenReturn(Datos.crearbanco());
		BigDecimal saldoOrigen = service.revisarSaldo(1L);
		BigDecimal saldoDestino = service.revisarSaldo(2L);
		assertEquals("1000", saldoOrigen.toPlainString());
		assertEquals("2000", saldoDestino.toPlainString());
		service.tranferir(1L,2L,new BigDecimal("100"),1L);
		saldoOrigen = service.revisarSaldo(1L);
		saldoDestino = service.revisarSaldo(2L);
		assertEquals("900", saldoOrigen.toPlainString());
		assertEquals("2100", saldoDestino.toPlainString());
		int total = service.revisarTotalDeTransferencia(1L);
		assertEquals(1, total);
		verify(cuentaRepositorio, times(3)).findById(1L);
		verify(cuentaRepositorio,times(3)).findById(2L);
		verify(cuentaRepositorio, times(2)).save(any(Cuenta.class));
		verify(bancoRepositorio, times(2)).findById(1L);
		verify(bancoRepositorio).save(any(Banco.class));
		verify(cuentaRepositorio,times(6)).findById(anyLong());
		verify(cuentaRepositorio, never()).findAll();
	}
	@Test
	void contextLoads2() {
		when(cuentaRepositorio.findById(1L)).thenReturn(Datos.crearCuenta001());
		when(cuentaRepositorio.findById(2L)).thenReturn(Datos.crearCuenta002());
		when(bancoRepositorio.findById(1L)).thenReturn(Datos.crearbanco());
		BigDecimal saldoOrigen = service.revisarSaldo(1L);
		BigDecimal saldoDestino = service.revisarSaldo(2L);
		assertEquals("1000", saldoOrigen.toPlainString());
		assertEquals("2000", saldoDestino.toPlainString());
		assertThrows(DineroInsuficiente.class, ()->{
			service.tranferir(1L,2L,new BigDecimal("1200"),1L);
		});

		saldoOrigen = service.revisarSaldo(1L);
		saldoDestino = service.revisarSaldo(2L);
		assertEquals("1000", saldoOrigen.toPlainString());
		assertEquals("2000", saldoDestino.toPlainString());
		int total = service.revisarTotalDeTransferencia(1L);
		assertEquals(1, total);
		verify(cuentaRepositorio, times(3)).findById(1L);
		verify(cuentaRepositorio,times(2)).findById(2L);
		verify(cuentaRepositorio,never()).save(any(Cuenta.class));
		verify(bancoRepositorio, times(2)).findById(1L);
		verify(bancoRepositorio,never()).save(any(Banco.class));
		verify(cuentaRepositorio, times(5)).findById(anyLong());
		verify(cuentaRepositorio,never()).findAll();
	}

	@Test
	void contextLoad3() {
		when(cuentaRepositorio.findById(1L)).thenReturn(Datos.crearCuenta001());
		Cuenta cuenta1 = service.findById(1L);
		Cuenta cuenta2 = service.findById(1L);
		assertSame(cuenta1, cuenta2);
		assertTrue(cuenta1==cuenta2);
		assertEquals("Damian", cuenta1.getPersona());
		assertEquals("Damian", cuenta2.getPersona());
		verify(cuentaRepositorio,times(2)).findById(anyLong());
	}

	@Test
	void testFindAll() {
		//given
		List<Cuenta> datos = Arrays.asList(Datos.crearCuenta001().orElseThrow(),Datos.crearCuenta002().orElseThrow());
	when(cuentaRepositorio.findAll()).thenReturn(datos);
	//when
		List<Cuenta>cuentas = service.findAll();
	// then
	assertFalse(cuentas.isEmpty());
	assertEquals(2,cuentas.size());
	assertTrue(cuentas.contains(Datos.crearCuenta001().orElseThrow()));
	verify(cuentaRepositorio).findAll();
	}

	@Test
	void testSave() {
		//given
		Cuenta pepe = new Cuenta(null, "pepe", new BigDecimal("2000"));
		when(cuentaRepositorio.save(any())).then(invocation -> {
			Cuenta c = invocation.getArgument(0);
			c.setId(3L);
			return c;
		});
		//when
	Cuenta cuenta = service.save(pepe);
		// then

		assertEquals(3,cuenta.getId());
		assertEquals("pepe",cuenta.getPersona());
		assertEquals("2000",cuenta.getSaldo().toPlainString());

		verify(cuentaRepositorio).save(any());
	}
}
