package com.ore.vicse.integrador4to.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.ore.vicse.integrador4to.R;
import com.ore.vicse.integrador4to.models.Almacen;
import com.ore.vicse.integrador4to.models.Producto;
import com.ore.vicse.integrador4to.services.ApiService;
import com.ore.vicse.integrador4to.services.ApiServiceGenerator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterProductoActivity extends AppCompatActivity {

    private static final String TAG = RegisterProductoActivity.class.getSimpleName();

    private Integer idProvedor;

    private ImageView imagePreview;
    private Spinner spinner;
    private EditText nombreInput;
    private EditText precioInput;
    private EditText detallesInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_producto);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());


        idProvedor = getIntent().getExtras().getInt("idProvider");
        imagePreview = findViewById(R.id.imagen_pro_preview);
        nombreInput = findViewById(R.id.nombre_producto_input);
        precioInput = findViewById(R.id.precio_producto_input);
        detallesInput = findViewById(R.id.detalle_producto_input);
        spinner = findViewById(R.id.spinnerAlmacen);




    }

    // Cámara configuración


    private static final int CAPTURE_IMAGE_REQUEST = 300;

    private Uri mediaFileUri;

    public void takePicture(View view){
        try{

           if(!permissionsGranted()){
               ActivityCompat.requestPermissions(this, PERMISSIONS_LIST, PERMISSIONS_REQUEST);
               return;
           }
           // creando el directorio de imágenes (si no existe)
            File mediaStorageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
           if(!mediaStorageDir.exists()){
               if(!mediaStorageDir.mkdirs()){
                   throw new Exception("Failed to create directory");
               }
           }
           // Definiendo la ruta destino de la captura(Uri)
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
           File mediFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp+ ".jpg");
           mediaFileUri = Uri.fromFile(mediFile);

           //Iniciando a captura
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mediaFileUri);
            startActivityForResult(intent, CAPTURE_IMAGE_REQUEST);

        }catch (Exception e){
            Log.d(TAG, e.toString());
            Toast.makeText(this, "Error en la captura:"+ e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected  void onActivityResult(int requestCode,  int resultCode,Intent data){
        if(requestCode == CAPTURE_IMAGE_REQUEST){
            if(resultCode == RESULT_OK){
                try{
                    Log.d(TAG, "ResultCode:RESULT_OK");
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mediaFileUri);

                    bitmap = scaleBitmapDown(bitmap, 800);

                    imagePreview.setImageBitmap(bitmap);
                }catch (Exception e){
                    Log.d(TAG, e.toString());
                    Toast.makeText(this, "Error al procesar imagen:" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }else if(resultCode == RESULT_CANCELED){
                Log.d(TAG, "ResultCode:RESULT_CANCELED");
            }else{
                Log.d(TAG, "ResultCode:" +resultCode);
            }
        }
    }

    public void callRegister(View view){

        String nombre = nombreInput.getText().toString();
        String precio = precioInput.getText().toString();
        String detalles = detallesInput.getText().toString();
        String idProveedor = idProvedor.toString();


        if(nombre.isEmpty() || precio.isEmpty() || detalles.isEmpty()){
            Toast.makeText(this, "Todos los campos son requeridos", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService service = ApiServiceGenerator.createService(ApiService.class);

        Call<Producto> call = null;

        File file = new File(mediaFileUri.getPath());
        Log.d(TAG, "File:"+ file.getPath() + " -exists:" +file.exists());

        Bitmap bitmap = BitmapFactory.decodeFile(mediaFileUri.getPath());

        bitmap = scaleBitmapDown(bitmap, 800);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), byteArray);
        MultipartBody.Part imagenPart = MultipartBody.Part.createFormData("imagen", file.getName(), requestFile);

        RequestBody nombrePart = RequestBody.create(MultipartBody.FORM, nombre);
        RequestBody precioPart = RequestBody.create(MultipartBody.FORM, precio);
        RequestBody detallesPart = RequestBody.create(MultipartBody.FORM, detalles);
        final RequestBody idProveedorPart = RequestBody.create(MultipartBody.FORM, idProveedor);


        call = service.createProductoWithImage(nombrePart, precioPart, detallesPart, idProveedorPart, idProveedorPart, imagenPart);

        call.enqueue(new Callback<Producto>() {
            @Override
            public void onResponse(Call<Producto> call, Response<Producto> response) {
                try{
                    int statusCode = response.code();
                    Log.d(TAG, "HTPP status code:" +statusCode);

                    if(response.isSuccessful()){
                        Producto producto = response.body();
                        Log.d(TAG, "producto:" +producto);

                        Toast.makeText(RegisterProductoActivity.this, "Registro Satisfactorio ", Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        Log.d(TAG, "onError:" + response.errorBody().string());
                        throw new Exception("Error en el servicio"+idProveedorPart);
                    }

                }catch (Throwable t){
                    try {
                        Log.e(TAG, "onThrowable:" +t.toString(), t);
                        Toast.makeText(RegisterProductoActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();

                    }catch (Throwable x){
                    }
                }
            }

            @Override
            public void onFailure(Call<Producto> call, Throwable t) {
                Log.e(TAG, "onFailure:"+ t.toString());
                Toast.makeText(RegisterProductoActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    // Permissions handler

    private static final int PERMISSIONS_REQUEST = 200;

    private static String[] PERMISSIONS_LIST = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private boolean permissionsGranted(){
        for (String permission: PERMISSIONS_LIST){
            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
                return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissinons[], int[] grantResults){
        switch (requestCode){
            case PERMISSIONS_REQUEST: {
                for (int i = 0; i< grantResults.length; i++){
                    Log.d(TAG, ""+ grantResults[i]);
                    if(grantResults[i] !=PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this, PERMISSIONS_LIST[i]+ "persmiso rechazado!", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                Toast.makeText(this, "Permisos concedidos, intente nuevamente." , Toast.LENGTH_LONG).show();
            }
        }
    }

    //Redimensionar una imagen bitmap
    private Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension){

        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;

        if(originalHeight > originalWidth){
            resizedHeight= maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        }else if (originalWidth > originalHeight){
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if(originalHeight == originalWidth){
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }

        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }

    private void initialize(){

        final ApiService service = ApiServiceGenerator.createService(ApiService.class);

        Call<List<Almacen>> call = service.getAlmacenes();

        call.enqueue(new Callback<List<Almacen>>() {
            @Override
            public void onResponse(Call<List<Almacen>> call, Response<List<Almacen>> response) {
                try {
                    int statusCode = response.code();
                    Log.d(TAG, "HTPP status code: "+ statusCode);

                    if(response.isSuccessful()){

                        List<Almacen> almacenes = response.body();
                        Log.d(TAG, "almacenes: " +almacenes);

                        almacenes.add(0, new Almacen(0,"sad","asd","asd","Todos los almacenes"));

                        ArrayAdapter<Almacen> almacenAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, almacenes);
                        spinner.setAdapter(almacenAdapter);
                        /*spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                almacenId = item.getId();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });*/

                    }else{
                        Log.e(TAG, "onError:" + response.errorBody().string());
                        throw new Exception("Error en el servicio");
                    }

                }catch (Throwable t){
                    try{
                        Log.e(TAG, "onThrowable: " + t.toString(), t);
                        Toast.makeText(RegisterProductoActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }catch (Throwable x){}
                }
            }

            @Override
            public void onFailure(Call<List<Almacen>> call, Throwable t) {
                Log.e(TAG, "onFailure:" +t.toString());
                Toast.makeText(RegisterProductoActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
