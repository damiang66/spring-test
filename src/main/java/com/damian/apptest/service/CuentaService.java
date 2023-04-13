package com.damian.apptest.service;

import com.damian.apptest.entidad.Cuenta;

import java.math.BigDecimal;

public interface CuentaService {
    Cuenta findById(Long id);
    int revisarTotalDeTransferencia(Long bancoId);
    BigDecimal revisarSaldo(Long cuentaId);
    void tranferir(Long cuentaOrigen, Long cuentaDestino, BigDecimal monto, Long bancoId);

}
