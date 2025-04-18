package com.example.posapp.model;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.posapp.R;

import java.util.ArrayList;

public class MyRVadapter extends RecyclerView.Adapter<MyRVadapter.Myholder> {
ArrayList<Inventory> products = new ArrayList<>();
private OnClickStockItem onClickStockItem;

    public MyRVadapter(ArrayList<Inventory> products , OnClickStockItem onClickStockItem) {
        this.products = products;
        this.onClickStockItem = onClickStockItem;
    }

    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View v=  LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_custom_layout,parent,false);
        return new Myholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Myholder holder, int position) {
        holder.product_name.setText(products.get(position).getProduct_name());
        holder.id = products.get(position).getProductID();
        holder.price_of_buy.setText(String.valueOf(products.get(position).getPriceOfBuy()));
        holder.price_of_sell.setText(String.valueOf(products.get(position).getPriceOfSell()));
        holder.quantity.setText(String.valueOf(products.get(position).getQuantity()));

    }

    @Override
    public int getItemCount() {
      return products.size();
    }

    public class Myholder extends RecyclerView.ViewHolder {
        TextView product_name;
        TextView price_of_buy;
        TextView price_of_sell;
        TextView quantity;
        int id;

        public Myholder(@NonNull View itemView) {
            super(itemView);
            product_name = itemView.findViewById(R.id.custom_product_name);
            price_of_buy = itemView.findViewById(R.id.custom_price_of_buy);
            price_of_sell = itemView.findViewById(R.id.custom_price_of_sell);
            quantity = itemView.findViewById(R.id.custom_quantity);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                      onClickStockItem.OnClickItem(id);

                }
            });

        }
    }

    public void updatedata(ArrayList<Inventory> data){
        products = data;
        this.notifyDataSetChanged();
    }
}
