package com.webnexs.tranznexsdriver;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CabsTypeAdapter extends RecyclerView.Adapter<CabsTypeAdapter.CustomViewHolder> {
    private List<CabType> Cabtypes;
    private AdapterView.OnItemClickListener listener;
    public Context context;
    public ArrayList<CabsTypeAdapter> dataItems;


    public CabsTypeAdapter(List<CabType> Cabtypes)
    {
        this.Cabtypes = Cabtypes;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.car_dialog_layout, parent, false);

        return new CustomViewHolder(itView);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        CabType gobase = Cabtypes.get(position);
        holder.model.setText(gobase.getName());
        holder.id.setText(gobase.getId());
    }

    @Override
    public int getItemCount() {
        return Cabtypes.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        public TextView model,id;



        public CustomViewHolder(View itView) {
            super(itView);
            model = (TextView) itView.findViewById(R.id.model);
            id = (TextView) itView.findViewById(R.id.id);
        }
    }
}