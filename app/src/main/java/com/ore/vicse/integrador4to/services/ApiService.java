package com.ore.vicse.integrador4to.services;

import com.ore.vicse.integrador4to.models.Almacen;
import com.ore.vicse.integrador4to.models.Producto;
import com.ore.vicse.integrador4to.models.Proveedor;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {

    String API_BASE_URL = "http://integrador4tociclo-vicse.c9users.io";

    @GET("proveedores/{id}/productos")
    Call<List<Producto>> getProductosPro(@Path("id") Integer id);


    @FormUrlEncoded
    @POST("/api/proveedores/")
    Call<Proveedor> createProveedor(@Field("empresa") String empresa,
                                    @Field("ruc") String ruc,
                                    @Field("correo") String correo,
                                    @Field("password") String password);

    @Multipart
    @POST("/api/productos/")
    Call<Producto> createProductoWithImage(
            @Part("nombre") RequestBody nombre,
            @Part("precio") RequestBody precio,
            @Part("detalles") RequestBody detalles,
            @Part("id_proveedor") RequestBody id_proveedor,
            @Part("id_almacen") RequestBody id_almacen,
            @Part MultipartBody.Part imagen
    );

    @GET("/api/almacenes/")
    Call<List<Almacen>> getAlmacenes();

    @FormUrlEncoded
    @POST("/proveedores/login/")
    Call<Proveedor> login(@Field("ruc") String ruc,
                          @Field("password") String password);

}
