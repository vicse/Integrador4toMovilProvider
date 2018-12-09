package com.ore.vicse.integrador4to.models;

public class Almacen {

    private Integer id_almacen;
    private String longitud;
    private String latitud;
    private String direccion;
    private String nombre;

    public Almacen(Integer id_almacen, String longitud, String latitud, String direccion, String nombre) {
        this.id_almacen = id_almacen;
        this.longitud = longitud;
        this.latitud = latitud;
        this.direccion = direccion;
        this.nombre = nombre;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getId_almacen() {
        return id_almacen;
    }

    public void setId_almacen(int id_almacen) {
        this.id_almacen = id_almacen;
    }

    @Override
    public String toString() {
        return "Almacen{" +
                "id_almacen=" + id_almacen +
                ", longitud='" + longitud + '\'' +
                ", latitud='" + latitud + '\'' +
                ", direccion='" + direccion + '\'' +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
