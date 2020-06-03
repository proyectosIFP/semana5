package com.example.aplicacinftc.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aplicacinftc.Models.User;
import com.example.aplicacinftc.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    private EditText editEmail;
    private EditText editContra;
    private Switch recordarSwitch;

    private String email;
    private String password;

    private Button buttonLogin;
    private Button buttonRegister;
    private Button buttonResetear;

    //EMPLEADO INICIADO
    private String emailIniciado = "";
    private String nombre = "";
    private String apellidos = "";
    private String contraseña = "";
    private String edad = "";
    private String foto = "";
    private String recordar = "";
    private String rol = "";
    private String grupo = "";
    private ArrayList<String> asignaturas;
    private String idUser = "";


    //FORMA DE ENTRAR
    private boolean entrar;
    private ProgressDialog mProgressDialog;


    //OBJETO PARA LA BASE DE DATOS
    private DatabaseReference mDataBase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editEmail = (EditText) findViewById(R.id.loginCorreoUser);
        editContra = (EditText) findViewById(R.id.loginPasswordUser);
        recordarSwitch = (Switch) findViewById(R.id.recordarSwitch);

        mAuth = FirebaseAuth.getInstance();
        mDataBase = FirebaseDatabase.getInstance().getReference();

        mProgressDialog = new ProgressDialog(this);

        buttonLogin = (Button) findViewById(R.id.buttonLoginUser);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //COGEMOS LO DE LOS EDIT
                email = editEmail.getText().toString();
                password = editContra.getText().toString();
                //PARA QUE NO ESTÉ VACIO
                if (!email.isEmpty() && !password.isEmpty()) {
                    //METODO PARA LOGIN
                    loginUser();
                } else {
                    Toast.makeText(getApplication(), "COMPLETE TODOS LOS CAMPOS", Toast.LENGTH_SHORT).show();
                }

            }
        });

        buttonRegister = (Button) findViewById(R.id.buttonRegisterUserLlamar);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });

        buttonResetear = (Button) findViewById(R.id.buttonResetearLlamar);

        buttonResetear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RecuperarActivity.class));
                finish();
            }
        });

    }

    private void loginUser() {
        //INICIO CON EMAIL Y LA CONTRASEÑA
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    //SI INICIA PASAMOS AL MAIN
                    if (recordarSwitch.isChecked()){
                        mDataBase.child("Users").child(mAuth.getCurrentUser().getUid()).child("recordar").setValue("true");
                    }else{
                        mDataBase.child("Users").child(mAuth.getCurrentUser().getUid()).child("recordar").setValue("false");
                    }
                    entrar=false;
                    getUsersInfo();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(getApplication(), "EMAIL O CONTRASEÑA INCORRECTOS", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplication(), "ERROR AL INICIAR SESION", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //METODO AL INICIAR LA APP

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            FirebaseUser user = mAuth.getCurrentUser();
            entrar=true;
            mProgressDialog.setTitle("Cargando...");
            mProgressDialog.setMessage("Cargando...");
            mProgressDialog.show();
            getUsersInfo();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    mProgressDialog.dismiss();
                }
            }, 1500);
        }
    }


    private void getUsersInfo() {
        String id = mAuth.getCurrentUser().getUid();
        mDataBase.child("Users").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    emailIniciado = dataSnapshot.child("email").getValue().toString();
                    nombre = dataSnapshot.child("nombre").getValue().toString();
                    apellidos = dataSnapshot.child("apellidos").getValue().toString();
                    edad = dataSnapshot.child("edad").getValue().toString();
                    foto = dataSnapshot.child("foto").getValue().toString();
                    recordar = dataSnapshot.child("recordar").getValue().toString();
                    rol = dataSnapshot.child("rol").getValue().toString();
                    grupo = dataSnapshot.child("grupo").getValue().toString();
                    asignaturas = (ArrayList<String>) dataSnapshot.child("asignaturas").getValue();
                    idUser = dataSnapshot.getKey();

                    FirebaseUser user = mAuth.getCurrentUser();
                    String id = mAuth.getCurrentUser().getUid();

                    GlobalInfo.UsuarioIniciado = new User(emailIniciado, nombre, apellidos, edad, foto, recordar,rol,grupo,asignaturas,idUser);

                    if (entrar){
                        //SI YA HAY USUARIO PASAS DIRECTAMENTE AL MAIN
                        if (GlobalInfo.UsuarioIniciado.getRecordar().equals("true")){
                            //SI YA HAY USUARIO PASAS DIRECTAMENTE AL MAIN
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }
                    }else {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplication(), "ERROR AL INICIAR SESION", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
