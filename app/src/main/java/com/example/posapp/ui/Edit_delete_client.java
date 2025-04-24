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
import com.example.posapp.database.DataBaseControler;
import com.example.posapp.model.Client;

public class Edit_delete_client extends AppCompatActivity {
private TextView back_btn , tv_id;
private EditText client_name,client_phone,client_store;
private Button save_btn,delete_btn;
private Intent data;
private DataBaseControler db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_delete_client);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        back_btn = findViewById(R.id.back_btn);
        tv_id = findViewById(R.id.edit_client_id);
        client_name = findViewById(R.id.edit_client_name);
        client_phone = findViewById(R.id.edit_client_phone);
        client_store = findViewById(R.id.edit_client_store_name);
        save_btn = findViewById(R.id.edit_update_btn);
        delete_btn = findViewById(R.id.edit_delete_btn);
        db = DataBaseControler.getInstance(this);


        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        int id = getIntent().getIntExtra("CLIENT_ID",-1);
        tv_id.setText(id+"");
        db.open();
           Client client = db.getClientByID(id);
        db.close();

        client_name.setText(client.getFullname());
        client_phone.setText(client.getPhone_number());
        client_store.setText(client.getStore_name());

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String new_name = client_name.getText().toString();
                String new_phone = client_phone.getText().toString();
                String new_store = client_store.getText().toString();
                if (new_name.isEmpty()|| new_store.isEmpty() || new_phone.isEmpty()){
                    Toast.makeText(Edit_delete_client.this, "بيانات غير كاملة !", Toast.LENGTH_SHORT).show();
                }else {
                    client.setFullname(new_name);
                    client.setPhone_number(new_phone);
                    client.setStore_name(new_store);
                    db.open();
                    db.updateClient(client);
                    db.close();
                    Toast.makeText(Edit_delete_client.this, "تم تحديث الزبون بنجاح", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });


        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.open();
                 if (db.deleteClient(id)){
                     Toast.makeText(Edit_delete_client.this, "تم حذف الزبون بنجاح", Toast.LENGTH_SHORT).show();
                     finish();
                 }else {
                     Toast.makeText(Edit_delete_client.this, "حدث خطأ أثناء حذف الزبون!", Toast.LENGTH_SHORT).show();
                 }
                db.close();

            }
        });
    }
}