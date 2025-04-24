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
import com.example.posapp.model.Inventory;
import com.example.posapp.model.Product;

public class Edit_delete_product extends AppCompatActivity {
    private TextView back_btn;
    private TextView product_id;
    private EditText product_parcode;
    private EditText product_name;
    private EditText product_price_of_buy;
    private EditText product_price_of_sell;
    private EditText edit_product_quantity;
    private Button edit_product;
    private Button delete_product;
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
        edit_product_quantity = findViewById(R.id.edit_product_quantity);

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
       Inventory inventory = db.getQuantity(id);
       db.close();
        product_parcode.setText(product.getBarcode());
        product_name.setText(product.getProduct_name());
        product_price_of_buy.setText(product.getPrice_of_buy()+"");
        product_price_of_sell.setText(product.getPrice_of_sell()+"");

        edit_product_quantity.setText(inventory.getQuantity()+"");

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
                  String quantity = edit_product_quantity.getText().toString();

                  if (parcode.isEmpty()|| name.isEmpty()|| price_of_buy.isEmpty() || price_of_sell.isEmpty()|| quantity.isEmpty()){
                      Toast.makeText(Edit_delete_product.this, "بيانات غير كاملة !", Toast.LENGTH_SHORT).show();
                  }else{
                      double price_buy = Double.parseDouble((price_of_buy.toString()));
                      double price_sell = Double.parseDouble(price_of_sell.toString());
                      int update_quantity = Integer.parseInt(quantity);
                      Product product1 = new Product(name,parcode,price_buy,price_sell);
                      Inventory inventory = new Inventory();
                      inventory.setQuantity(update_quantity);
                      product1.setId(id);
                       db.updateProduct(product1,inventory.getQuantity());
                      Toast.makeText(Edit_delete_product.this, "تم تعديل منتج", Toast.LENGTH_SHORT).show();
                      finish();
                  }


            }
        });

    }
}