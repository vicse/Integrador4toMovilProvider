package com.ore.vicse.integrador4to.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ore.vicse.integrador4to.R;
import com.ore.vicse.integrador4to.models.Proveedor;
import com.ore.vicse.integrador4to.services.ApiService;
import com.ore.vicse.integrador4to.services.ApiServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterProviderActivity extends AppCompatActivity {

    private static final String TAG = RegisterProviderActivity.class.getSimpleName();

    private EditText empresaInput,rucInput,emailInput,passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_provider);

        empresaInput = (EditText) findViewById(R.id.company_input);
        rucInput = (EditText) findViewById(R.id.ruc_input);
        emailInput = (EditText) findViewById(R.id.email_input);
        passwordInput = (EditText) findViewById(R.id.password_input);


    }

    public void showLogin(View view){
        Intent intent = new Intent(RegisterProviderActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void callRegister(View view){

        final String empresa = empresaInput.getText().toString();
        final String ruc = rucInput.getText().toString();
        final String email = emailInput.getText().toString();
        final String password = passwordInput.getText().toString();

        if(empresa.isEmpty() || ruc.isEmpty() || email.isEmpty() || password.isEmpty()){
            Toast.makeText(this,"Todos los campos son requeridos", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService service = ApiServiceGenerator.createService(ApiService.class);

        Call<Proveedor> call = null;
        call = service.createProveedor(empresa,ruc,email,password);
        call.enqueue(new Callback<Proveedor>() {
            @Override
            public void onResponse(Call<Proveedor> call, Response<Proveedor> response) {
                try {

                    int statusCode = response.code();
                    Log.d(TAG, "HTTP status code: " + statusCode);

                    if (response.isSuccessful()) {

                        Proveedor proveedor = response.body();
                        Log.d(TAG, "proveedor: " + proveedor);

                        Toast.makeText(RegisterProviderActivity.this, "Registro satisfactorio", Toast.LENGTH_LONG).show();

                        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);

                        SharedPreferences.Editor editor= preferences.edit();
                        editor.putInt("id", proveedor.getId_proveedor());
                        editor.putString("empresa", empresa);
                        editor.putString("ruc", ruc);
                        editor.putString("email", email);
                        editor.putString("password", password);
                        editor.commit();

                        startActivity(new Intent(RegisterProviderActivity.this, HomeActivity.class));
                        finish();

                    } else {
                        Log.e(TAG, "onError: " + response.errorBody().string());
                        throw new Exception("Error en el servicio");
                    }

                } catch (Throwable t) {
                    try {
                        Log.e(TAG, "onThrowable: " + t.toString(), t);
                        Toast.makeText(RegisterProviderActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    } catch (Throwable x) {
                    }
                }

            }

            @Override
            public void onFailure(Call<Proveedor> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.toString());
                Toast.makeText(RegisterProviderActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

    }
}
