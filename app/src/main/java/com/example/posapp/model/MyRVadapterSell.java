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
import com.example.posapp.database.DataBaseControler;
import com.example.posapp.ui.SellActivity;

import java.util.ArrayList;

public class MyRVadapterSell extends RecyclerView.Adapter<MyRVadapterSell.Myholder> {
    private ArrayList<Product> product = new ArrayList<>();
    private Context context;
    private SellListClick sellListClick;
    private OnProductChangeListner updatetotal;
    private DataBaseControler db;
public interface OnProductChangeListner{
    void onProductListChangeListner(ArrayList<Product> updatedList);
}
    public MyRVadapterSell(ArrayList<Product> product, Context ctx,SellListClick sellListClick,OnProductChangeListner listner) {
        this.sellListClick = sellListClick;
        this.product = product;
        this.context = ctx;
        this.updatetotal = listner;
    }

    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sell_custom_layout, parent, false);
        return new Myholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Myholder holder, int position) {
        holder.pos = (position);
        holder.name.setText(String.valueOf(product.get(position).getProduct_name()));
        holder.price.setText(String.valueOf(product.get(position).getPrice_of_sell()));
        holder.quantity.setText(String.valueOf(product.get(position).getQuantity_of_sell()));
        holder.total.setText(String.valueOf(product.get(position).getQuantity_of_sell() * product.get(position).getPrice_of_sell()));



    }

    @Override
    public int getItemCount() {
        return product.size();
    }

    public ArrayList<Product> getproducts() {
    return product;
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
          TextWatcher textWatcherforprice = new TextWatcher() {
              @Override
              public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

              }

              @Override
              public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

              }

              @Override
              public void afterTextChanged(Editable editable) {
                 calculateTotalForPriceTexTwatcher();
              }
          };
            TextWatcher textWatcherforquantity = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    calculateTotalForQuantityTextWatcher();
                }
            };

            price.addTextChangedListener(textWatcherforprice);
            quantity.addTextChangedListener(textWatcherforquantity);


        }
        private  void calculateTotalForPriceTexTwatcher(){
            try{

                int quant = Integer.parseInt(quantity.getText().toString());
                double pric = Double.parseDouble(price.getText().toString());
                double _total = quant*pric;
                db = DataBaseControler.getInstance(context);
                db.open();
                 int quantityInInventory = db.getquantityInInventory(product.get(getAdapterPosition()).getId());
                db.close();
                total.setText(String.valueOf(_total));

                product.get(getAdapterPosition()).setQuantity_of_sell(quant);
                product.get(getAdapterPosition()).setPrice_of_sell(pric);
                product.get(getAdapterPosition()).setTotal_price(Double.parseDouble(total.getText().toString()));

                if (updatetotal != null){
                    updatetotal.onProductListChangeListner(product);
                }
            }catch (Exception e){
                total.setText("خطأ في البيانات!");
               // quantity.setText(product.get(getAdapterPosition()).getQuantity_of_sell());


            }
        }
        private  void calculateTotalForQuantityTextWatcher(){
            try{

                int quant = Integer.parseInt(quantity.getText().toString());
                double pric = Double.parseDouble(price.getText().toString());
                double _total = quant*pric;
                db = DataBaseControler.getInstance(context);
                db.open();
                int quantityInInventory = db.getquantityInInventory(product.get(getAdapterPosition()).getId());
                db.close();
                total.setText(String.valueOf(_total));
                if (quant > quantityInInventory){
                    Toast.makeText(context, "لا يمكن تجاوز الكمية الموجودة في المخزون!", Toast.LENGTH_SHORT).show();
                    SellActivity.totalError();
                    throw  new ArrayIndexOutOfBoundsException();
                }
                product.get(getAdapterPosition()).setQuantity_of_sell(quant);
                product.get(getAdapterPosition()).setPrice_of_sell(pric);
                product.get(getAdapterPosition()).setTotal_price(Double.parseDouble(total.getText().toString()));

                if (updatetotal != null){
                    updatetotal.onProductListChangeListner(product);
                }
            }catch (Exception e){
                total.setText("خطأ في البيانات!");
                // quantity.setText(product.get(getAdapterPosition()).getQuantity_of_sell());


            }
        }
    }
}
