package com.example.navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RVPAdapter extends RecyclerView.Adapter<RVPAdapter.RVViewHolderClass> {
    private final ProductInterface productInterface;

    ArrayList<ProductModel> objectproductClassList;
    public RVPAdapter(ArrayList<ProductModel> objectModelClassList, ProductInterface productInterface){
        this.objectproductClassList = objectModelClassList;
        this.productInterface=productInterface;

    }
    @NonNull
    @Override
    public RVViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int position) {

        return new RVViewHolderClass(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_row,parent,false),productInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull RVViewHolderClass holder, int position) {
        ProductModel objectModelClass=objectproductClassList.get(position);
      //  holder.cid.setText(objectModelClass.getCid());
        holder.pname.setText(objectModelClass.getPname());
        holder.pprice.setText(objectModelClass.getPprice());
        holder.pwatts.setText(objectModelClass.getPwatts());
        holder.objectImageView.setImageBitmap(objectModelClass.getImage());

    }



    @Override
    public int getItemCount() {
        return objectproductClassList.size();
    }

    public static class RVViewHolderClass extends RecyclerView.ViewHolder{
        TextView cid,pname,pprice,pwatts;
        ImageView objectImageView;

        public RVViewHolderClass(@NonNull View itemView,ProductInterface productView) {
            super(itemView);
           // cid=itemView.findViewById(R.id.textView);
            pname=itemView.findViewById(R.id.nametext);
            pprice=itemView.findViewById(R.id.priceText);
            pwatts=itemView.findViewById(R.id.wattsText);


            objectImageView = itemView.findViewById(R.id.imageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(productView != null){
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            productView.getData(pos);
                        }
                    }
                }
            });




        }
    }

}
