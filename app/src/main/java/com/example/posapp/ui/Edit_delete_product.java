package com.example.posapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.posapp.R;
import com.example.posapp.StockActivity;
import com.example.posapp.database.DataBaseControler;
import com.example.posapp.model.Product;

public class Edit_delete_product extends AppCompatActivity {
TextView back_btn;
TextView product_id;
EditText product_parcode;
EditText product_name;
EditText product_price_of_buy;
EditText product_price_of_sell;
Button edit_product;
Button delete_product;
private Intent recive_id;
private DataBaseControler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_delete_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        back_btn = findViewById(R.id.back_btn);
        product_id = findViewById(R.id.edit_product_id);

        product_parcode = findViewById(R.id.edit_product_parcode);
        product_name = findViewById(R.id.edit_product_name);
        product_price_of_buy = findViewById(R.id.edit_product_price_of_buy);
        product_price_of_sell = findViewById(R.id.edit_product_price_of_sell);

        edit_product = findViewById(R.id.edit_update_btn);
        delete_product = findViewById(R.id.edit_delete_btn);
        db = DataBaseControler.getInstance(this);


        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

       recive_id = getIntent();
       int id = recive_id.getIntExtra(StockActivity.PRODUCT_ID,-1);
       product_id.setText(String.valueOf(id));

       db.open();
       Product product = db.getProductById(id);
       db.close();
        product_parcode.setText(product.getBarcode());
        product_name.setText(product.getProduct_name());
        product_price_of_buy.setText(product.getPrice_of_buy()+"");
        product_price_of_sell.setText(product.getPrice_of_sell()+"");


        delete_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.open();
                  int result = db.deleteProdut(product.getId());
                db.close();
                Toast.makeText(Edit_delete_product.this, "تم حذف المنتج", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        edit_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.open();
                  String parcode  = product_parcode.getText().toString();
                  String name = product_name.getText().toString();
                  String price_of_buy = product_price_of_buy.getText().toString();
                  String price_of_sell = product_price_of_sell.getText().toString();

                  if (parcode== "" || name == "" || price_of_buy == "" || price_of_sell == ""){
                      Toast.makeText(Edit_delete_product.this, "بيانات غير كاملة !", Toast.LENGTH_SHORT).show();
                  }else {
                      double price_buy = Double.parseDouble((price_of_buy.toString()));
                      double price_sell = Double.parseDouble(price_of_sell.toString());
                      Product product1 = new Product(name,parcode,price_buy,price_sell);
                      product1.setId(id);
                       db.updateProduct(product1);

                  }
                  finish();
                Toast.makeText(Edit_delete_product.this, "تم تعديل منتج", Toast.LENGTH_SHORT).show();
            }
        });

    }
}