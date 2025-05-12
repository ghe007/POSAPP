package com.example.posapp.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.example.posapp.R;
import com.example.posapp.model.Client;
import com.example.posapp.model.Inventory;
import com.example.posapp.model.Invoice;
import com.example.posapp.model.Product;
import com.example.posapp.model.Product_Bill;
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
             cursor.close();
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
                cursor.close();
            }

        }

    }catch (Exception e){
        Toast.makeText(ctx, "exception in search productbyname func", Toast.LENGTH_SHORT).show();
    }

    return data;
}
    @SuppressLint("Range")
    public int getquantityInInventory(int id){
    String query = " SELECT "+Mydatabase.Inventory_quantity+" FROM "+Mydatabase.Inventory_table+" WHERE "+Mydatabase.Inventory_ProductID+"=?";
        int quantity = 0;
        Cursor data = database.rawQuery(query,new String[]{id+""});
    if (data != null){
        if (data.moveToFirst()){
            do {
                quantity = data.getInt(data.getColumnIndex(Mydatabase.Inventory_quantity));
            }while (data.moveToNext());
            data.close();
        }
    }
    return quantity;
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
            cursor.close();
        }else {
            Toast.makeText(ctx, R.string.clients_msg_noClients, Toast.LENGTH_SHORT).show();

        }

    }else {
        Toast.makeText(ctx, R.string.clients_msg_noClients, Toast.LENGTH_SHORT).show();
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
                    cursor.close();
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
    cursor1.close();
    return inventory;
}
    public int deleteProdut(int id){
    int result = database.delete(Mydatabase.Product_table,"id = ?",new String[]{id+""});
    return result;
      }

      @SuppressLint("Range")
      public Client getClientByID(int id){
      Client client = new Client();
      String query = " SELECT * FROM "+Mydatabase.Client_table+" WHERE "+Mydatabase.Client_id+" =?";

       Cursor cursor = database.rawQuery(query,new String[]{id+""});

       if (cursor != null){
           if (cursor.moveToFirst()){
               do {
                   client.setId(cursor.getInt(cursor.getColumnIndex(Mydatabase.Client_id)));
                   client.setFullname(cursor.getString(cursor.getColumnIndex(Mydatabase.Cilent_fullName)));
                   client.setPhone_number(cursor.getString(cursor.getColumnIndex(Mydatabase.Client_phoneNumber)));
                   client.setStore_name(cursor.getString(cursor.getColumnIndex(Mydatabase.Client_storeName)));
               }while (cursor.moveToNext());
               cursor.close();
           }
       }
       return client;
      }


      public boolean updateClient(Client client){
         ContentValues values = new ContentValues();

         values.put(Mydatabase.Client_phoneNumber,client.getPhone_number());
         values.put(Mydatabase.Cilent_fullName,client.getFullname());
         values.put(Mydatabase.Client_storeName,client.getStore_name());

       int result = database.update(Mydatabase.Client_table,values,"id = ?",new String[]{client.getId()+""});
       return result!=0;
      }

      @SuppressLint("Range")
      public ArrayList<Client> searchClient(String search_qury){
ArrayList<Client> data = new ArrayList<>();
    String query = " SELECT * FROM "+Mydatabase.Client_table+" WHERE "+Mydatabase.Client_phoneNumber+" LIKE ?"+" OR "+Mydatabase.Cilent_fullName+" LIKE ?";
 Cursor cursor = database.rawQuery(query,new String[]{"%"+search_qury+"%","%"+search_qury+"%"});
 if (cursor != null){
     if (cursor.moveToFirst()){
         do {
             Client list_data = new Client();

             list_data.setId(cursor.getInt(cursor.getColumnIndex(Mydatabase.Client_id)));
             list_data.setFullname(cursor.getString(cursor.getColumnIndex(Mydatabase.Cilent_fullName)));
             list_data.setPhone_number(cursor.getString(cursor.getColumnIndex(Mydatabase.Client_phoneNumber)));
             list_data.setStore_name(cursor.getString(cursor.getColumnIndex(Mydatabase.Client_storeName)));

             data.add(list_data);
         }while (cursor.moveToNext());
         cursor.close();
     }
 }
 return data;
      }
      public  boolean deleteClient(int id){
      int result = database.delete(Mydatabase.Client_table,"id = ?",new String[]{id+""});
      return result !=0;
      }
    @SuppressLint("Range")
      public int getIdClientbyPhone(String phone){
         String query = " SELECT "+Mydatabase.Client_id+" FROM "+Mydatabase.Client_table+" WHERE "+Mydatabase.Client_phoneNumber+" =?";
        int id = -1;
         Cursor data =database.rawQuery(query,new String[]{phone});
         if (data != null){
             if (data.moveToFirst()){
                 do {
                    id = data.getInt(data.getColumnIndex(Mydatabase.Client_id));
                 }while (data.moveToNext());
                 data.close();
             }
         }
         return id;
      }

      public long isertIntoBill(int id_client,double total_price,String date){
      //   String qury = " INSERT INTO "+Mydatabase.Bill_table+"("+Mydatabase.Bill_ClientID+","+Mydatabase.Bill_date+","+Mydatabase.Bill_totalprice+") VALUES(?,?,?)";
          ContentValues data = new ContentValues();
          data.put(Mydatabase.Bill_ClientID,id_client);
          data.put(Mydatabase.Bill_date,date);
          data.put(Mydatabase.Bill_totalprice,total_price);

     long result = database.insert(Mydatabase.Bill_table,null,data);

return result;
      }

    @SuppressLint("Range")
