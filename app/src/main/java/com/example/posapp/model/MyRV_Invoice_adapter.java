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

public class MyRV_Invoice_adapter extends RecyclerView.Adapter<MyRV_Invoice_adapter.invoiceholder> {
private Context ctx;
private ArrayList<Invoice> invoices = new ArrayList<>();
private OnClickStockItem onClickStockItem;

    public MyRV_Invoice_adapter(Context ctx , ArrayList<Invoice> invoices, OnClickStockItem onClickStockItem){
        this.onClickStockItem = onClickStockItem;
        this.ctx = ctx;
        this.invoices = invoices;
    }

    @NonNull
    @Override
    public invoiceholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_layout_invoice_rv_item, parent, false);
        invoiceholder holder = new invoiceholder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull invoiceholder holder, int position) {

              holder.tv_invoice_id.setText(invoices.get(position).getId()+"");
              holder.tv_invoice_client.setText(invoices.get(position).getClient_name());
              holder.tv_invoice_total.setText(invoices.get(position).getTotal_price()+"");
              holder.tv_invoice_date.setText(invoices.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return invoices.size();
    }

    public class invoiceholder extends RecyclerView.ViewHolder {
        TextView tv_invoice_id, tv_invoice_client, tv_invoice_total, tv_invoice_date;
        public invoiceholder(@NonNull View itemView) {
            super(itemView);
            tv_invoice_id = itemView.findViewById(R.id.invoice_id);
            tv_invoice_client = itemView.findViewById(R.id.invoice_client);
            tv_invoice_total = itemView.findViewById(R.id.invoice_total);
            tv_invoice_date = itemView.findViewById(R.id.invoice_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickStockItem.OnClickItem(getAdapterPosition());
                }
        });
            }
    }
}
