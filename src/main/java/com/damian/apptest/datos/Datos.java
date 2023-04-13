package com.damian.apptest.datos;

import com.damian.apptest.entidad.Banco;
import com.damian.apptest.entidad.Cuenta;

import java.math.BigDecimal;

public class Datos {
    public static final Cuenta CUENTA_001= new Cuenta(1L, "Damian", new BigDecimal("1000"));
    public static final Cuenta CUENTA_002= new Cuenta(2L, "Camila", new BigDecimal("2000"));
    public static final Banco BANCO_001 = new Banco(1L,"Banco Rio", 0);
    public static Cuenta crearCuenta001(){
        return new Cuenta(1L, "Damian", new BigDecimal("1000"));
    }
    public static Cuenta crearCuenta002(){
        return new Cuenta(2L, "Camila", new BigDecimal("2000"));
    }
    public static Banco crearbanco(){
        return new Banco(1L,"Banco Rio", 0);
    }
}