public ArrayList<Invoice> getAllBills(){
    ArrayList<Invoice> data = new ArrayList<>();
    String query = " SELECT *,b." +Mydatabase.Bill_id+
            " AS BIllID,b."+Mydatabase.Bill_ClientID+
            ",b."+Mydatabase.Bill_date+
            ",b."+Mydatabase.Bill_totalprice+
            ",c." +Mydatabase.Cilent_fullName+
            " FROM "+Mydatabase.Bill_table+
            " AS b JOIN "+Mydatabase.Client_table+
            " AS c ON b."+Mydatabase.Bill_ClientID+"= c."+Mydatabase.Client_id;
    try {
        Cursor result = database.rawQuery(query, null);

        if (result != null) {
            if (result.moveToFirst()) {
                do {
                    Invoice invoice = new Invoice();
                    invoice.setId(result.getInt(result.getColumnIndex("BIllID")));
                    invoice.setClient_name(result.getString(result.getColumnIndex(Mydatabase.Cilent_fullName)));
                    invoice.setDate(result.getString(result.getColumnIndex(Mydatabase.Bill_date)));
                    invoice.setTotal_price(result.getDouble(result.getColumnIndex(Mydatabase.Bill_totalprice)));
                    invoice.setClient_id(result.getInt(result.getColumnIndex(Mydatabase.Bill_ClientID)));
                    invoice.setStore_name(result.getString(result.getColumnIndex(Mydatabase.Client_storeName)));
                    invoice.setPhone_number(result.getString(result.getColumnIndex(Mydatabase.Client_phoneNumber)));
                    data.add(invoice);
                } while (result.moveToNext());
                result.close();
            }
        }
    }catch (Exception e){
        Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT).show();
    }
return data;
}

public void insert_into_Product_bill(Product product,Invoice bill){

    ContentValues values = new ContentValues();
    try {

            values.put(Mydatabase.Product_Bill_BillID, bill.getId());
            values.put(Mydatabase.Product_Bill_ProductID, product.getId());
            values.put(Mydatabase.Product_Bill_quantity, product.getQuantity_of_sell());
            values.put(Mydatabase.Product_Bill_totalpriceForProduct, product.getTotal_price());
            values.put(Mydatabase.Product_Bill_priceofsold, product.getPrice_of_sell());

            database.insert(Mydatabase.Product_Bill_table, null, values);


    }catch (Exception e){
        Toast.makeText(ctx," حذث خطأ انثاء حفظ تفاصيل الفاتورة!", Toast.LENGTH_SHORT).show();
    }
}
@SuppressLint("Range")
public int updateQuantityInInventory(Product product){
    int quantity = 0;
    String query = " SELECT "+Mydatabase.Inventory_quantity+" FROM "+Mydatabase.Inventory_table+" WHERE "+Mydatabase.Inventory_ProductID+" = ?";
      Cursor cursor = database.rawQuery(query,new String[]{product.getId()+""});
      if (cursor.moveToFirst()){
          do {
             quantity = cursor.getInt(cursor.getColumnIndex(Mydatabase.Inventory_quantity));
          }while (cursor.moveToNext());
          cursor.close();
      }
    ContentValues values = new ContentValues();
    values.put(Mydatabase.Inventory_quantity,(quantity-product.getQuantity_of_sell()));
  int i = database.update(Mydatabase.Inventory_table,values,Mydatabase.Inventory_ProductID+"=?",new String[]{product.getId()+""});


    return i ;
}


