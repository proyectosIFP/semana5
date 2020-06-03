package com.example.aplicacinftc.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
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

public class tab2Fragment extends Fragment {

    private View view;
    private FloatingActionButton fabGrupo;
    private GridView gridView;
    private GridAdapterGrupos adapterGridView;
    private List<Grupo> todosGrupos;
    private FirebaseAuth mAuth;
    private DatabaseReference mDataBase;
    private SearchView busqueda;


    public tab2Fragment() {
        // Required empty public constructor
    }


    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tab2, container, false);

        mAuth = FirebaseAuth.getInstance();
        mDataBase = FirebaseDatabase.getInstance().getReference();

        todosGrupos = new ArrayList<Grupo>();
        getGruposFromFirebase();

        gridView = (GridView) view.findViewById(R.id.gridViewGrupos);
        gridView.setVisibility(View.VISIBLE);
        //gridView.setOnItemClickListener(this);
        registerForContextMenu(this.gridView);


        fabGrupo = (FloatingActionButton) view.findViewById(R.id.fabGrupos);
        fabGrupo.setVisibility(View.VISIBLE);

        busqueda = (SearchView) view.findViewById(R.id.buscarBuzon);
        TextView searchEditText = (TextView) busqueda.findViewById(busqueda.getContext().getResources().getIdentifier("android:id/search_src_text", null, null));
        searchEditText.setTextColor(Color.WHITE);


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

                                mDataBase.child("Grupos").child(todosGrupos.get(position).getId()).removeValue();
                            }
                        });
                siNo.setNegativeButton("Editar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Grupo grupoeditar = todosGrupos.get(position);



                        Intent intent= new Intent(getActivity(),AnadirGrupoActivity.class);
                        intent.putExtra("idGrupo",grupoeditar.getId());
                        startActivity(intent);
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
                        List<Grupo> listaFiltrada = filter(todosGrupos, newText);
                        adapterGridView.setFilter(listaFiltrada);
                        gridView.setAdapter(adapterGridView);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });


        fabGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AnadirGrupoActivity.class);
                startActivity(intent);

            }
        });

        return view;
    }

    private void getGruposFromFirebase() {

        mDataBase.child("Grupos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    todosGrupos.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String nombre = ds.child("nombre").getValue().toString();
                        String numero = ds.child("numero").getValue().toString();
                        String id = ds.getKey();


                        Grupo getGrupo = new Grupo(nombre, numero, id);
                        todosGrupos.add(getGrupo);
                    }
                    gridView.setVisibility(View.VISIBLE);
                    adapterGridView = new GridAdapterGrupos(getContext(), R.layout.grid_item_grupo, todosGrupos);
                    gridView.setAdapter(adapterGridView);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private List<Grupo> filter(List<Grupo> grupos, String texto) {
        ArrayList<Grupo> listaFiltrada = new ArrayList<>();

        try {
            texto = texto.toLowerCase();

            for (Grupo grupo : grupos) {

                String nombre = grupo.getNombre().toLowerCase();
                String numero = grupo.getNumero().toLowerCase();



                if ( nombre.contains(texto) || numero.contains(texto) ) {
                    listaFiltrada.add(grupo);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaFiltrada;
    }



}
