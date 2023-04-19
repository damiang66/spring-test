package com.damian.apptest.service;

import com.damian.apptest.entidad.Cuenta;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CuentaService {
    List<Cuenta> findAll();
    Cuenta findById(Long id);
    Cuenta save(Cuenta cuenta);

    int revisarTotalDeTransferencia(Long bancoId);
    BigDecimal revisarSaldo(Long cuentaId);
    void tranferir(Long cuentaOrigen, Long cuentaDestino, BigDecimal monto, Long bancoId);
    void eliminar(Long id);

}
