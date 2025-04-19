package com.example.posapp.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.example.posapp.model.Client;
import com.example.posapp.model.Inventory;
import com.example.posapp.model.Product;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;

public class DataBaseControler {

    private SQLiteDatabase database ;
    private SQLiteAssetHelper sqLiteAssetHelper;
    private static DataBaseControler dataBaseControler ;
    private  Context ctx;

private DataBaseControler(Context context){
   sqLiteAssetHelper = new Mydatabase(context);
    ctx = context;
 }

 public void IsConnected(){
     Toast.makeText(ctx, sqLiteAssetHelper.getDatabaseName(), Toast.LENGTH_SHORT).show();
 }


public static DataBaseControler getInstance(Context ctx){
    if(dataBaseControler == null){
        dataBaseControler = new DataBaseControler(ctx);
    }
    return dataBaseControler;
}



public void open(){
    if (database == null){
        database = sqLiteAssetHelper.getWritableDatabase();
    }
}

public void close(){
    if (database != null){
        this.database.close();
        database =null;
    }
}

public boolean addProduct(Product product){
    ContentValues values = new ContentValues();
   // String barcode_validation;
    values.put(Mydatabase.Product_nameProduct , product.getProduct_name());
    values.put(Mydatabase.Product_priceOfBuy , product.getPrice_of_buy());
    values.put(Mydatabase.Product_priceOfSale , product.getPrice_of_sell());

          values.put(Mydatabase.Product_barcode , product.getBarcode());
  long result =  database.insert(Mydatabase.Product_table,null,values);
  product.setId((int)result);

return result != -1;
}
    @SuppressLint("Range")
public ArrayList<Inventory> getAllProductsIntoInventory() {

        ArrayList<Inventory> products = new ArrayList<>();
//        try {
            String query =" SELECT Invetory.id,Invetory.ProductID, Product.nameProduct , Product.barcode , Product.priceOfBuy , Product.priceOfSell,Invetory.quantity FROM Invetory"
            +" JOIN Product ON Invetory.ProductID = Product.id";


            Cursor cursor = database.rawQuery(query, null);


                if (cursor.moveToFirst()) {

                    do {

                        int id = cursor.getInt(cursor.getColumnIndex(Mydatabase.Inventory_id));
                        int ProductID = cursor.getInt(cursor.getColumnIndex(Mydatabase.Inventory_ProductID));
                        String product_name = cursor.getString(cursor.getColumnIndex(Mydatabase.Product_nameProduct));
                        int quantity = cursor.getInt(cursor.getColumnIndex(Mydatabase.Inventory_quantity));
                        String bar_code = cursor.getString(cursor.getColumnIndex(Mydatabase.Product_barcode));
                        double priceOfBuy = cursor.getDouble(cursor.getColumnIndex(Mydatabase.Product_priceOfBuy));
                        double priceOfSell = cursor.getDouble(cursor.getColumnIndex(Mydatabase.Product_priceOfSale));


                        Inventory inventory = new Inventory(id, ProductID, quantity, product_name, bar_code, priceOfBuy, priceOfSell);
                        products.add(inventory);

                    } while (cursor.moveToNext());
                    cursor.close();
                }else {
                    Toast.makeText(ctx, "empty cursor", Toast.LENGTH_SHORT).show();
                }
//            } catch(Exception e){
//                Log.e("getAllProducts", e.getMessage());
           // }

    return products;
}

public boolean insertNewProductIntoInventory(int quantity , int id){

    ContentValues values = new ContentValues();
    values.put(Mydatabase.Inventory_quantity , quantity);
    values.put(Mydatabase.Inventory_ProductID,id);

   long result = database.insert(Mydatabase.Inventory_table,null,values);

    return result != -1;
}
    @SuppressLint("Range")
public ArrayList<Product> getAllProducts() {
    ArrayList<Product> products = new ArrayList<>();
    try {
        String query = "SELECT * FROM Product";
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                 int id = cursor.getInt(cursor.getColumnIndex(Mydatabase.Product_id));
                String name = cursor.getString(cursor.getColumnIndex(Mydatabase.Product_nameProduct));
                String barcode = cursor.getString(cursor.getColumnIndex(Mydatabase.Product_barcode));
                double priceOfBuy = cursor.getDouble(cursor.getColumnIndex(Mydatabase.Product_priceOfBuy));
                double priceOfSell = cursor.getDouble(cursor.getColumnIndex(Mydatabase.Product_priceOfSale));
                Product product = new Product(name,barcode,priceOfBuy,priceOfSell);
                products.add(product);
                } while (cursor.moveToNext());
            cursor.close();

            Toast.makeText(ctx, products.get(0).getProduct_name(), Toast.LENGTH_SHORT).show();
        }
    } catch(Exception e){
        Log.e("getAllProducts", e.getMessage());
        }
    return products;
}

    @SuppressLint("Range")
    public ArrayList<String> getProductsName(){
    ArrayList<String> names = new ArrayList<>();
    String query = "SELECT "+Mydatabase.Product_nameProduct+" FROM "+Mydatabase.Product_table;
   Cursor cursor= database.rawQuery(query,null);

   if (cursor != null){
         if (cursor.moveToFirst()){
              do {
                 String name = cursor.getString(cursor.getColumnIndex(Mydatabase.Product_nameProduct));
                 names.add(name);
              }while (cursor.moveToNext());

         }

   }else {
       Toast.makeText(ctx, "لاتوجد بيانات", Toast.LENGTH_SHORT).show();
   }

    cursor.close();

   return names;
}
    @SuppressLint("Range")
