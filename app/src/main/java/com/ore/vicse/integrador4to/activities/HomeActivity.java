package com.ore.vicse.integrador4to.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ore.vicse.integrador4to.R;
import com.ore.vicse.integrador4to.fragments.HomeFragment;
import com.ore.vicse.integrador4to.fragments.InfoFragment;
import com.ore.vicse.integrador4to.fragments.MapFragment;
import com.ore.vicse.integrador4to.fragments.ProductFragment;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity {


    private String nombrePro;
    private String correoPro;
    private String imagenPro;
    private Integer idProveedor;
    private SharedPreferences mPrefs;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setToolbar();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navview);

        mPrefs = getSharedPreferences("credenciales", Context.MODE_PRIVATE);

        nombrePro = mPrefs.getString("empresa", "Empresa");
        correoPro = mPrefs.getString("correo", "empresa@tecsup.edu.pe");
        imagenPro = mPrefs.getString("imagen" , null);


        idProveedor = mPrefs.getInt("id", 0);

        ImageView photoImage = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.menu_photo);


        photoImage.setBackgroundResource(R.drawable.ic_profile2);
        Picasso.with(navigationView.getContext()).load("http://integrador4tociclo-vicse.c9users.io"+imagenPro).resize(64,64).into(photoImage);

//        Picasso.with(navigationView.getContext()).load("http://integrador4tociclo-vicse.c9users.io"+imagenPro).into(navigationView.set;

        TextView fullnameText = (TextView) navigationView.getHeaderView(0).findViewById(R.id.menu_fullname);
        fullnameText.setText(nombrePro);

        TextView emailText = (TextView) navigationView.getHeaderView(0).findViewById(R.id.menu_email);
        emailText.setText(correoPro);

        setFragmentByDefault();

        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
//                Toast.makeText(HomeActivity.this,"Open",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
//                Toast.makeText(HomeActivity.this,"Close",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                boolean fragmentTransaction = false;
                Fragment fragment = null;

                switch (item.getItemId()){
                    case R.id.menu_home:
                        fragment = new HomeFragment();
                        fragmentTransaction = true;
                        break;
                    case R.id.menu_pro:
                        fragment = new ProductFragment();
                        fragmentTransaction = true;
                        Bundle datos = new Bundle();
                        datos.putInt("idPro",idProveedor);
                        fragment.setArguments(datos);
//                        Toast.makeText(HomeActivity.this,"Id proveedor"+ datos,Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menu_map:
                        fragment = new MapFragment();
                        fragmentTransaction = true;
                        break;
                    case R.id.menu_info:
                        fragment = new InfoFragment();
                        fragmentTransaction = true;
                        break;
                    case R.id.menu_conf:
                        Toast.makeText(HomeActivity.this,"Configuración",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menu_out:
                        logOut();
                        startActivity(new Intent(HomeActivity.this, MainActivity.class));
                        break;
                }

                if(fragmentTransaction){
                    changeFragment(fragment, item);
                    drawerLayout.closeDrawers();
                }
                return true;
            }
        });
    }

    public void logOut(){
        mPrefs = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=mPrefs.edit();
        editor.putInt("id", 0);
        editor.putString("empresa", null);
        editor.putString("ruc", null);
        editor.putString("correo", null);
        editor.putString("imagen", null);
        editor.putString("password", null);
        editor.apply();
    }


    private void setToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon( R.drawable.ic_menu );
    }

    private void setFragmentByDefault(){
        changeFragment(new HomeFragment(), navigationView.getMenu().getItem(0));
    }

    private void changeFragment(Fragment fragment, MenuItem item){
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
        item.setChecked(true);
        getSupportActionBar().setTitle(item.getTitle());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){
            case android.R.id.home:
                //abrir el menu lateral
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
