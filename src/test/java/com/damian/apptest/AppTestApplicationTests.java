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
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

@SpringBootTest
class AppTestApplicationTests {
CuentaRepositorio cuentaRepositorio;
BancoRepositorio bancoRepositorio;
CuentaService service;

	@BeforeEach
	void setUp() {
		cuentaRepositorio = mock(CuentaRepositorio.class);
		bancoRepositorio = mock(BancoRepositorio.class);
		service = new CuentaServiceImpl(cuentaRepositorio,bancoRepositorio);
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
		verify(bancoRepositorio).save(any(Banco.class));
	}

}
