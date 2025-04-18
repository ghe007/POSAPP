package com.example.posapp.ui;

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

public class AddClientActivity extends AppCompatActivity {
private TextView back_icon;
private Button add_client;
private EditText client_name;
private EditText phone;
private EditText store_name;
private DataBaseControler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_client);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        back_icon = findViewById(R.id.addclient_toolbar_back_icon);
        add_client = findViewById(R.id.add_client_client_add_btn);
        client_name = findViewById(R.id.add_client_client_name);
        phone = findViewById(R.id.add_client_client_phone);
        store_name = findViewById(R.id.add_client_client_store_name);
db = DataBaseControler.getInstance(AddClientActivity.this);

back_icon.setOnClickListener(v -> {
    finish();
});

add_client.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        try {
           String name = client_name.getText().toString().trim();
           String Client_phone = phone.getText().toString().trim();
           String store = store_name.getText().toString().trim();

           if (name.isEmpty() || Client_phone.isEmpty() || store.isEmpty()){
               throw  new Exception();
           }else {
               Client client = new Client(name,store,Client_phone);
               db.open();
            db.addClient(client);
            db.close();
               Toast.makeText(AddClientActivity.this, "تم اضافة الزبون بنجاح", Toast.LENGTH_SHORT).show();
           }
        }catch (Exception e){
            Toast.makeText(AddClientActivity.this, "معلومات خاطئة", Toast.LENGTH_SHORT).show();
        }
    }
});

    }
}