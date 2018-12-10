package com.ore.vicse.integrador4to.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.EditText;

import android.widget.Toast;

import com.ore.vicse.integrador4to.R;
import com.ore.vicse.integrador4to.models.Proveedor;
import com.ore.vicse.integrador4to.services.ApiService;
import com.ore.vicse.integrador4to.services.ApiServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private SharedPreferences mPrefs;
    private EditText rucInput;
    private EditText passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rucInput = (EditText) findViewById(R.id.editText3);
        passwordInput = (EditText) findViewById(R.id.editText2);


    }
    public void Login(View view){

        String ruc = rucInput.getText().toString();
        String password = passwordInput.getText().toString();

        if(ruc.isEmpty() || password.isEmpty()){
            Toast.makeText(MainActivity.this, "Todos los campos son requeridos" , Toast.LENGTH_SHORT).show();
            return;
        }

        final ApiService service = ApiServiceGenerator.createService(ApiService.class);

        Call<Proveedor> call = null;

        call = service.login(ruc, password);
        call.enqueue(new Callback<Proveedor>() {
            @Override
            public void onResponse(Call<Proveedor> call, Response<Proveedor> response) {
                try{
                    int statusCode = response.code();
                    Log.d(TAG, "HTPP status code:" + statusCode);

                    if(response.isSuccessful()){

                        Proveedor proveedor = response.body();
                        Log.d(TAG, "proveedor:" +proveedor);
                        Toast.makeText(MainActivity.this, "Bienvenido Provedor : "+proveedor.getCorreo(), Toast.LENGTH_SHORT).show();
                        mPrefs = getSharedPreferences("credenciales", Context.MODE_PRIVATE);

                        SharedPreferences.Editor editor = mPrefs.edit();
                        editor.putInt("id", proveedor.getId_proveedor());
                        editor.putString("empresa", proveedor.getEmpresa());
                        editor.putString("ruc", proveedor.getRuc());
                        editor.putString("correo", proveedor.getCorreo());
                        editor.putString("imagen", proveedor.getImagen());
                        editor.putString("password", proveedor.getPassword());
                        editor.apply();
                        startActivity(new Intent(MainActivity.this, HomeActivity.class));
                        finish();

                    }else{
                        Log.e(TAG, "onError:"+ response.errorBody().string());
                        throw new Exception("No registrado en la app");
                    }
                }catch (Throwable t){
                    try{
                        Log.e(TAG, "onThrowable:"+ t.toString(), t);
                        Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }catch (Throwable x){ }
                }
            }

            @Override
            public void onFailure(Call<Proveedor> call, Throwable t) {
                Log.e(TAG, "onFailure:" + t.toString());
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void showRegisterProvider(View view){
        startActivity(new Intent(MainActivity.this, RegisterProviderActivity.class));
    }

}