@SuppressLint("Range")
public ArrayList<Product_Bill> getAllBillDetails(int ID){
    ArrayList<Product_Bill> detail_list = new ArrayList<>();
//SELECT * FROM Product_Bill AS PB JOIN Bill AS B , Product AS P ON PB.BillID = B.id AND PB.ProductID = P.id
    String query = " SELECT * ,PB."+Mydatabase.Product_Bill_id+" AS ProductBillId,PB."+Mydatabase.Product_Bill_ProductID+" AS ProductID FROM "+
            Mydatabase.Product_Bill_table+
            " AS PB JOIN "+
            Mydatabase.Bill_table+
            " AS B , "+
            Mydatabase.Product_table+
            " AS P ON PB."+
            Mydatabase.Product_Bill_BillID+
            " = B."+Mydatabase.Bill_id+
            " AND PB."+Mydatabase.Product_Bill_ProductID+
            " = P."+Mydatabase.Product_id +
            " WHERE B."+
            Mydatabase.Bill_id+
            " = ?";

    Cursor data = database.rawQuery(query,new String[]{ID+""});
    try {
        if (data != null) {
            if (data.moveToFirst()) {
                do {
                    Product_Bill details = new Product_Bill();

                    details.setId(data.getInt(data.getColumnIndex("ProductBillId")));
                    details.setBillID(data.getInt(data.getColumnIndex(Mydatabase.Product_Bill_BillID)));
                    details.setProductID(data.getInt(data.getColumnIndex("ProductID")));
                    details.setQuantityOfSold(data.getInt(data.getColumnIndex(Mydatabase.Product_Bill_quantity)));
                    details.setTotalpriceForproduct(data.getDouble(data.getColumnIndex(Mydatabase.Product_Bill_totalpriceForProduct)));
                    details.setPriceOfSold(data.getDouble(data.getColumnIndex(Mydatabase.Product_Bill_priceofsold)));
                    details.setProductname(data.getString(data.getColumnIndex(Mydatabase.Product_nameProduct)));
                    details.setTotalpriceForbill(data.getDouble(data.getColumnIndex(Mydatabase.Bill_totalprice)));
                    details.setBilldate(data.getString(data.getColumnIndex(Mydatabase.Bill_date)));
                    detail_list.add(details);

                } while (data.moveToNext());
                data.close();
            }
        }
    }catch (Exception e){
        Toast.makeText(ctx, " حذث خطأ أنثاء طلب تفاصيل الفاتورة!", Toast.LENGTH_SHORT).show();
    }
    return detail_list;
}
 @SuppressLint("Range")
    public ArrayList<Invoice> searchBillByIdOrTotalOrDate(String query){
        ArrayList<Invoice> data = new ArrayList<>();
        String q = " SELECT b." +Mydatabase.Bill_id+
                " AS BIllID,b."+Mydatabase.Bill_ClientID+
                ",b."+Mydatabase.Bill_date+
                ",b."+Mydatabase.Bill_totalprice+
                ",c." +Mydatabase.Cilent_fullName+
                " FROM "+Mydatabase.Bill_table+
                " AS b JOIN "+Mydatabase.Client_table+
                " AS c ON b."+Mydatabase.Bill_ClientID+"= c."+Mydatabase.Client_id +" WHERE "+Mydatabase.Bill_id+" LIKE ? OR "+Mydatabase.Bill_date+" LIKE ? OR "+Mydatabase.Bill_totalprice+" LIKE ?";
       try {
           Cursor cursor = database.rawQuery(q, new String[]{"%" + query + "%", "%" + query + "%", "%" + query + "%"});

           if (cursor != null) {
               if (cursor.moveToFirst()) {
                   do {
                       Invoice invoice = new Invoice();
                       invoice.setId(cursor.getInt(cursor.getColumnIndex("BIllID")));
                       invoice.setClient_name(cursor.getString(cursor.getColumnIndex(Mydatabase.Cilent_fullName)));
                       invoice.setDate(cursor.getString(cursor.getColumnIndex(Mydatabase.Bill_date)));
                       invoice.setTotal_price(cursor.getDouble(cursor.getColumnIndex(Mydatabase.Bill_totalprice)));
                       invoice.setClient_id(cursor.getInt(cursor.getColumnIndex(Mydatabase.Bill_ClientID)));
                       data.add(invoice);
                   } while (cursor.moveToNext());
                   cursor.close();
               }
           }
       } catch (Exception e) {
           Toast.makeText(ctx, " حذث خطأ عند البحث عن الفاتورة!", Toast.LENGTH_SHORT).show();

       }
       return data;
    }
    @SuppressLint("Range")
    public ArrayList<Inventory> searchProductInventory(String query){
    String q =" SELECT Invetory.id,Invetory.ProductID, Product.nameProduct , Product.barcode , Product.priceOfBuy , Product.priceOfSell,Invetory.quantity FROM Invetory"
                +" JOIN Product ON Invetory.ProductID = Product.id WHERE Product.nameProduct LIKE ? OR Product.barcode LIKE ? OR Product.priceOfBuy LIKE ? OR Product.priceOfSell LIKE ? OR Invetory.quantity LIKE ?";

    ArrayList<Inventory> products = new ArrayList<>();
    try {


        Cursor cursor = database.rawQuery(q,new String[]{"%"+query+"%","%"+query+"%","%"+query+"%","%"+query+"%","%"+query+"%"});

        if (cursor != null){
            if (cursor.moveToFirst()){
                do {
                    Inventory inventory = new Inventory();
                    inventory.setId(cursor.getInt(cursor.getColumnIndex(Mydatabase.Inventory_id)));
                    inventory.setProductID(cursor.getInt(cursor.getColumnIndex(Mydatabase.Inventory_ProductID)));
                    inventory.setQuantity(cursor.getInt(cursor.getColumnIndex(Mydatabase.Inventory_quantity)));
                    inventory.setProduct_name(cursor.getString(cursor.getColumnIndex(Mydatabase.Product_nameProduct)));
                    inventory.setBar_code(cursor.getString(cursor.getColumnIndex(Mydatabase.Product_barcode)));
                    inventory.setPriceOfBuy(cursor.getDouble(cursor.getColumnIndex(Mydatabase.Product_priceOfBuy)));
                    inventory.setPriceOfSell(cursor.getDouble(cursor.getColumnIndex(Mydatabase.Product_priceOfSale)));

                    products.add(inventory);


                }while (cursor.moveToNext());
            }
        }
    } catch (Exception e) {

    }
    return products;
    }

    @SuppressLint("Range")
    public ArrayList<String> getClientsPhone(){
    String query = " SELECT "+Mydatabase.Client_phoneNumber+" FROM "+Mydatabase.Client_table;
        ArrayList<String> phone = new ArrayList<>();
    Cursor cursor = database.rawQuery(query,null);
    if (cursor != null){
        if (cursor.moveToFirst()){
            do {
                phone.add(cursor.getString(cursor.getColumnIndex(Mydatabase.Client_phoneNumber)));
            }while (cursor.moveToNext());
        }
    }

        return phone;
    }

    @SuppressLint("Range")
    public ArrayList<String> getProductbarcode(){
     String query = " SELECT "+Mydatabase.Product_barcode+" FROM "+Mydatabase.Product_table;
     ArrayList<String> barcodes = new ArrayList<>();
     Cursor cursor = database.rawQuery(query,null);
     if (cursor != null){
         if (cursor.moveToFirst()){
             do {
                 barcodes.add(cursor.getString(cursor.getColumnIndex(Mydatabase.Product_barcode)));
             }while (cursor.moveToNext());
         }
     }
     return barcodes;
    }

}
