package com.ore.vicse.integrador4to.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ore.vicse.integrador4to.models.Almacen;

import java.util.List;

public class AlmacenesAdapter extends ArrayAdapter<Almacen> {

    private final Context mContext;
    private List<Almacen> mAlmacenes;

    public AlmacenesAdapter(@NonNull Context context, @NonNull List<Almacen> almacenes){
        super(context, 0, almacenes);
        mContext = context;
        mAlmacenes = almacenes;
    }

    @NonNull
    @Override
    public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent){
        View view;
        Almacen almacen = mAlmacenes.get(position);

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        }else{
            view = convertView;
        }

        TextView textView = view.findViewById(android.R.id.text1);
        textView.setText(almacen.getNombre());

        return view;
    }

    @Override
    public View getDropDownView(int position, @NonNull View converView, @NonNull ViewGroup parent){
        return getView(position, converView, parent);
    }

    @Override
    public int getCount(){
        return mAlmacenes.size();
    }

    @NonNull
    @Override
    public Almacen getItem(int position){
        return mAlmacenes.get(position);
    }

    public void replaceData(List<Almacen> almacenes){
        mAlmacenes = almacenes;
        notifyDataSetChanged();
    }
}
