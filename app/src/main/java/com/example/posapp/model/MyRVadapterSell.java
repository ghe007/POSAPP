package com.example.posapp.model;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.posapp.R;
import com.example.posapp.ui.SellActivity;

import java.util.ArrayList;

public class MyRVadapterSell extends RecyclerView.Adapter<MyRVadapterSell.Myholder> {
    private ArrayList<Product> product = new ArrayList<>();
    private double totalprice =0.0;
    private Context context;
    private SellListClick sellListClick;
  private ArrayList<Double> total_all = new ArrayList<>();
    public MyRVadapterSell(ArrayList<Product> product, Context ctx,SellListClick sellListClick) {
        this.sellListClick = sellListClick;
        this.product = product;
        this.context = ctx;
    }

    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sell_custom_layout, parent, false);
        return new Myholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Myholder holder, int position) {
        int pos = position;
        double defualtprice = product.get(pos).getPrice_of_sell();
       double defualtquantity = product.get(pos).getQuantity_of_sell();

        holder.name.setText(String.valueOf(product.get(position).getProduct_name()));
        holder.price.setText(String.valueOf(product.get(position).getPrice_of_sell()));
        holder.quantity.setText(String.valueOf(product.get(position).getQuantity_of_sell()));
        holder.total.setText(String.valueOf(product.get(position).getQuantity_of_sell() * product.get(position).getPrice_of_sell()));

        //total_price_from_tv_total = Double.parseDouble(holder.total.getText().toString());
        //totalprice += Double.parseDouble(String.valueOf(holder.total.getText()));
        Toast.makeText(context, "hello", Toast.LENGTH_SHORT).show();

        holder.pos = pos;

        holder.price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable != null && !editable.toString().isEmpty()) {

                    product.get(pos).setPrice_of_sell(Double.parseDouble(editable.toString()));
                    holder.total.setText(String.valueOf(product.get(pos).getQuantity_of_sell() * product.get(pos).getPrice_of_sell()));

                } else {
                    product.get(pos).setPrice_of_sell(defualtprice);
                    holder.total.setText(String.valueOf(product.get(pos).getQuantity_of_sell() * product.get(pos).getPrice_of_sell()));
                }
            }
        });
        holder.quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable != null && !editable.toString().isEmpty()) {

                    product.get(pos).setQuantity_of_sell(Integer.parseInt(editable.toString()));
                    holder.total.setText(String.valueOf(product.get(pos).getQuantity_of_sell() * product.get(pos).getPrice_of_sell()));
//                    totalprice +=Double.parseDouble(String.valueOf((product.get(pos).getQuantity_of_sell()-1)*product.get(pos).getPrice_of_sell()));
//                    setTotalprice(tot
//                    alprice);
//                    Toast.makeText(context, totalprice+"", Toast.LENGTH_SHORT).show();
                } else {
                    product.get(pos).setQuantity_of_sell(1);
                    holder.total.setText(String.valueOf(product.get(pos).getQuantity_of_sell() * product.get(pos).getPrice_of_sell()));
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return product.size();
    }


    public boolean addsell_item(Product p) {
        for (int i = 0; i < product.size(); i++) {
            if (product.get(i).getId() == p.getId()) {
                Toast.makeText(context, "المنتج موجود بالفعل!", Toast.LENGTH_SHORT).show();
              //  product.get(i).setQuantity_of_sell(product.get(i).getQuantity_of_sell() + 1);
             //  totalpriceforExistsitem(i);
                //this.notifyDataSetChanged();

                return false;
            }
        }
        this.product.add(p);
        int pos = product.indexOf(p);
        setTotalpricefornewitem(pos);
        this.notifyDataSetChanged();
        return true;
    }

    public void totalpriceforExistsitem(int pos){
        totalprice += product.get(pos).getPrice_of_sell() * (product.get(pos).getQuantity_of_sell()-1);
        //Toast.makeText(context, totalprice + "", Toast.LENGTH_SHORT).show();
        //totalprice += total_price_from_tv_total;
    }
    public void setTotalpricefornewitem(int pos){
      totalprice += product.get(pos).getPrice_of_sell() * product.get(pos).getQuantity_of_sell();
     // Toast.makeText(context, totalprice + "", Toast.LENGTH_SHORT).show();
       // totalprice +=total_price_from_tv_total;
   }
    public void updatetotalafterRemove(int id){
      double removeprice = Double.parseDouble(String.valueOf(product.get(id).getPrice_of_sell()*(product.get(id).getQuantity_of_sell())));
        totalprice -= removeprice;

       this.notifyDataSetChanged();
   }
   public double getTotalprice(){

this.notifyDataSetChanged();
       return totalprice;
   }
public void setTotalprice(double totalprice){
        this.totalprice = totalprice;
}
   public class Myholder extends RecyclerView.ViewHolder {
        TextView name;
        EditText price;
        EditText quantity;
        EditText total;
        int pos;

        public Myholder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.sell_product_name);
            price = itemView.findViewById(R.id.sell_product_price_of_sell);
            quantity = itemView.findViewById(R.id.sell_product_quantity);
            total = itemView.findViewById(R.id.sell_product_total_price);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    sellListClick.OnitemClicked(pos);


                    return true;
                }
            });


        }
    }
}
