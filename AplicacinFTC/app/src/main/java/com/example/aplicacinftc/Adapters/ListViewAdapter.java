package com.example.aplicacinftc.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.example.aplicacinftc.Models.Asignaturas;
import com.example.aplicacinftc.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ListViewAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<Asignaturas> list;

    public ListViewAdapter(Context context, int layout, List<Asignaturas> list) {
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
            holder.nombreItem = (TextView) convertView.findViewById(R.id.listviewNombreAsig);
            holder.checkItem = (CheckBox) convertView.findViewById(R.id.checkBox);
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
        holder.nombreItem.setText(currentUser.getNombre());
        holder.checkItem.setChecked(false);




        return convertView;
    }
    public void setFilter(List<Asignaturas>list){
        this.list=new ArrayList<>();
        this.list.addAll(list);
        notifyDataSetChanged();

    }

    static class ViewHolder {
        private TextView nombreItem;
        private CheckBox checkItem;

    }

}
