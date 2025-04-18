package com.example.posapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.posapp.database.DataBaseControler;
import com.example.posapp.model.Inventory;
import com.example.posapp.model.MyRVadapter;
import com.example.posapp.model.OnClickStockItem;
import com.example.posapp.ui.Edit_delete_product;

import java.util.ArrayList;

public class StockActivity extends AppCompatActivity {
private TextView toolbar_icon;
private RecyclerView rv_products_list;
private DataBaseControler db;
private ArrayList<Inventory> products;
private MyRVadapter adapter;
private Intent to_Edit_or_delete;
public static final String PRODUCT_ID = "PRODUCT_ID";
private int id = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_stock);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
toolbar_icon = findViewById(R.id.stock_toolbar_back_icon);
rv_products_list = findViewById(R.id.products_list);


toolbar_icon.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

        finish();
    }
});

products = new ArrayList<>();
db = DataBaseControler.getInstance(getBaseContext());

db.open();
    products = db.getAllProductsIntoInventory();
db.close();
if (!(products.isEmpty())) {
    adapter = new MyRVadapter(products, new OnClickStockItem() {
        @Override
        public void OnClickItem(int position) {
            id = position;
            to_Edit_or_delete = new Intent(StockActivity.this, Edit_delete_product.class);
            to_Edit_or_delete.putExtra(PRODUCT_ID,id);
            launcher.launch(to_Edit_or_delete);
            id=-1;

        }
    });
    rv_products_list.setAdapter(adapter);
    rv_products_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
}else {
    Toast.makeText(this, "لاتوجد منتجات", Toast.LENGTH_SHORT).show();
}

    }

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult()
            , new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult data) {
                    db.open();
                      products = db.getAllProductsIntoInventory();
                    db.close();
                    adapter.updatedata(products);
                }
            });


}