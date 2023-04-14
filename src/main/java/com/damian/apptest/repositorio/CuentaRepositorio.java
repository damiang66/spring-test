package com.damian.apptest.repositorio;

import com.damian.apptest.entidad.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CuentaRepositorio extends JpaRepository<Cuenta, Long> {
  //  List<Cuenta>findAll();
   // Cuenta findById(Long id);
   // void save(Cuenta cuenta);
    Optional<Cuenta> findByPersona(String persona);
}
