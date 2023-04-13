package com.damian.apptest.repositorio;

import com.damian.apptest.entidad.Banco;


import java.util.List;

public interface BancoRepositorio {
    List<Banco> findAll();
    Banco findById(Long id);
    void save(Banco banco);
}
