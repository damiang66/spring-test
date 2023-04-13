package com.damian.apptest.entidad;



public class Banco {
    private Long id;
    private String nombre;
    private int total;
    public Banco() {

    }

    public Banco(Long id, String nombre, int total) {
        this.id = id;
        this.nombre = nombre;
        this.total = total;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
