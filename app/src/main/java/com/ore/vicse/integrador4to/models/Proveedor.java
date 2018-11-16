package com.ore.vicse.integrador4to.models;

public class Proveedor {

    private Integer id_proveedor;
    private String empresa;
    private String ruc;
    private String correo;
    private String imagen;
    private String password;

    public Integer getId_proveedor() {
        return id_proveedor;
    }

    public void setId_proveedor(Integer id_proveedor) {
        this.id_proveedor = id_proveedor;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Proveedor{" +
                "id_proveedor=" + id_proveedor +
                ", empresa='" + empresa + '\'' +
                ", ruc='" + ruc + '\'' +
                ", correo='" + correo + '\'' +
                ", imagen='" + imagen + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
