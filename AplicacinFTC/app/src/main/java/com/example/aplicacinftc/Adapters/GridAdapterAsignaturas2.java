package com.example.aplicacinftc.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.example.aplicacinftc.Activities.GlobalInfo;
import com.example.aplicacinftc.Models.Asignaturas;
import com.example.aplicacinftc.Models.Grupo;
import com.example.aplicacinftc.Models.Reunion;
import com.example.aplicacinftc.Models.User;
import com.example.aplicacinftc.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class GridAdapterAsignaturas2 extends BaseAdapter {

    private Context context;
    private int layout;
    private List<Asignaturas> list;
    private FirebaseAuth mAuth;
    private DatabaseReference mDataBase;
    private ArrayList<Reunion> todosReuniones;
    private ArrayList<String> asignaturas;
    private Boolean crear = false;


    public GridAdapterAsignaturas2(Context context, int layout, List<Asignaturas> list) {
        this.context = context;
        this.layout = layout;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Asignaturas getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        ViewHolder holder;


        mAuth = FirebaseAuth.getInstance();
        mDataBase = FirebaseDatabase.getInstance().getReference();
        todosReuniones =new ArrayList<Reunion>();
        asignaturas = new ArrayList<String>();

        if (convertView == null) {
            // SÃ³lo si estÃ¡ nulo, es decir, primera vez en ser renderizado, inflamos
            // y adjuntamos las referencias del layout en una nueva instancia de nuestro
            // ViewHolder, y lo insertamos dentro del convertView, para reciclar su uso
            convertView = LayoutInflater.from(context).inflate(layout, null);
            holder = new ViewHolder();
            holder.iconItem = (ImageView) convertView.findViewById(R.id.imageViewFotoAsignatura);
            holder.nombreItem = (TextView) convertView.findViewById(R.id.textviewNombre);
            holder.cursoItem = (TextView) convertView.findViewById(R.id.textviewCurso);
            holder.shape = (LinearLayout) convertView.findViewById(R.id.shapeEmple);
            holder.Iniciado = GlobalInfo.UsuarioIniciado;
            convertView.setTag(holder);
        } else {
            // Obtenemos la referencia que posteriormente pusimos dentro del convertView
            // Y asÃ­, reciclamos su uso sin necesidad de buscar de nuevo, referencias con FindViewById
            holder = (ViewHolder) convertView.getTag();
        }
        Resources res = context.getResources();

        asignaturas =holder.Iniciado.getAsignaturas();

        final Asignaturas currentUser = getItem(position);
        Picasso.get().load(currentUser.getImagen()).into(holder.iconItem);
        holder.nombreItem.setText(currentUser.getNombre());
        holder.cursoItem.setText(currentUser.getCurso());

        getReunionesFromFirebase();

        for(int i=0;i<todosReuniones.size();i++) {
            for (int j = 0; j < asignaturas.size(); j++) {
                if (holder.Iniciado.getGrupo().equals(todosReuniones.get(i).getSolicitado()) && holder.Iniciado.getAsignaturas().get(j).equals(todosReuniones.get(i).getNombreAsig())) {
                    Drawable drawable2 = ResourcesCompat.getDrawable(res, R.drawable.radius2, null);
                    holder.shape.setBackground(drawable2);
                } else {
                    Drawable drawable = ResourcesCompat.getDrawable(res, R.drawable.radius, null);
                    holder.shape.setBackground(drawable);
                }
            }
        }




        return convertView;
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


                        Reunion reu = new Reunion(nombreAsig, solicitado, id);
                        todosReuniones.add(reu);
                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public void setFilter(List<Asignaturas>list){
        this.list=new ArrayList<>();
        this.list.addAll(list);
        notifyDataSetChanged();

    }

    static class ViewHolder {
        private ImageView iconItem;
        private TextView cursoItem;
        private TextView nombreItem;
        private LinearLayout shape;
        private User Iniciado;

    }

}
