package com.example.posapp.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.posapp.R;

import java.util.ArrayList;

public class RV_Invoice_detail extends RecyclerView.Adapter<RV_Invoice_detail.detailInvoiceholber> {
    private Context ctx;
    private ArrayList<Product_Bill> product_Bills;


    public RV_Invoice_detail(ArrayList<Product_Bill> list,Context ctx) {
        this.product_Bills = list;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public detailInvoiceholber onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_itemlist_invoice_details,parent,false);
        return new detailInvoiceholber(view);
    }

    @Override
    public void onBindViewHolder(@NonNull detailInvoiceholber holder, int position) {
            holder.tv_product_name.setText( product_Bills.get(position).getProductname());
            holder.tv_product_quantity.setText(product_Bills.get(position).getQuantityOfSold()+" ");
            holder.tv_product_sellPrice.setText(product_Bills.get(position).getPriceOfSold()+" ");
            holder.tv_product_total_product.setText(product_Bills.get(position).getTotalpriceForproduct()+" ");

    }

    @Override
    public int getItemCount() {
        return product_Bills.size();
    }






    public class detailInvoiceholber extends RecyclerView.ViewHolder{
TextView tv_product_name, tv_product_quantity, tv_product_sellPrice, tv_product_total_product;
        public detailInvoiceholber(@NonNull View itemView) {
            super(itemView);
            tv_product_name = itemView.findViewById(R.id.invoice_detail_product_name);
            tv_product_quantity = itemView.findViewById(R.id.invoice_detail_product_quantity);
            tv_product_sellPrice = itemView.findViewById(R.id.invoice_detail_product_sellPrice);
            tv_product_total_product = itemView.findViewById(R.id.invoice_detail_product_total_product);
        }
    }
}
