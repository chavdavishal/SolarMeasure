package com.example.navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.RVViewHolderClass> {
    private final RecyclerViewInterface recyclerViewInterface;
    static int pos;
    ArrayList<ModelClass> objectModelClassList;
    public RVAdapter(ArrayList<ModelClass> objectModelClassList, RecyclerViewInterface recyclerViewInterface){
        this.objectModelClassList = objectModelClassList;
        this.recyclerViewInterface=recyclerViewInterface;
    }
    @NonNull
    @Override
    public RVViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        return new RVViewHolderClass(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_row,parent,false), recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull RVViewHolderClass holder, int position) {
        ModelClass objectModelClass=objectModelClassList.get(position);
        holder.imageNameTv.setText(objectModelClass.getImageName());
        holder.Contact.setText(objectModelClass.getContact());
        holder.Email.setText(objectModelClass.getEmail());
        holder.Address.setText(objectModelClass.getAddress());
        holder.objectImageView.setImageBitmap(objectModelClass.getImage());

    }



    @Override
    public int getItemCount() {
        return objectModelClassList.size();
    }

    public static class RVViewHolderClass extends RecyclerView.ViewHolder{
        TextView imageNameTv,Contact,Email,Address;
        ImageView objectImageView;

        public RVViewHolderClass(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            imageNameTv=itemView.findViewById(R.id.textView);
            Contact=itemView.findViewById(R.id.textView1);
            Email=itemView.findViewById(R.id.textView2);
            Address=itemView.findViewById(R.id.textView3);


            objectImageView = itemView.findViewById(R.id.imageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(recyclerViewInterface != null){
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){

                            recyclerViewInterface.onItemClick();
                        }
                    }
                }
            });
        }
    }

}
