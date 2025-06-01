package com.example.posapp.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SellActivity extends AppCompatActivity {
    private TextView sell_back_btn;
    private static Double total_price =0.0d;
    private static TextView total_price_tv;
    private RecyclerView sell_rv;
    private MyRVadapterSell myRVadapterSell;
    private ArrayList<Product> rv_items;
    private AutoCompleteTextView searchtext,searchClients;
    private ArrayAdapter<String> search_adapter,search_client;
    private ArrayList<String> names_list_forsearch,clients_for_search;
    private DataBaseControler db;
    private String search_client_phone;
    private Button sell,add_newClient;
    private TableLayout tableLayout;

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
                total_price = 0.0d;
                for (Product p : rv_items){
                    total_price += p.getTotal_price();
                }
                total_price_tv.setText(String.valueOf(total_price));
                myRVadapterSell.notifyDataSetChanged();
                if (rv_items.size() == 0){
                    total_price=0.0d;
                    total_price_tv.setText(total_price+"");
                }
            }
        }, new MyRVadapterSell.OnProductChangeListner() {
            @Override
            public void onProductListChangeListner(ArrayList<Product> updatedList) {
                total_price = 0.0d;
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
                   searchClients.setText(phone[0]);
                   search_client_phone = phone[1];
            }
        });
sell.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        try {
            //String client_number = searchClients.getText().toString();//1
            Date current_date = new Date();//2

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
            String formatted_date = formatter.format(current_date);
            if(search_client_phone.isEmpty() || rv_items.size() == 0 || total_price < 0){
                throw  new Exception();

            }else {
                db.open();

                int id = db.getIdClientbyPhone(search_client_phone);
                search_client_phone = "";
                if (id == -1){
                    throw new RuntimeException();
                }
                Client client = new Client();
                client = db.getClientByID(id);
                Invoice bill = new Invoice();

              int isinserted = (int) db.isertIntoBill(id,total_price,formatted_date);
                bill.setId(isinserted);
                generatePdfDoc(client,isinserted,formatted_date);
              for (Product p : rv_items){
                  db.insert_into_Product_bill(p,bill);
                  db.updateQuantityInInventory(p);
              }


                db.close();
            }
        }catch (Exception e){
            if (e instanceof RuntimeException){
                Toast.makeText(SellActivity.this, "اسم الزبون خاطئ!", Toast.LENGTH_SHORT).show();
            }
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
            total_price = 0.0d;
            for (Product pf : rv_items) {
                total_price += pf.getTotal_price();

            }
            total_price_tv.setText(total_price + "0");
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
    total_price = -1.0;
    total_price_tv.setText(total_price+"");
}
  private void generatePdfDoc(Client client,int billID,String date){

        View invoiceView = LayoutInflater.from(SellActivity.this).inflate(R.layout.pdf_xml, null);
        DisplayMetrics displayMetrics = new DisplayMetrics();

        TextView client_name = invoiceView.findViewById(R.id.pdf_view_client_name);
        TextView client_phone = invoiceView.findViewById(R.id.pdf_view_client_phone);
        TextView client_store = invoiceView.findViewById(R.id.pdf_view_store);
        TextView client_date = invoiceView.findViewById(R.id.pdf_view_date);
        TextView bill_number = invoiceView.findViewById(R.id.pdf_view_bill_id);

        client_name.setText("اسم العميل: "+client.getFullname());
        client_phone.setText("رقم العميل: "+client.getPhone_number());
        client_store.setText("المتجر: "+client.getStore_name());
        client_date.setText("تاريخ الفاتورة: "+date);
        bill_number.setText("رقم الفاتورة: "+billID);

        tableLayout = invoiceView.findViewById(R.id.table);
        TableRow info = new TableRow(SellActivity.this);

        TextView total_text = new TextView(SellActivity.this);
        TextView price_text = new TextView(SellActivity.this);
        TextView quantity_text = new TextView(SellActivity.this);
        TextView name_text = new TextView(SellActivity.this);
        total_text.setText("الاجمالي");
        total_text.setTextColor(Color.WHITE);
        price_text.setText("السعر");
        price_text.setTextColor(Color.WHITE);
        quantity_text.setText("الكمية");
        quantity_text.setTextColor(Color.WHITE);
        name_text.setText("المنتج");
        name_text.setTextColor(Color.WHITE);

        info.addView(total_text);
        info.addView(price_text);
        info.addView(quantity_text);
        info.addView(name_text);
        info.setGravity(Gravity.CENTER);
        info.setBackgroundResource(android.R.color.darker_gray);
        tableLayout.addView(info);
        for (Product p : rv_items) {
            TableRow row = new TableRow(SellActivity.this);


            TextView total = new TextView(SellActivity.this);
            total.setText(p.getTotal_price()+"");
            total.setTextColor(Color.BLACK);
            total.setGravity(Gravity.CENTER);


            TextView price = new TextView(SellActivity.this);
            price.setText(p.getPrice_of_sell() + "");
            price.setTextColor(Color.BLACK);
            price.setGravity(Gravity.END);


            TextView qty = new TextView(SellActivity.this);
            qty.setText(p.getQuantity_of_sell()+"");
            qty.setTextColor(Color.BLACK);
            qty.setGravity(Gravity.END);


            TextView name = new TextView(SellActivity.this);
            name.setText(p.getProduct_name());
            name.setGravity(Gravity.START);
            name.setTextColor(Color.BLACK);


            row.addView(total);
            row.addView(price);
            row.addView(qty);
            row.addView(name);


            tableLayout.addView(row);


        }

        TextView total_in_bill = new TextView(SellActivity.this);
        total_in_bill = invoiceView.findViewById(R.id.pdf_view_total);
        total_in_bill.setText(total_price+"");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            this.getDisplay().getRealMetrics(displayMetrics);
        }else this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        invoiceView.measure(View.MeasureSpec.makeMeasureSpec(displayMetrics.widthPixels, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(displayMetrics.heightPixels,View.MeasureSpec.EXACTLY)
        );

        invoiceView.layout(0,0,displayMetrics.widthPixels,4000);

        PdfDocument document = new PdfDocument();

        int viewDocWidth = invoiceView.getMeasuredWidth();
        int viewDocHeight = invoiceView.getMeasuredHeight();

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(viewDocWidth,4000,1).create();

        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        invoiceView.draw(canvas);
        document.finishPage(page);

        File pdfpath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String pdf_name = "الفاتورة "+billID+".pdf";

        File file = new File(pdfpath+"/Invoices/",pdf_name);

        try {
            FileOutputStream fos = new FileOutputStream(file);
            document.writeTo(fos);
            document.close();
            fos.close();
            Toast.makeText(SellActivity.this, "تم حفظ الفاتورة رقم "+billID, Toast.LENGTH_LONG).show();


        } catch (Exception e) {
            Toast.makeText(SellActivity.this,"خطأ أثناء حفظ الفاتورة!",Toast.LENGTH_SHORT).show();
            Log.e("myerror",e.getMessage());
        }
    }








}