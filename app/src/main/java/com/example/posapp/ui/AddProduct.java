package com.example.posapp.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.widget.TextViewCompat;

import com.example.posapp.R;
import com.example.posapp.database.DataBaseControler;
import com.example.posapp.model.Inventory;
import com.example.posapp.model.Product;
import com.google.android.material.textfield.TextInputEditText;

public class AddProduct extends AppCompatActivity {
private Toolbar toolbar;
private TextView toolbar_text , toolbar_back_icon;
private TextInputEditText product_name , product_barcode , product_price_of_buy , product_price_of_sell , product_quantity;
private Button product_add_btn;
private Product newproduct;
private Inventory newinventory;
private DataBaseControler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.add_product), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

product_add_btn = findViewById(R.id.product_add_btn);
toolbar_back_icon = findViewById(R.id.toolbar_back_icon);

product_name = findViewById(R.id.product_name);
product_barcode = findViewById(R.id.product_barcode);
product_quantity = findViewById(R.id.product_quantity);
product_price_of_buy = findViewById(R.id.product_price_of_buy);
product_price_of_sell = findViewById(R.id.product_price_of_sell);

toolbar_back_icon.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        finish();
    }
});
product_add_btn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

            String name =  product_name.getText().toString();
            String barcode =  product_barcode.getText().toString();
            String quantity = product_quantity.getText().toString();
            String price_of_buy = product_price_of_buy.getText().toString();
            String price_of_sell = product_price_of_sell.getText().toString();

            if (name.isEmpty() || barcode.isEmpty() || quantity.isEmpty() || price_of_buy.isEmpty()|| price_of_sell.isEmpty()){
                Toast.makeText(AddProduct.this, "الرجاء ملئ جميع الحقول", Toast.LENGTH_SHORT).show();
            }

            try {
                int quantity_int = Integer.parseInt(quantity);
                double price_of_buy_double = Double.parseDouble(price_of_buy);
                double price_of_sell_double = Double.parseDouble(price_of_sell);

            newproduct = new Product(name,barcode,price_of_buy_double,price_of_sell_double);
            db = DataBaseControler.getInstance(getBaseContext());
            db.open();
             boolean result =  db.addProduct(newproduct);

               if (result){
                   Toast.makeText(AddProduct.this, "تم اضافة المنتج", Toast.LENGTH_SHORT).show();
                   newinventory = new Inventory(newproduct.getId(),quantity_int);
               boolean result_inventory =  db.insertNewProductIntoInventory(newinventory.getQuantity(),newinventory.getProductID());
               if (result_inventory){
                   Toast.makeText(AddProduct.this, "تم اضافة المنتج الى المخزون", Toast.LENGTH_SHORT).show();
               }
               }
        } catch (Exception e) {
            Log.e("addproduct", e.getMessage());
            Toast.makeText(AddProduct.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
});




    }

}