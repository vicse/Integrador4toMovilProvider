package com.ore.vicse.integrador4to.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ore.vicse.integrador4to.R;
import com.ore.vicse.integrador4to.activities.RegisterProductoActivity;
import com.ore.vicse.integrador4to.adapters.ProductosAdapter;
import com.ore.vicse.integrador4to.models.Producto;
import com.ore.vicse.integrador4to.services.ApiService;
import com.ore.vicse.integrador4to.services.ApiServiceGenerator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductFragment extends Fragment {

    private static final String TAG = ProductFragment.class.getSimpleName();
    private RecyclerView productosList;
    private Integer idProveedor;

    public ProductFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idProveedor = getArguments().getInt("idPro");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=  inflater.inflate(R.layout.fragment_product, container, false);

        productosList = (RecyclerView) view.findViewById(R.id.recyclerview);
        productosList.setLayoutManager(new LinearLayoutManager(getContext()));

        productosList.setAdapter(new ProductosAdapter());
        initialize();

        FloatingActionButton btnShowRegisterPro = (FloatingActionButton) view.findViewById(R.id.btnShowRegisterProducto);
        btnShowRegisterPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RegisterProductoActivity.class);
                intent.putExtra("idProvider", idProveedor);
//                Toast.makeText(getContext(), "IdProveedor"+idProveedor, Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
        return view;
    }

    private void initialize(){

        ApiService service = ApiServiceGenerator.createService(ApiService.class);

        Call<List<Producto>> call = service.getProductosPro(idProveedor);

        call.enqueue(new Callback<List<Producto>>() {
            @Override
            public void onResponse(Call<List<Producto>> call, Response<List<Producto>> response) {
                try {

                    int statusCode = response.code();
                    Log.d(TAG, "HTTP status code: " + statusCode);

                    if (response.isSuccessful()) {

                        List<Producto> productos = response.body();
                        Log.d(TAG, "productos: " + productos);

                        ProductosAdapter adapter = (ProductosAdapter) productosList.getAdapter();
                        adapter.setProductos(productos);
                        adapter.notifyDataSetChanged();

                    } else {
                        Log.e(TAG, "onError: " + response.errorBody().string());
                        throw new Exception("Error en el servicio");
                    }

                } catch (Throwable t) {
                    try {
                        Log.e(TAG, "onThrowable: " + t.toString(), t);
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                    }catch (Throwable x){}
                }
            }

            @Override
            public void onFailure(Call<List<Producto>> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.toString());
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }

        });
    }


}
