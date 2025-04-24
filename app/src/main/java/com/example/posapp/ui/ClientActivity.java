package com.example.posapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.posapp.R;
import com.example.posapp.database.DataBaseControler;
import com.example.posapp.model.Client;
import com.example.posapp.model.OnClientClick;
import com.example.posapp.model.RVAdapterforClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ClientActivity extends AppCompatActivity {
private RecyclerView rv_client;
private Toolbar toolbar;
private Intent to_addclient;
private SearchView search_client;
private TextView back_icon;
private RVAdapterforClient adapter;
private ArrayList<Client> clients ;
private FloatingActionButton fab_add_client;
private DataBaseControler db;
private Intent to_edit_or_delete_client;
private int client_id;
private String CLIENT_ID = "CLIENT_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_client);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

try {

    rv_client = findViewById(R.id.recyclerView);
    fab_add_client = findViewById(R.id.fab_add_client);
    back_icon = findViewById(R.id.client_toolbar_back_icon);
    search_client = findViewById(R.id.client_search);


       search_client.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
           @Override
           public boolean onQueryTextSubmit(String s) {
               db.open();
               ArrayList<Client> response = db.searchClient(s);
               db.close();

               if (response != null && !response.isEmpty()) {
                   adapter.newData(response);
               }else {
                   Toast.makeText(ClientActivity.this,R.string.clients_msg_noClients,Toast.LENGTH_SHORT).show();
               }
               return true;
           }

           @Override
           public boolean onQueryTextChange(String s) {
               db.open();
                 ArrayList<Client> response = db.searchClient(s);
               db.close();
               adapter.newData(response);
               return true;
           }
       });

       search_client.setOnCloseListener(new SearchView.OnCloseListener() {
           @Override
           public boolean onClose() {

               db.open();
               clients  = db.getAllClients();
               db.close();
               adapter.newData(clients);

               return true;
           }
       });

    clients = new ArrayList<>();
    db = DataBaseControler.getInstance(this);

    db.open();
    clients = db.getAllClients();
    db.close();


    adapter = new RVAdapterforClient(clients, this, new OnClientClick() {
        @Override
        public void onClick(int id) {

            to_edit_or_delete_client = new Intent(ClientActivity.this, Edit_delete_client.class);
            to_edit_or_delete_client.putExtra(CLIENT_ID,id);
            launcher.launch(to_edit_or_delete_client);
        }
    });
    rv_client.setLayoutManager(new LinearLayoutManager(this));
    rv_client.setAdapter(adapter);

    fab_add_client.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
           to_addclient = new Intent(ClientActivity.this,AddClientActivity.class);
           launcher.launch(to_addclient);

        }
    });
    back_icon.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    });
} catch (Exception e) {
    Log.e("calintActivity Ex",e.getMessage());
}

    }

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult()
            , new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                   db.open();
                   clients = db.getAllClients();
                   db.close();
                   adapter.newData(clients);
                }
            });
}