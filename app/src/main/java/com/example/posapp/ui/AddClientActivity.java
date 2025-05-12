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

import java.util.ArrayList;

public class AddClientActivity extends AppCompatActivity {
private TextView back_icon;
private Button add_client;
private ArrayList<String> clients_phone;
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
clients_phone = new ArrayList<>();

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
               if (Client_phone.length() != 10 ){
                   throw new NullPointerException();
               }
               Client client = new Client(name,store,Client_phone);
               db.open();
               clients_phone = db.getClientsPhone();

               if (clients_phone.contains(client.getPhone_number())){

                   throw new ClassNotFoundException();
               }else {
                   db.addClient(client);
               }

            db.close();
               Toast.makeText(AddClientActivity.this, R.string.addClints_msg_succ, Toast.LENGTH_SHORT).show();
               finish();
           }

        }catch (Exception e){
            if (e instanceof NullPointerException){
                Toast.makeText(AddClientActivity.this, "رقم الهاتف غير صحيح!", Toast.LENGTH_SHORT).show();
            } else if (e instanceof ClassNotFoundException) {
                Toast.makeText(AddClientActivity.this, " يوجد هذا الرقم مسبقا لزبون اخر!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(AddClientActivity.this, R.string.addClints_msg_fail, Toast.LENGTH_SHORT).show();
            }
            }
    }
});

    }
}