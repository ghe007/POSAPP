package com.example.posapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.posapp.R;
import com.example.posapp.StockActivity;
import com.example.posapp.database.DataBaseControler;
import com.example.posapp.database.Mydatabase;

public class MainActivity extends AppCompatActivity {
private CardView product_card;
private CardView inventory_card;
private CardView bill_card;
private Toolbar toolbar;
private Intent to_addproduct;
private CardView main_card_client;
private Intent to_clients;
private CardView main_card_sell;
private Intent to_stock;
private Intent to_sell,to_bill;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

      toolbar = findViewById(R.id.main_toolbar);
        product_card = findViewById(R.id.main_card);
        inventory_card = findViewById(R.id.main_card_inventory);
        main_card_sell = findViewById(R.id.main_card_sell);
        main_card_client = findViewById(R.id.main_card_clients);
        bill_card = findViewById(R.id.main_card_invoice);
        setSupportActionBar(toolbar);


        product_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            to_addproduct = new Intent(MainActivity.this,AddProduct.class);
            launcher.launch(to_addproduct);
            }
        });


inventory_card.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
     try {

         to_stock = new Intent(MainActivity.this, StockActivity.class);
         launcher.launch(to_stock);
     } catch (Exception e) {
         Log.e("onlickmain",e.getMessage());
     }

    }
});

        main_card_sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                to_sell  =new Intent(MainActivity.this,SellActivity.class);
                launcher.launch(to_sell);
            }
        });
        main_card_client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               try {
                   to_clients = new Intent(MainActivity.this,ClientActivity.class);
                   launcher.launch(to_clients);
               }catch (Exception e){
                   Toast.makeText(MainActivity.this, "prblm in main", Toast.LENGTH_SHORT).show();
                   Log.e("to_client",e.getMessage());
               }

            }
        });
bill_card.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        to_bill = new Intent( MainActivity.this,ActivityInvoice.class);
        launcher.launch(to_bill);
    }
});
    }


    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {

                }
            });
}