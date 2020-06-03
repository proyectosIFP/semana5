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

import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aplicacinftc.Models.Asignaturas;
import com.example.aplicacinftc.Models.Grupo;
import com.example.aplicacinftc.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class GridAdapterAsignaturas extends BaseAdapter {

    private Context context;
    private int layout;
    private List<Asignaturas> list;

    public GridAdapterAsignaturas(Context context, int layout, List<Asignaturas> list) {
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

        if (convertView == null) {
            // SÃ³lo si estÃ¡ nulo, es decir, primera vez en ser renderizado, inflamos
            // y adjuntamos las referencias del layout en una nueva instancia de nuestro
            // ViewHolder, y lo insertamos dentro del convertView, para reciclar su uso
            convertView = LayoutInflater.from(context).inflate(layout, null);
            holder = new ViewHolder();
            holder.iconItem = (ImageView) convertView.findViewById(R.id.imageViewFotoAsignatura);
            holder.nombreItem = (TextView) convertView.findViewById(R.id.textviewNombre);
            holder.cursoItem = (TextView) convertView.findViewById(R.id.textviewCurso);
            convertView.setTag(holder);
        } else {
            // Obtenemos la referencia que posteriormente pusimos dentro del convertView
            // Y asÃ­, reciclamos su uso sin necesidad de buscar de nuevo, referencias con FindViewById
            holder = (ViewHolder) convertView.getTag();
        }
        Resources res = context.getResources();
        Drawable drawable = ResourcesCompat.getDrawable(res, R.drawable.radius, null);
       // holder.shape.setBackground(drawable);

        final Asignaturas currentUser = getItem(position);
        Picasso.get().load(currentUser.getImagen()).into(holder.iconItem);
        holder.nombreItem.setText(currentUser.getNombre());
        holder.cursoItem.setText(currentUser.getCurso());




        return convertView;
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

    }
}
