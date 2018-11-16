package com.ore.vicse.integrador4to.services;

import com.ore.vicse.integrador4to.models.Producto;
import com.ore.vicse.integrador4to.models.Proveedor;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {

    String API_BASE_URL = "http://integrador4tociclo-vicse.c9users.io";

    @GET("/api/productos/")
    Call<List<Producto>> getProductos();

    @FormUrlEncoded
    @POST("/api/proveedores/")
    Call<Proveedor> createProveedor(@Field("empresa") String empresa,
                                    @Field("ruc") String ruc,
                                    @Field("correo") String correo,
                                    @Field("password") String password);

}
    