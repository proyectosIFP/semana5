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

import com.example.aplicacinftc.Models.Asignaturas;
import com.example.aplicacinftc.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AnadirAsignaturaActivity extends AppCompatActivity {

    private EditText editnombre, editcurso, editdescripcion;

    private Button botonfoto, botonadd;

    private String nombre, curso, descripcion, foto,id, idAsig;

    private boolean editar= false;

    private Asignaturas asinaturaedit;

    //PARA LA FOTO DE PERFIL
    private static final int GALLERY_INTENT = 1;
    private Uri uri;
    private Boolean fileImg = false;

    private DatabaseReference mDataBase;
    private FirebaseAuth mAuth;
    private StorageReference mStorage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir_asignatura);

        mAuth = FirebaseAuth.getInstance();
        mDataBase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();

        editnombre = (EditText) findViewById(R.id.textviewAddNombre);
        editcurso = (EditText) findViewById(R.id.textviewAddCurso);
        editdescripcion = (EditText) findViewById(R.id.textviewAddDescripcion);
        botonfoto = (Button) findViewById(R.id.buttonAddFotoAsignatura);
        botonadd  = (Button) findViewById(R.id.buttonAddAsignatura);

        if (getIntent().getExtras()!=null){
            idAsig = getIntent().getStringExtra("idAsig");
            editar=true;
        }else {
            editar=false;
        }
        if(editar){
            mDataBase.child("Asignaturas").child(idAsig).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //grupoedit = dataSnapshot.getValue(Grupo.class);
                    String nombreE = dataSnapshot.child("nombre").getValue().toString();
                    String cursoE = dataSnapshot.child("curso").getValue().toString();
                    String descripcionE = dataSnapshot.child("descripcion").getValue().toString();
                    editnombre.setText(nombreE);
                    editcurso.setText(cursoE);
                    editdescripcion.setText(descripcionE);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }




        botonfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                //AQUI SE LLAMA AL METODO DE ABAJO DEL TODO
                startActivityForResult(intent, GALLERY_INTENT);
            }
        });

        botonadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editar) {
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
                                        foto = uri2.toString();
                                        //LLAMAMOS AL METODO PARA REGISTRO
                                        nombre = editnombre.getText().toString();
                                        curso = editcurso.getText().toString();
                                        descripcion = editdescripcion.getText().toString();
                                        if(!nombre.isEmpty()&&!curso.isEmpty()&&!curso.isEmpty()) {
                                            mDataBase.child("Asignaturas").child(idAsig).child("nombre").setValue(nombre);
                                            mDataBase.child("Asignaturas").child(idAsig).child("curso").setValue(curso);
                                            mDataBase.child("Asignaturas").child(idAsig).child("descripcion").setValue(descripcion);
                                            mDataBase.child("Asignaturas").child(idAsig).child("imagen").setValue(foto);
                                            Toast.makeText(getApplication(), "COMPLETADO", Toast.LENGTH_SHORT).show();
                                        }else{
                                            Toast.makeText(getApplication(), "COMPLETE TODOS LOS CAMPOS", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplication(), "ERROR AL CARGAR LA IMAGEN", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        //EN CASO DE NO SELECCIONAR NINGUNA FOTO SE GARGA ESTA POR DEFECTO
                        foto = "https://firebasestorage.googleapis.com/v0/b/aplicacinftc.appspot.com/o/por%20defecto.jpg?alt=media&token=e88772f9-b7c2-422c-967f-2d0016747b53";
                        nombre = editnombre.getText().toString();
                        curso = editcurso.getText().toString();
                        descripcion = editdescripcion.getText().toString();
                        if(!nombre.isEmpty()&&!curso.isEmpty()&&!curso.isEmpty()) {
                            mDataBase.child("Asignaturas").child(idAsig).child("nombre").setValue(nombre);
                            mDataBase.child("Asignaturas").child(idAsig).child("curso").setValue(curso);
                            mDataBase.child("Asignaturas").child(idAsig).child("descripcion").setValue(descripcion);
                            mDataBase.child("Asignaturas").child(idAsig).child("imagen").setValue(foto);

                            Toast.makeText(getApplication(), "COMPLETADO", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplication(), "COMPLETE TODOS LOS CAMPOS", Toast.LENGTH_SHORT).show();
                        }

                    }

                } else {
                    crearAsig();
                }
            }
        });

    }

    public void crearAsig(){
        nombre = editnombre.getText().toString();
        curso = editcurso.getText().toString();
        descripcion = editdescripcion.getText().toString();
        if(!nombre.isEmpty()&&!curso.isEmpty()&&!descripcion.isEmpty()){

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
                                foto = uri2.toString();
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
                foto = "https://firebasestorage.googleapis.com/v0/b/aplicacinftc.appspot.com/o/por%20defecto.jpg?alt=media&token=e88772f9-b7c2-422c-967f-2d0016747b53";
                completarRegistro();
            }

        }else {
            Toast.makeText(getApplication(), "COMPLETE TODOS LOS CAMPOS", Toast.LENGTH_SHORT).show();
        }
    }

    public void completarRegistro(){

        Asignaturas asignaturas= new Asignaturas(nombre, curso, descripcion, foto,id);

        mDataBase.child("Asignaturas").push().setValue(asignaturas).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplication(), "ASIGNATURA CREADA", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplication(), "ERROR AL CREAR", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //SI ES CORRECTO
        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {
            //HACEMOS UNA BARRA DE CARGA

            //GUARDAMOS LA IMAGEN EN EL URI Y VOLVEMOS TRUE EL FILEIMG
            uri = data.getData();
            fileImg = true;




        }

    }
}
