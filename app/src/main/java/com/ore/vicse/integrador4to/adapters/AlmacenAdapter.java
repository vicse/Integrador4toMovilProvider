package com.ore.vicse.integrador4to.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ore.vicse.integrador4to.R;
import com.ore.vicse.integrador4to.models.Almacen;

import java.util.List;

public class AlmacenAdapter extends ArrayAdapter<Almacen> {

    private Context aContext;
    private List<Almacen> mItems;

    public AlmacenAdapter(@NonNull Context context, @NonNull List<Almacen> items){
        super(context, 0, items);
        aContext = context;
        mItems = items;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        Almacen almacen = mItems.get(position);

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(aContext);
            view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        }else{
            view = convertView;
        }

        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        textView.setText(almacen.getNombre());

        return view;
    }

    @Override
    public View getDropDownView(int position, @NonNull View converView, @NonNull ViewGroup parent){
        return getView(position, converView, parent);
    }
}
