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

public class RVAdapterforClient extends RecyclerView.Adapter<RVAdapterforClient.Clientholder> {

    private ArrayList<Client> clients = new ArrayList<>();
    private Context ctx;

   public RVAdapterforClient(ArrayList<Client> clients , Context ctx){
    this.clients = clients;
    this.ctx = ctx;
}

    @NonNull
    @Override
    public Clientholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_rv_client,parent,false);
        return new Clientholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Clientholder holder, int position) {
            Client c= clients.get(position);
             holder.tv_client_name.setText(c.getFullname());
             holder.tv_client_phone.setText(c.getPhone_number());
             holder.tv_client_store.setText(c.getStore_name());
             holder.tv_client_name.setTag(c.getId());
    }

    @Override
    public int getItemCount() {
        return clients.size();
    }


    public class Clientholder extends RecyclerView.ViewHolder{

        TextView tv_client_name,tv_client_phone,tv_client_store;
     public Clientholder(@NonNull View itemView) {
         super(itemView);
         tv_client_name=itemView.findViewById(R.id.fullname_client);
         tv_client_phone=itemView.findViewById(R.id.phone_client);
         tv_client_store=itemView.findViewById(R.id.store_client);

     }
 }

 public void newData(ArrayList<Client> c){
      this.clients = c;
      this.notifyDataSetChanged();

 }
}
