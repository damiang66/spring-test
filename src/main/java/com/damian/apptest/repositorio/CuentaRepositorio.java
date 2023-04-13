package com.damian.apptest.repositorio;

import com.damian.apptest.entidad.Cuenta;

import java.util.List;

public interface CuentaRepositorio {
    List<Cuenta>findAll();
    Cuenta findById(Long id);
    void save(Cuenta cuenta);
}
