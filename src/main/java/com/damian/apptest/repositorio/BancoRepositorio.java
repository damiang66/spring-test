package com.damian.apptest.repositorio;

import com.damian.apptest.entidad.Banco;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface BancoRepositorio extends JpaRepository<Banco,Long> {
    //List<Banco> findAll();
    //Banco findById(Long id);
   // void save(Banco banco);
}
