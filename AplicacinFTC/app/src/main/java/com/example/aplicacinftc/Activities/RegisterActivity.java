package com.example.aplicacinftc.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aplicacinftc.Models.User;
import com.example.aplicacinftc.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    private EditText editEmail, editNombre, editApellidos, editEdad, editContraseña, editRepetirContraseña;

    private Button botonAñadirFoto, botonRegistrar, botonLogin;

    private String email, nombre, apellidos, edad, contraseña, repetirContraseña, stringFoto, idUser;
    private String recordar = "false";
    private String rol= "alumno";
    private String grupo;
    private ArrayList<String> asignaturas;
    //PARA LA FOTO DE PERFIL
    private static final int GALLERY_INTENT = 1;
    private Uri uri;
    private Boolean fileImg = false;
    private ProgressDialog mProgressDialogFoto;

    //OBJETO PARA LA BASE DE DATOS
    private DatabaseReference mDataBase;
    private FirebaseAuth mAuth;
    private StorageReference mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        mDataBase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();

        editEmail = (EditText) findViewById(R.id.registerCorreoUser);
        editNombre = (EditText) findViewById(R.id.registerNombreUser);
        editApellidos = (EditText) findViewById(R.id.registerApellidosUser);
        editEdad = (EditText) findViewById(R.id.registerEdadUser);
        editContraseña = (EditText) findViewById(R.id.registerPasswordUser);
        editRepetirContraseña = (EditText) findViewById(R.id.registerRepeatPasswordUser);

        botonAñadirFoto = (Button) findViewById(R.id.buttonAñadirFotoPerfil);

        botonAñadirFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                //AQUI SE LLAMA AL METODO DE ABAJO DEL TODO
                startActivityForResult(intent, GALLERY_INTENT);
            }
        });

        botonRegistrar = (Button) findViewById(R.id.buttonRegisterUser);

        botonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarUsuario();
            }
        });

        botonLogin = (Button) findViewById(R.id.buttonLoginUserLlamar);

        botonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });


    }

    private void registrarUsuario() {

        //COGEMOS LO DE LOS EDIT Y LOS SPINNER
        email = editEmail.getText().toString();
        nombre = editNombre.getText().toString();
        apellidos = editApellidos.getText().toString();
        edad = editEdad.getText().toString();
        contraseña = editContraseña.getText().toString();
        repetirContraseña = editRepetirContraseña.getText().toString();
        recordar = "false";


        //VALIDACIONES
        if (!email.isEmpty() && !nombre.isEmpty() && !apellidos.isEmpty() && !edad.isEmpty() && !contraseña.isEmpty() && !repetirContraseña.isEmpty() && !email.trim().equals("") && !nombre.trim().equals("") && !apellidos.trim().equals("") && !edad.trim().equals("")) {

            if (contraseña.length() >= 6 || contraseña.trim().equals("")) {
                if (contraseña.equals(repetirContraseña)) {
                    //SUBIR IMAGEN
                    if (fileImg) {
                        //MARCAMOS LA RUTA DE LAS IMAGENES EN EL STORANGE
                        final StorageReference filePath = mStorage.child("Fotos de perfil").child(uri.getLastPathSegment());
                        //COGEMOS ESA RUTA Y LE METEMOS LA URI, QUE ES LA FOTO SELECCIONADA, GUARDADA ANTERIORMENTE EN EL ANTERIOR ON CLICK
                        filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                //GUARDA LA URL DE LA ULTIMA FOTO SUBIDA
                                Task<Uri> downloadUrl = mStorage.child("Fotos de perfil").child(uri.getLastPathSegment()).getDownloadUrl();
                                //SI ES CORRECTO ENTRAMOS AQUI
                                downloadUrl.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri2) {
                                        //URI DOS CORRESPONDE A downloadUrl Y SE LO AÑADIMOS A UN STRING QUE LE PASAMOS AL OBJETO
                                        stringFoto = uri2.toString();
                                        //LLAMAMOS AL METODO PARA REGISTRO
                                        completarRegistro();

                                    }
                                });

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplication(), "ERROR AL CARGAR LA IMAGEN", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        //EN CASO DE NO SELECCIONAR NINGUNA FOTO SE GARGA ESTA POR DEFECTO
                        stringFoto = "https://firebasestorage.googleapis.com/v0/b/aplicacinftc.appspot.com/o/por%20defecto.jpg?alt=media&token=e88772f9-b7c2-422c-967f-2d0016747b53";
                        completarRegistro();
                    }
                    //TERMINAR DE SUBIR IMAGEN
                    //MAS VALIDACIONES
                } else {
                    Toast.makeText(getApplication(), "LAS CONTRASEÑAS NO COINCIDEN", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplication(), "LA CONTRASEÑA NO ES SEGURA", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplication(), "COMPLETE TODOS LOS CAMPOS", Toast.LENGTH_SHORT).show();
        }

    }


    private void completarRegistro() {

        //EL AUTH PARA CREAR USUARIO EN EL AUTHENTIFICATION PASANDOLE EL CORREO Y CONTRASEÑA
        mAuth.createUserWithEmailAndPassword(email, contraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //GUARDAMOS LA ID DEL USUARIO CREADO
                    String id = mAuth.getCurrentUser().getUid();
                    //CREAMOS OBJETO PARA PASARSELO A LA BASE
                    User user = new User(email, nombre, apellidos, edad, stringFoto, recordar,rol,grupo,asignaturas,idUser);
                    //ENTRAMOS DONDE LOS USUARIOS Y LE METEMOS EL OBJETO LUEGO HAY VALIDACIONES
                    mDataBase.child("Users").child(id).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        //COMIENZO VALIDACIONES
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if (task2.isSuccessful()) {
                                Toast.makeText(getApplication(), "USUARIO REGISTRADO", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                finish();
                            } else {
                                Toast.makeText(getApplication(), "ERROR EN EL REGISTRO", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    mAuth.signOut();
                } else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplication(), "EMAIL YA EN USO", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplication(), "ERROR EN EL REGISTRO", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplication(), "COMPRUEBE SI LOS DATOS SON CORRECTOS", Toast.LENGTH_SHORT).show();
            }
        });
        //FIN VALIDACIONES
    }


    //PARA CARGAR LA FOTO DENTRO DEL URI

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //SI ES CORRECTO
        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {
            //HACEMOS UNA BARRA DE CARGA
            mProgressDialogFoto = new ProgressDialog(this);
            mProgressDialogFoto.setTitle("Cargando imagen...");
            mProgressDialogFoto.setMessage("Cargando...");
            mProgressDialogFoto.show();
            //GUARDAMOS LA IMAGEN EN EL URI Y VOLVEMOS TRUE EL FILEIMG
            uri = data.getData();
            fileImg = true;

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    mProgressDialogFoto.dismiss();
                }
            }, 500);


        }

    }

}
