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

import com.example.aplicacinftc.Activities.AnadirGrupoActivity;
import com.example.aplicacinftc.Activities.AnadirUserActivity;
import com.example.aplicacinftc.Adapters.GridAdapterGrupos;
import com.example.aplicacinftc.Adapters.GridAdapterUsers;
import com.example.aplicacinftc.Models.Grupo;
import com.example.aplicacinftc.Models.User;
import com.example.aplicacinftc.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class tab1Fragment extends Fragment {


    private View view;
    private FloatingActionButton fabUsers;
    private GridView gridView;
    private GridAdapterUsers adapterGridView;
    private List<User> todosUsers;
    private FirebaseAuth mAuth;
    private DatabaseReference mDataBase;
    private SearchView busqueda;

    public tab1Fragment() {
        // Required empty public constructor
    }

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_tab1, container, false);

        mAuth = FirebaseAuth.getInstance();
        mDataBase = FirebaseDatabase.getInstance().getReference();

        todosUsers = new ArrayList<User>();
        getUsersFromFirebase();

        gridView = (GridView) view.findViewById(R.id.gridViewUsers);
        gridView.setVisibility(View.VISIBLE);
        //gridView.setOnItemClickListener(this);
        registerForContextMenu(this.gridView);

        fabUsers = (FloatingActionButton) view.findViewById(R.id.fabUsers);
        fabUsers.setVisibility(View.VISIBLE);

        busqueda = (SearchView) view.findViewById(R.id.buscarBuzon);
        TextView searchEditText = (TextView) busqueda.findViewById(busqueda.getContext().getResources().getIdentifier("android:id/search_src_text", null, null));
        searchEditText.setTextColor(Color.WHITE);

        fabUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AnadirUserActivity.class);
                startActivity(intent);

            }
        });

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

                                mDataBase.child("Users").child(todosUsers.get(position).getId()).removeValue();
                            }
                        });
                siNo.setNegativeButton("Editar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        User usereditar = todosUsers.get(position);

                        Intent intent= new Intent(getActivity(), AnadirUserActivity.class);
                        intent.putExtra("idUser",usereditar.getId());
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
                    List<User> listaFiltrada = filter(todosUsers, newText);
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
    public void getUsersFromFirebase(){

        mDataBase.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    todosUsers.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String nombre = ds.child("nombre").getValue().toString();
                        String apellidos = ds.child("apellidos").getValue().toString();
                        //String email = ds.child("email").getValue().toString();
                        String email = "";
                        String edad = "";
                       // String edad = ds.child("edad").getValue().toString();
                        String foto = ds.child("foto").getValue().toString();
                        String recordar = "";
                        //String recordar = ds.child("recordar").getValue().toString();
                        String rol = ds.child("rol").getValue().toString();
                       // String grupo = ds.child("grupo").getValue().toString();
                        String grupo = "";
                       // Array[] asignaturas = ds.child("asignaturas").getValue();
                        ArrayList<String> asignaturas = null;
                        String id = ds.getKey();


                        User getUsers = new User(nombre, apellidos,email,edad, foto,recordar, rol, grupo, asignaturas,id);
                    todosUsers.add(getUsers);
                }
                gridView.setVisibility(View.VISIBLE);
                adapterGridView = new GridAdapterUsers(getContext(), R.layout.grid_item_user, todosUsers);
                gridView.setAdapter(adapterGridView);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private List<User> filter(List<User> users, String texto) {
        ArrayList<User> listaFiltrada = new ArrayList<>();

        try {
            texto = texto.toLowerCase();

            for (User user : users) {

                String nombre = user.getNombre().toLowerCase();
                String apellidos = user.getApellidos().toLowerCase();



                if ( nombre.contains(texto) || apellidos.contains(texto) ) {
                    listaFiltrada.add(user);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaFiltrada;
    }
}
