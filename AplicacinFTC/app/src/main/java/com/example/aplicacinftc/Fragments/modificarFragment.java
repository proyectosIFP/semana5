package com.example.aplicacinftc.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aplicacinftc.Activities.GlobalInfo;
import com.example.aplicacinftc.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class modificarFragment extends Fragment {

    private static final int GALLERY_INTENT = 1;
    private View view;
    private EditText editnombre;
    private EditText editapellidos;
    private EditText editedad;
    private Button buttonimg;
    private Button buttonactualizar;
    private Boolean fileImg = false;

    private DatabaseReference mDataBase;
    private StorageReference mStorage;
    private FirebaseAuth mAuth;

    private FirebaseUser user = mAuth.getInstance().getCurrentUser();
    private String id = user.getUid().toString();
    private String stringFoto;
    private Uri uri;

    private String nombre, apellidos, edad, foto;

    public modificarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_modificar, container, false);

        mAuth = FirebaseAuth.getInstance();
        mDataBase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();

        editnombre = (EditText) view.findViewById(R.id.cambiarNombreUser);
        editapellidos = (EditText) view.findViewById(R.id.cambiarApellidosUser);
        editedad = (EditText) view.findViewById(R.id.cambiarEdadUser);
        buttonimg = (Button) view.findViewById(R.id.buttoncambiarFotoPerfil);
        buttonactualizar = (Button) view.findViewById(R.id.buttonCambairUser);

        buttonimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                //AQUI SE LLAMA AL METODO DE ABAJO DEL TODO
                startActivityForResult(intent, GALLERY_INTENT);
                //fileImg=true;

            }
        });

        buttonactualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombre = editnombre.getText().toString();
                apellidos = editapellidos.getText().toString();
                edad = editedad.getText().toString();

                if (!nombre.isEmpty() && !apellidos.isEmpty() && !edad.isEmpty() && !nombre.trim().equals("") && !apellidos.trim().equals("") && !edad.trim().equals("")) {


                    //SUBIR IMAGEN
                    if (fileImg) {

                        cambiarFoto();

                        ModificarUsuario();

                    } else {
                        //EN CASO DE NO SELECCIONAR NINGUNA FOTO SE GARGA ESTA POR DEFECTO
                        foto = "https://firebasestorage.googleapis.com/v0/b/aplicacinftc.appspot.com/o/por%20defecto.jpg?alt=media&token=e88772f9-b7c2-422c-967f-2d0016747b53";
                        //mDataBase.child("Users").child(id).child("nombre").setValue(nombre);
                        ModificarUsuario();
                    }
                    //TERMINAR DE SUBIR IMAGEN
                    //MAS VALIDACIONES

                } else {
                    Toast.makeText(getContext(), "COMPLETE TODOS LOS CAMPOS", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void ModificarUsuario() {


        mDataBase.child("Users").child(id).child("nombre").setValue(nombre);
        //GlobalInfo.UsuarioIniciado.setNombre(nombre);

        mDataBase.child("Users").child(id).child("apellidos").setValue(apellidos);
        //GlobalInfo.UsuarioIniciado.setApellidos(apellidos);

        mDataBase.child("Users").child(id).child("edad").setValue(edad);
        //GlobalInfo.UsuarioIniciado.setEdad(edad);

        //mDataBase.child("Users").child(id).child("foto").setValue(foto);
        //GlobalInfo.UsuarioIniciado.setFoto(foto);

    }

    public void cambiarFoto(){
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
                    public void onSuccess(Uri uri) {
                        //URI DOS CORRESPONDE A downloadUrl Y SE LO AÑADIMOS A UN STRING QUE LE PASAMOS AL OBJETO
                        stringFoto = uri.toString();
                        mDataBase.child("Users").child(id).child("foto").setValue(stringFoto);


                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //SI ES CORRECTO
        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK){

            //GUARDAMOS LA IMAGEN EN EL URI Y VOLVEMOS TRUE EL FILEIMG
            uri = data.getData();
            fileImg = true;



        }

    }
}
