package com.example.aplicacinftc.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aplicacinftc.Activities.AnadirAsignaturaActivity;
import com.example.aplicacinftc.Activities.GlobalInfo;
import com.example.aplicacinftc.Adapters.GridAdapterAsignaturas;
import com.example.aplicacinftc.Adapters.GridAdapterAsignaturas2;
import com.example.aplicacinftc.Models.Asignaturas;
import com.example.aplicacinftc.Models.Grupo;
import com.example.aplicacinftc.Models.Reunion;
import com.example.aplicacinftc.Models.User;
import com.example.aplicacinftc.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class reunionesFragment extends Fragment {

    private View view;
    private GridView gridView;
    private GridAdapterAsignaturas2 adapterGridView;
    private List<Asignaturas> todosAsig;
    private List<Asignaturas> userAsig;
    private SearchView busqueda;
    private User usuarioIniciado;
    private ArrayList<String> asignaturas;
    private ArrayList<Reunion> todosReuniones;
    private String grupoUser;
    private String asigUser, idreunion,idElim;
    private Boolean crear = false;


    //OBJETO PARA LA BASE DE DATOS
    private FirebaseAuth mAuth;
    private DatabaseReference mDataBase;

    public reunionesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_reuniones, container, false);

        usuarioIniciado = GlobalInfo.UsuarioIniciado;
        asignaturas=usuarioIniciado.getAsignaturas();
        grupoUser=usuarioIniciado.getGrupo();

        mAuth = FirebaseAuth.getInstance();
        mDataBase = FirebaseDatabase.getInstance().getReference();

        todosAsig = new ArrayList<Asignaturas>();
        userAsig = new ArrayList<Asignaturas>();
        todosReuniones = new ArrayList<Reunion>();

        gridView = (GridView) view.findViewById(R.id.gridViewAsigReuniones);
        gridView.setVisibility(View.VISIBLE);
        //gridView.setOnItemClickListener(this);
        registerForContextMenu(this.gridView);
        getAsigsFromFirebase();
        getReunionesFromFirebase();


        busqueda = (SearchView) view.findViewById(R.id.buscarBuzon);
        TextView searchEditText = (TextView) busqueda.findViewById(busqueda.getContext().getResources().getIdentifier("android:id/search_src_text", null, null));
        searchEditText.setTextColor(Color.WHITE);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {

                final AlertDialog.Builder siNo = new AlertDialog.Builder(getContext());
                siNo.setCancelable(true);
                siNo.setTitle("¿Que desea hacer?");
                siNo.setMessage("Creara una reunion con su grupo. Si existe una reunion se eliminará.");

                siNo.setPositiveButton("Crear/Eliminar",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which ) {
                                Asignaturas asigMostrar = todosAsig.get(position);
                                asigUser = asigMostrar.getNombre();

                                Reunion reunion = new Reunion(asigUser, grupoUser, idreunion);

                                for(int i=0;i<todosReuniones.size();i++){
                                    if(todosReuniones.get(i).getNombreAsig().equals(reunion.getNombreAsig())&&todosReuniones.get(i).getSolicitado().equals(reunion.getSolicitado())){
                                        crear=false;
                                            break;
                                    }else{
                                        crear=true;

                                    }
                                }


                                if (crear){
                                    mDataBase.child("Reuniones").push().setValue(reunion).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getContext(), "REUNION CREADA", Toast.LENGTH_SHORT).show();

                                            } else {
                                                Toast.makeText(getContext(), "ERROR AL CREAR", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }else{
                                    mDataBase.child("Reuniones").child(todosReuniones.get(position).getId()).removeValue();
                                    adapterGridView.notifyDataSetChanged();

                                    Toast.makeText(getContext(), "REUNION ELIMINADA", Toast.LENGTH_SHORT).show();
                                    getReunionesFromFirebase();
                                }
                            }
                        });
                siNo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();

                    }
                });
                siNo.show();

            }
        });

        busqueda.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                try {
                    List<Asignaturas> listaFiltrada = filter(userAsig, newText);
                    adapterGridView.setFilter(listaFiltrada);
                    gridView.setAdapter(adapterGridView);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });

        return view;
    }
    private void getReunionesFromFirebase() {

        mDataBase.child("Reuniones").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    todosReuniones.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String nombreAsig = ds.child("nombreAsig").getValue().toString();
                        String solicitado = ds.child("solicitado").getValue().toString();
                        String id = ds.getKey();


                        Reunion getGrupo = new Reunion(nombreAsig, solicitado, id);
                        todosReuniones.add(getGrupo);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private List<Asignaturas> filter(List<Asignaturas> asignaturas, String texto) {
        ArrayList<Asignaturas> listaFiltrada = new ArrayList<>();

        try {
            texto = texto.toLowerCase();

            for (Asignaturas asignatura : asignaturas) {

                String nombre = asignatura.getNombre().toLowerCase();
                String curso = asignatura.getCurso().toLowerCase();
                String descripcion = asignatura.getDescripcion().toLowerCase();
                String foto = asignatura.getImagen().toLowerCase();



                if ( nombre.contains(texto) || curso.contains(texto) || descripcion.contains(texto) || foto.contains(texto) ) {
                    listaFiltrada.add(asignatura);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaFiltrada;
    }

    private void getAsigsFromFirebase() {

        mDataBase.child("Asignaturas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    todosAsig.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String nombre = ds.child("nombre").getValue().toString();
                        String curso = ds.child("curso").getValue().toString();
                        String descripcion = ds.child("descripcion").getValue().toString();
                        String imagen = ds.child("imagen").getValue().toString();
                        String id = ds.getKey();

                        Asignaturas getAsig = new Asignaturas(nombre, curso, descripcion, imagen, id);
                        todosAsig.add(getAsig);
                    }
                    for (int i = 0;i<todosAsig.size(); i++) {

                       for(int j = 0;j<asignaturas.size();j++){
                            if(todosAsig.get(i).getNombre().equals(asignaturas.get(j))){
                                userAsig.add(todosAsig.get(i));
                            }
                        }

                    }

                    gridView.setVisibility(View.VISIBLE);
                    adapterGridView = new GridAdapterAsignaturas2(getContext(), R.layout.grid_item_asignatura, userAsig);
                    gridView.setAdapter(adapterGridView);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
