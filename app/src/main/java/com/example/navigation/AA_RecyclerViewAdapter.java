package com.example.navigation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AA_RecyclerViewAdapter extends RecyclerView.Adapter<AA_RecyclerViewAdapter.MyViewHolder> {
    Context context;
    ArrayList<AminoAcidModel>aminoAcidModels;
    public AA_RecyclerViewAdapter(Context context, ArrayList<AminoAcidModel> aminoAcidModels){
        this.context = context;
        this.aminoAcidModels = aminoAcidModels;
    }

    @NonNull
    @Override
    public AA_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recyler_view_raw,parent,false);
        return new AA_RecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AA_RecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.tvname.setText(aminoAcidModels.get(position).getAminoAcidName());
        holder.tva.setText(aminoAcidModels.get(position).getAminoAcideAbbreviation());
        holder.imageView.setImageResource(aminoAcidModels.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return aminoAcidModels.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView tvname,tva;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            tvname = itemView.findViewById(R.id.textView);
            tva = itemView.findViewById(R.id.textView2);
        }
    }
}
