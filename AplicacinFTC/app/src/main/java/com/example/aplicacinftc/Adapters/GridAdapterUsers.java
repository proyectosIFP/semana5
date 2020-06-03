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

import com.example.aplicacinftc.Models.Asignaturas;
import com.example.aplicacinftc.Models.User;
import com.example.aplicacinftc.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class GridAdapterUsers extends BaseAdapter {

    private Context context;
    private int layout;
    private List<User> list;

    public GridAdapterUsers(Context context, int layout, List<User> list) {
        this.context = context;
        this.layout = layout;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public User getItem(int position) {
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
            holder.iconItem = (ImageView) convertView.findViewById(R.id.imageViewFotoUsuario);
            holder.nombreItem = (TextView) convertView.findViewById(R.id.textviewNombreUser);
            holder.apellidoItem = (TextView) convertView.findViewById(R.id.textviewApellidos);
            holder.rolItem = (TextView) convertView.findViewById(R.id.textviewRol);
            convertView.setTag(holder);

        } else {
            // Obtenemos la referencia que posteriormente pusimos dentro del convertView
            // Y asÃ­, reciclamos su uso sin necesidad de buscar de nuevo, referencias con FindViewById
            holder = (ViewHolder) convertView.getTag();

        }
        Resources res = context.getResources();
        Drawable drawable = ResourcesCompat.getDrawable(res, R.drawable.radius, null);
        // holder.shape.setBackground(drawable);

        final User usuario = getItem(position);
        Picasso.get().load(usuario.getFoto()).into(holder.iconItem);
        holder.nombreItem.setText(usuario.getNombre());
        holder.apellidoItem.setText(usuario.getApellidos());
        holder.rolItem.setText(usuario.getRol());

        return convertView;
    }
    public void setFilter(List<User>list){
        this.list=new ArrayList<>();
        this.list.addAll(list);
        notifyDataSetChanged();

    }

    static class ViewHolder {
        private ImageView iconItem;
        private TextView rolItem;
        private TextView nombreItem;
        private TextView apellidoItem;
        private LinearLayout shape;

    }

}
