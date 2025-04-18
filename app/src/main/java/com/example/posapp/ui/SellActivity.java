package com.example.posapp.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.posapp.R;
import com.example.posapp.database.DataBaseControler;

import com.example.posapp.model.MyRVadapterSell;
import com.example.posapp.model.Product;
import com.example.posapp.model.SellListClick;

import java.util.ArrayList;

public class SellActivity extends AppCompatActivity {
    private TextView sell_back_btn;
    private double total_price;
    private TextView total_price_tv;
    private RecyclerView sell_rv;
    private MyRVadapterSell myRVadapterSell;
    private ArrayList<Product> rv_items;
    private AutoCompleteTextView searchtext;
    private ArrayAdapter<String> search_adapter;
    private ArrayList<String> names_list_forsearch;
    private DataBaseControler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sell);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        sell_back_btn = findViewById(R.id.sell_toolbar_back_icon);
        searchtext = findViewById(R.id.autoCompleteTextView);
        sell_rv = findViewById(R.id.sell_rv);
        total_price_tv = findViewById(R.id.sell_total);

        rv_items = new ArrayList<>();
        myRVadapterSell = new MyRVadapterSell(rv_items, this, new SellListClick() {
            @Override
            public void OnitemClicked(int id) {
                myRVadapterSell.updatetotalafterRemove(id);
                rv_items.remove(id);
                total_price = myRVadapterSell.getTotalprice();
                total_price_tv.setText(String.valueOf(total_price));
                myRVadapterSell.notifyDataSetChanged();
            }
        });

        sell_rv.setLayoutManager(new LinearLayoutManager(this));
        sell_rv.setAdapter(myRVadapterSell);

        sell_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        db = DataBaseControler.getInstance(this);
        db.open();
        names_list_forsearch = new ArrayList<>();
        names_list_forsearch = db.getProductsName();
        db.close();
        search_adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, names_list_forsearch);
        searchtext.setThreshold(1);
        searchtext.setAdapter(search_adapter);

        searchtext.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    String name = (String) adapterView.getItemAtPosition(i);
                    db.open();
                     Product p = db.getProductByname(name);
                    db.close();
                    searchtext.setText("");
                    addProducttollist(p);

                } catch (Exception e) {
                    Toast.makeText(SellActivity.this, "autocomplete text view ", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void addProducttollist(Product p) {

        myRVadapterSell.addsell_item(p);
        total_price = myRVadapterSell.getTotalprice();

        myRVadapterSell.notifyDataSetChanged();
        total_price_tv.setText(String.valueOf(total_price));
    }
}