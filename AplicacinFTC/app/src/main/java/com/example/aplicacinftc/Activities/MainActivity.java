package com.example.aplicacinftc.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.aplicacinftc.Fragments.gestionarFragment;
import com.example.aplicacinftc.Fragments.modificarFragment;
import com.example.aplicacinftc.Fragments.reunionesFragment;
import com.example.aplicacinftc.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private Button botonLogout;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView fotoPerfil;
    private TextView editnombre;
    private String nombre="";
    private String apellido="";

    //OBJETO PARA LA BASE DE DATOS
    private DatabaseReference mDataBase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setToolbar();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navview);


        mAuth = FirebaseAuth.getInstance();
        mDataBase = FirebaseDatabase.getInstance().getReference();

        Button buttonLogout = (Button) navigationView.getMenu().findItem(R.id.itemLogout).getActionView();

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mAuth.signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });

        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                editnombre = (TextView) findViewById(R.id.NombreUsuario);

                nombre=GlobalInfo.UsuarioIniciado.getNombre();
                apellido=GlobalInfo.UsuarioIniciado.getApellidos();

                editnombre.setText(nombre +" "+ apellido);


                if (!GlobalInfo.UsuarioIniciado.getFoto().equals("")){
                    fotoPerfil = (ImageView) findViewById(R.id.FotoPerfilMenu);
                    Picasso.get().load(GlobalInfo.UsuarioIniciado.getFoto()).into(fotoPerfil);
                }else{
                    GlobalInfo.UsuarioIniciado.setFoto("https://firebasestorage.googleapis.com/v0/b/arebee-9b43d.appspot.com/o/por%20defecto.jpg?alt=media&token=7fcb6996-f662-4457-89fe-e1221186f38d");
                }
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

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
                Activity activity=null;
                drawerLayout.closeDrawers();

                switch (item.getItemId()) {

                    case R.id.menu_gestionar:
                        fragment = new gestionarFragment();
                        fragmentTransaction = true;

                        break;
                    case R.id.menu_reuniones:
                        fragment = new reunionesFragment();
                        fragmentTransaction = true;

                        break;
                    case R.id.menu_modificar:
                        fragment= new modificarFragment();
                        //activity = new modificarActivity();
                        fragmentTransaction = true;

                        break;

                }


                if (fragmentTransaction) {
                    changeFragment(fragment, item);

                    drawerLayout.closeDrawers();
                }
                return true;
            }
        });


    }
    private void setToolbar() {
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void changeFragment(Fragment fragment, MenuItem item) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
        item.setChecked(true);
        getSupportActionBar().setTitle(item.getTitle());
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                // abrir el menu lateral
                if (drawerLayout.isDrawerOpen(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);
                }else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }




                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
