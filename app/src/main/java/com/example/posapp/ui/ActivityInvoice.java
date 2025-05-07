package com.example.posapp.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SearchView;
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

import com.example.posapp.R;
import com.example.posapp.database.DataBaseControler;
import com.example.posapp.model.Invoice;
import com.example.posapp.model.MyRV_Invoice_adapter;
import com.example.posapp.model.OnClickStockItem;

import java.util.ArrayList;

public class ActivityInvoice extends AppCompatActivity {
private TextView batck_btn;
private SearchView search_invoice;
private RecyclerView invoice_rv;
private MyRV_Invoice_adapter adapter;
private ArrayList<Invoice> invoices;
public static final String bill_id ="Bill_id",client_name="client_name",client_phone="client_phone",client_store="client_store";
private DataBaseControler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_invoice);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        batck_btn=findViewById(R.id.invoice_toolbar_back_icon);
        search_invoice=findViewById(R.id.client_search);
        invoice_rv=findViewById(R.id.recyclerView);
        db = DataBaseControler.getInstance(this);
        try {
            invoices = new ArrayList<>();

            db.open();
            invoices = db.getAllBills();
            db.close();
            //getAllBills(invoices);
            adapter = new MyRV_Invoice_adapter(this, invoices, new OnClickStockItem() {
                @Override
                public void OnClickItem(int position) {
                    Intent intent = new Intent(ActivityInvoice.this, Invoice_detalis.class);
                    intent.putExtra(bill_id, invoices.get(position).getId());
                    intent.putExtra(client_name, invoices.get(position).getClient_name());
                    intent.putExtra(client_phone, invoices.get(position).getPhone_number());
                    intent.putExtra(client_store, invoices.get(position).getStore_name());
                    launcher.launch(intent);
                }
            });
            invoice_rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            invoice_rv.setAdapter(adapter);
        }catch (Exception e){
            Toast.makeText(this, e.getMessage()+"", Toast.LENGTH_SHORT).show();
        }


        batck_btn.setOnClickListener(view -> {
            finish();
        });

        

    }
public void getAllBills(ArrayList<Invoice> invoices){

        db.open();
        invoices = db.getAllBills();
        db.close();

        for (Invoice i : invoices){
            Toast.makeText(this, i.getId()+"", Toast.LENGTH_SHORT).show();
        }
 if (invoices.isEmpty()){
     Toast.makeText(this, "لاتوجد فواتير!", Toast.LENGTH_SHORT).show();
 }
}
ActivityResultLauncher<Intent> launcher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {
                db.open();
                invoices = db.getAllBills();
                db.close();
                adapter.notifyDataSetChanged();
            }
        }
);
}