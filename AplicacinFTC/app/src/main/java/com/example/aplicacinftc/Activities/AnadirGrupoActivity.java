package com.example.aplicacinftc.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aplicacinftc.Models.Asignaturas;
import com.example.aplicacinftc.Models.Grupo;
import com.example.aplicacinftc.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AnadirGrupoActivity extends AppCompatActivity {

    private EditText editnombre, editnumero;

    private Button botonadd;
    private DatabaseReference mDataBase;
    private String nombre, numero,id, idGrupo;
    private boolean editar= false;
    private Grupo grupoedit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir_grupo);

        mDataBase = FirebaseDatabase.getInstance().getReference();

        editnombre = (EditText) findViewById(R.id.textviewAddNombreG);
        editnumero = (EditText) findViewById(R.id.textviewAddNumero);

        botonadd  = (Button) findViewById(R.id.buttonAddGrupo);

        if (getIntent().getExtras()!=null){
            idGrupo = getIntent().getStringExtra("idGrupo");
            editar=true;
        }else {
            editar=false;
        }
        if(editar){
            mDataBase.child("Grupos").child(idGrupo).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //grupoedit = dataSnapshot.getValue(Grupo.class);
                    String nombreE = dataSnapshot.child("nombre").getValue().toString();
                    String numeroE = dataSnapshot.child("numero").getValue().toString();
                    editnombre.setText(nombreE);
                    editnumero.setText(numeroE);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        botonadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editar) {
                    nombre = editnombre.getText().toString();
                    numero = editnumero.getText().toString();
                    if(!nombre.isEmpty()&&!numero.isEmpty()) {
                        mDataBase.child("Grupos").child(idGrupo).child("nombre").setValue(nombre);
                        mDataBase.child("Grupos").child(idGrupo).child("numero").setValue(numero);
                        Toast.makeText(getApplication(), "COMPLETADO", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplication(), "COMPLETE TODOS LOS CAMPOS", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    crearGrupo();
                }
            }
        });
    }
    public void crearGrupo(){
        nombre = editnombre.getText().toString();
        numero = editnumero.getText().toString();


        if(!nombre.isEmpty()&&!numero.isEmpty()){

            Grupo grupo= new Grupo(nombre, numero, id);

            mDataBase.child("Grupos").push().setValue(grupo).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplication(), "GRUPO CREADO", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(getApplication(), "ERROR AL CREAR", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }else {
            Toast.makeText(getApplication(), "COMPLETE TODOS LOS CAMPOS", Toast.LENGTH_SHORT).show();
        }
    }
}
