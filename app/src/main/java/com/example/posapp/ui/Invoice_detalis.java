package com.example.posapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.example.posapp.model.Invoice;
import com.example.posapp.model.Product_Bill;
import com.example.posapp.model.RV_Invoice_detail;

import java.util.ArrayList;

public class Invoice_detalis extends AppCompatActivity {

    private RecyclerView rv;
    private RV_Invoice_detail adapter;
    private TextView tv_total;
    private TextView back_btn,client_name,client_phone,client_store,bill_date;
    private DataBaseControler db;
    private ArrayList<Product_Bill> data_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_invoice_detalis);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

       rv = findViewById(R.id.bill_detail_list);
       tv_total = findViewById(R.id.invoice_detail_total);
        client_name = findViewById(R.id.invoice_detail_client_name);
        client_phone = findViewById(R.id.invoice_detail_client_phone);
        client_store = findViewById(R.id.invoice_detail_client_store);
        bill_date = findViewById(R.id.invoice_detail_bill_date);
       back_btn = findViewById(R.id.back_btn);
       back_btn.setOnClickListener(view -> {
           finish();
       });
        db = DataBaseControler.getInstance(this);
       try {

         Intent recived_data = getIntent();
         int bill_id = recived_data.getIntExtra(ActivityInvoice.bill_id,-1);
          db = DataBaseControler.getInstance(this);
           db.open();
           data_list = db.getAllBillDetails(bill_id);
       tv_total.setText(data_list.get(0).getTotalpriceForbill()+"");
       bill_date.setText(data_list.get(0).getBilldate());
       client_name.setText(recived_data.getStringExtra(ActivityInvoice.client_name));
       client_phone.setText(recived_data.getStringExtra(ActivityInvoice.client_phone));
       client_store.setText(recived_data.getStringExtra(ActivityInvoice.client_store));
           db.close();

           adapter = new RV_Invoice_detail(data_list,this);
           rv.setLayoutManager(new LinearLayoutManager(this));
           rv.setAdapter(adapter);

       }catch (Exception e){
           Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
           Log.e("cating",e.getMessage());
       }

    }
}