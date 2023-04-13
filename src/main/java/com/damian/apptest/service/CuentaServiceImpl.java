package com.damian.apptest.service;

import com.damian.apptest.entidad.Banco;
import com.damian.apptest.entidad.Cuenta;
import com.damian.apptest.repositorio.BancoRepositorio;
import com.damian.apptest.repositorio.CuentaRepositorio;

import java.math.BigDecimal;

public class CuentaServiceImpl implements CuentaService{
    private CuentaRepositorio cuentaRepositorio;
    private BancoRepositorio bancoRepositorio;

    public CuentaServiceImpl(CuentaRepositorio cuentaRepositorio, BancoRepositorio bancoRepositorio) {
        this.cuentaRepositorio = cuentaRepositorio;
        this.bancoRepositorio = bancoRepositorio;
    }

    @Override
    public Cuenta findById(Long id) {
        return cuentaRepositorio.findById(id);
    }

    @Override
    public int revisarTotalDeTransferencia(Long bancoId) {
        Banco banco = bancoRepositorio.findById(bancoId);
        return banco.getTotal();
    }

    @Override
    public BigDecimal revisarSaldo(Long cuentaId) {
        Cuenta cuenta = cuentaRepositorio.findById(cuentaId);
        return cuenta.getSaldo();
    }

    @Override
    public void tranferir(Long cuentaOrigen, Long cuentaDestino, BigDecimal monto, Long bancoId) {
        Banco banco = bancoRepositorio.findById(bancoId);
        int totalTransferencia = banco.getTotal();
        banco.setTotal(++totalTransferencia);
        bancoRepositorio.save(banco);
        Cuenta origen = cuentaRepositorio.findById(cuentaOrigen);
        origen.debito(monto);
        cuentaRepositorio.save(origen);
        Cuenta destino = cuentaRepositorio.findById(cuentaDestino);
        destino.credito(monto);
        cuentaRepositorio.save(destino);


    }
}
