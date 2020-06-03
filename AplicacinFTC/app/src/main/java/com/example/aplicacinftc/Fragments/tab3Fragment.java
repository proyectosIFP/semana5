package com.example.aplicacinftc.Fragments;

import android.annotation.SuppressLint;
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

import com.example.aplicacinftc.Activities.AnadirAsignaturaActivity;
import com.example.aplicacinftc.Activities.AnadirGrupoActivity;
import com.example.aplicacinftc.Adapters.GridAdapterAsignaturas;
import com.example.aplicacinftc.Adapters.GridAdapterGrupos;
import com.example.aplicacinftc.Models.Asignaturas;
import com.example.aplicacinftc.Models.Grupo;
import com.example.aplicacinftc.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class tab3Fragment extends Fragment {

    private FloatingActionButton fabAsignaturas;

    private View view;

    //LO DE LA LISTA
    private GridView gridView;
    private GridAdapterAsignaturas adapterGridView;
    private List<Asignaturas> todosAsig;
    private SearchView busqueda;


    //OBJETO PARA LA BASE DE DATOS
    private FirebaseAuth mAuth;
    private DatabaseReference mDataBase;

    public tab3Fragment() {
        // Required empty public constructor
    }

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tab3, container, false);
        setHasOptionsMenu(true);

        mAuth = FirebaseAuth.getInstance();
        mDataBase = FirebaseDatabase.getInstance().getReference();

        todosAsig = new ArrayList<Asignaturas>();

        gridView = (GridView) view.findViewById(R.id.gridViewAsig);
        gridView.setVisibility(View.VISIBLE);
        //gridView.setOnItemClickListener(this);
        registerForContextMenu(this.gridView);
        getAsigsFromFirebase();


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder siNo = new AlertDialog.Builder(getContext());
                siNo.setCancelable(true);
                siNo.setTitle("¿Que desea hacer?");
                siNo.setMessage("Se eliminará permanentemente.");

                siNo.setPositiveButton("Eliminar",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which ) {

                                mDataBase.child("Asignaturas").child(todosAsig.get(position).getId()).removeValue();
                                adapterGridView.notifyDataSetChanged();
                                getAsigsFromFirebase();

                            }
                        });
                siNo.setNegativeButton("Editar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Asignaturas Asigeditar = todosAsig.get(position);



                        Intent intent= new Intent(getActivity(), AnadirAsignaturaActivity.class);
                        intent.putExtra("idAsig",Asigeditar.getId());
                        startActivity(intent);
                    }
                });
                siNo.show();
            }
        });

        fabAsignaturas = (FloatingActionButton) view.findViewById(R.id.fabAsignatura);
        fabAsignaturas.setVisibility(View.VISIBLE);

        busqueda = (SearchView) view.findViewById(R.id.buscarBuzon);
        TextView searchEditText = (TextView) busqueda.findViewById(busqueda.getContext().getResources().getIdentifier("android:id/search_src_text", null, null));
        searchEditText.setTextColor(Color.WHITE);

        busqueda.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                try {
                    List<Asignaturas> listaFiltrada = filter(todosAsig, newText);
                    adapterGridView.setFilter(listaFiltrada);
                    gridView.setAdapter(adapterGridView);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });

        fabAsignaturas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AnadirAsignaturaActivity.class);
                startActivity(intent);

            }
        });



        return view;
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

                        Asignaturas getAsig = new Asignaturas(nombre, curso, descripcion, imagen,id);
                        todosAsig.add(getAsig);
                    }
                    gridView.setVisibility(View.VISIBLE);
                    adapterGridView = new GridAdapterAsignaturas(getContext(), R.layout.grid_item_asignatura, todosAsig);
                    gridView.setAdapter(adapterGridView);
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
}