public  Product getProductByname(String name){
        ArrayList<Product> data_list = new ArrayList<Product>();
          Product data = new Product();
    try {
        String query = " SELECT "+Mydatabase.Product_id+","+Mydatabase.Product_nameProduct+"," +Mydatabase.Product_priceOfSale+" FROM "+Mydatabase.Product_table+" WHERE "+Mydatabase.Product_nameProduct+" = ?";

        Cursor cursor = database.rawQuery(query,new String[]{name});
        if (cursor != null){
            if (cursor.moveToFirst()){
                do {
                    data.setProduct_name(cursor.getString(cursor.getColumnIndex(Mydatabase.Product_nameProduct)));
                    data.setId(cursor.getInt(cursor.getColumnIndex(Mydatabase.Product_id)));
                    data.setPrice_of_sell(cursor.getDouble(cursor.getColumnIndex(Mydatabase.Product_priceOfSale)));
                   // data_list.add(data);
                }while (cursor.moveToNext());

            }

        }

    }catch (Exception e){
        Toast.makeText(ctx, "exception in search productbyname func", Toast.LENGTH_SHORT).show();
    }

    return data;
}

    @SuppressLint("Range")
  public ArrayList<Client> getAllClients(){
    String query = "SELECT * FROM "+Mydatabase.Client_table;
    ArrayList<Client> clients = new ArrayList<>();

  Cursor cursor = database.rawQuery(query,null);
    if (cursor != null){
        if (cursor.moveToFirst()){
            do {
                Client client = new Client();
                client.setId(cursor.getInt(cursor.getColumnIndex(Mydatabase.Client_id)));
                client.setFullname(cursor.getString(cursor.getColumnIndex(Mydatabase.Cilent_fullName)));
                client.setStore_name(cursor.getString(cursor.getColumnIndex(Mydatabase.Client_storeName)));
                client.setPhone_number(cursor.getString(cursor.getColumnIndex(Mydatabase.Client_phoneNumber)));

                clients.add(client);
            }while (cursor.moveToNext());
        }else {
            Toast.makeText(ctx, "لايوجد زبائن!", Toast.LENGTH_SHORT).show();

        }

    }else {
        Toast.makeText(ctx, "لايوجد زبائن!", Toast.LENGTH_SHORT).show();
    }
    return clients;
  }

  public boolean addClient(Client client){
      long result = 0;
    ContentValues valus = new ContentValues();
    valus.put("fullName",client.getFullname());
    valus.put("phoneNumber",client.getPhone_number());
    valus.put("storeName",client.getStore_name());
    try{
        result = database.insert(Mydatabase.Client_table,null,valus);
    }catch (Exception e){
        Toast.makeText(ctx ,"خطأ أثناء اضافة زبون!",Toast.LENGTH_SHORT).show();
    }
     return result != -1;
  }


  public boolean updateProduct(Product product , int quantity_in_inventorey){

    ContentValues values_to_product_table = new ContentValues();
   int id_p = product.getId();
      values_to_product_table.put(Mydatabase.Product_barcode,product.getBarcode());
      values_to_product_table.put(Mydatabase.Product_nameProduct,product.getProduct_name());
      values_to_product_table.put(Mydatabase.Product_priceOfBuy,product.getPrice_of_buy());
      values_to_product_table.put(Mydatabase.Product_priceOfSale,product.getPrice_of_sell());


      ContentValues values_to_inventory_table = new ContentValues();
      values_to_inventory_table.put(Mydatabase.Inventory_quantity,quantity_in_inventorey);
   int result_product_table = database.update(Mydatabase.Product_table,values_to_product_table,"id=?",new String[]{id_p+""});
   int result_inventory_table =database.update(Mydatabase.Inventory_table,values_to_inventory_table,Mydatabase.Inventory_ProductID+"=?",new String[]{id_p+""});
      return (result_product_table != 0 && result_inventory_table !=0) ;
  }



    @SuppressLint("Range")
    public  Product getProductById(int id){
       Product data = new Product();


        try {
            String query = "SELECT * FROM "+Mydatabase.Product_table+" WHERE "+Mydatabase.Product_id+" =?";

            Cursor cursor = database.rawQuery(query,new String[]{id+""});



            if (cursor != null){
                if (cursor.moveToFirst()){
                    do {
                        data.setProduct_name(cursor.getString(cursor.getColumnIndex(Mydatabase.Product_nameProduct)));
                        data.setId(cursor.getInt(cursor.getColumnIndex(Mydatabase.Product_id)));
                        data.setBarcode(cursor.getString(cursor.getColumnIndex(Mydatabase.Product_barcode)));
                        data.setPrice_of_buy(cursor.getDouble(cursor.getColumnIndex(Mydatabase.Product_priceOfBuy)));
                        data.setPrice_of_sell(cursor.getDouble(cursor.getColumnIndex(Mydatabase.Product_priceOfSale)));

                    }while (cursor.moveToNext());

                }

            }

        }catch (Exception e){
            Toast.makeText(ctx, "حدث خطأ أثناء جلب البيانات!", Toast.LENGTH_SHORT).show();
        }

        return data ;
    }

@SuppressLint("Range")
public Inventory getQuantity(int porduct_id){
    Inventory inventory = new Inventory();
    String query_get_quantity = "SELECT "+Mydatabase.Inventory_quantity+" FROM "+Mydatabase.Inventory_table+" WHERE "+Mydatabase.Inventory_ProductID+"=?";
    Cursor cursor1 = database.rawQuery(query_get_quantity,new String[]{porduct_id+""});
    if (cursor1.moveToFirst()){
        inventory.setQuantity(cursor1.getInt(cursor1.getColumnIndex(Mydatabase.Inventory_quantity)));

    }

    return inventory;
}
    public int deleteProdut(int id){
    int result = database.delete(Mydatabase.Product_table,"id = ?",new String[]{id+""});
    return result;
      }
}
