package com.example.posapp.ui;

import android.content.Intent;
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

import com.example.posapp.model.Client;
import com.example.posapp.model.Invoice;
import com.example.posapp.model.MyRVadapterSell;
import com.example.posapp.model.Product;
import com.example.posapp.model.SellListClick;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SellActivity extends AppCompatActivity {
    private TextView sell_back_btn;
    private static double total_price;
    private static TextView total_price_tv;
    private RecyclerView sell_rv;
    private MyRVadapterSell myRVadapterSell;
    private ArrayList<Product> rv_items;
    private AutoCompleteTextView searchtext,searchClients;
    private ArrayAdapter<String> search_adapter,search_client;
    private ArrayList<String> names_list_forsearch,clients_for_search;
    private DataBaseControler db;
    private Button sell,add_newClient;

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
        searchClients = findViewById(R.id.search_clients);
        add_newClient = findViewById(R.id.add_newClient_btn);
        sell_rv = findViewById(R.id.sell_rv);
        total_price_tv = findViewById(R.id.sell_total);
        sell = findViewById(R.id.btn_sell);



        total_price_tv.setText(total_price+"");
        rv_items = new ArrayList<>();
        myRVadapterSell = new MyRVadapterSell(rv_items, this, new SellListClick() {
            @Override
            public void OnitemClicked(int id) {
                rv_items.remove(id);
                total_price_tv.setText(String.valueOf(total_price));
                myRVadapterSell.notifyDataSetChanged();
                if (rv_items.size() == 0){
                    total_price=0;
                    total_price_tv.setText(total_price+"");
                }
            }
        }, new MyRVadapterSell.OnProductChangeListner() {
            @Override
            public void onProductListChangeListner(ArrayList<Product> updatedList) {
                total_price = 0;
                for (Product p: updatedList){
                    total_price += p.getTotal_price();
                }

                total_price_tv.setText(total_price+"");

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
        clients_for_search = new ArrayList<>();
        getsearchedClients();
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
                    Toast.makeText(SellActivity.this, e.getMessage()+":/autocomplete text view ", Toast.LENGTH_SHORT).show();
                }
            }
        });
        search_client = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item, clients_for_search);
        searchClients.setThreshold(1);
        searchClients.setAdapter(search_client);
        searchClients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                   String phoneNumber = (String) adapterView.getItemAtPosition(i);
               String[] phone = phoneNumber.split(":");
                   searchClients.setText(phone[1]);
            }
        });
sell.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        try {
            String client_number = searchClients.getText().toString();//1
            Date current_date = new Date();//2

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
            String formatted_date = formatter.format(current_date);
            if(client_number.isEmpty() || rv_items.size() == 0 || total_price < 0){
                throw  new Exception();

            }else {
                db.open();

                int id = db.getIdClientbyPhone(client_number);
                Invoice bill = new Invoice();

              int isinserted = (int) db.isertIntoBill(id,total_price,formatted_date);
                bill.setId(isinserted);
              for (Product p : rv_items){
                  db.insert_into_Product_bill(p,bill);
                  db.updateQuantityInInventory(p);
              }

              if (isinserted != -1){

                  Toast.makeText(SellActivity.this, "تم حفظ الفاتورة ", Toast.LENGTH_SHORT).show();
              }
                db.close();
            }
        }catch (Exception e){
            Toast.makeText(SellActivity.this, "بيانات غير صحيحة!", Toast.LENGTH_SHORT).show();
        }




    }
});

add_newClient.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent to_add_client = new Intent(SellActivity.this,AddClientActivity.class);
        launcher.launch(to_add_client);
    }
});
    }

    private void getsearchedClients() {

        ArrayList<Client> data = db.getAllClients();
        for (Client client : data){
            clients_for_search.add(client.toString());
        }

    }

    public void addProducttollist(Product p) {
        if (!rv_items.contains(p)) {
            rv_items.add(p);
            //p.getQuantity_of_sell() * p.getPrice_of_sell();

            myRVadapterSell.notifyDataSetChanged();
            ArrayList<Product> selled = myRVadapterSell.getproducts();
            total_price = 0;
            for (Product pf : rv_items) {
                total_price += pf.getTotal_price();

            }
            total_price_tv.setText(total_price + "");
        }else {
            Toast.makeText(this, "المنتج موجود!", Toast.LENGTH_SHORT).show();
        }
    }

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    clients_for_search.clear();
                    db.open();
                    getsearchedClients();
                    search_client.notifyDataSetChanged();

                    db.close();

                }
            });
public static void totalError(){
    total_price = -1;
    total_price_tv.setText(total_price+"");
}


}