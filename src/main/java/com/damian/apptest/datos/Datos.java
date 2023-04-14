package com.damian.apptest.datos;

import com.damian.apptest.entidad.Banco;
import com.damian.apptest.entidad.Cuenta;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.util.Optional;

public class Datos {
    public static final Cuenta CUENTA_001= new Cuenta(1L, "Damian", new BigDecimal("1000"));
    public static final Cuenta CUENTA_002= new Cuenta(2L, "Camila", new BigDecimal("2000"));
    public static final Banco BANCO_001 = new Banco(1L,"Banco Rio", 0);
    public static Optional<Cuenta> crearCuenta001(){
        return  Optional.of(new Cuenta(1L, "Damian", new BigDecimal("1000")));
    }
    public static Optional<Cuenta> crearCuenta002(){
        return Optional.of(new Cuenta(2L, "Camila", new BigDecimal("2000")));
    }
    public static Optional<Banco> crearbanco(){
        return  Optional.of(new Banco(1L,"Banco Rio", 0));
    }
}
