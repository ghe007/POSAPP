package com.example.posapp.database;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;



public class Mydatabase extends SQLiteAssetHelper {

    private  static  final String db_name = "posm.db";
    private static final int vers = 1;

// Tables : every space mean we did another table
    public static final String Client_table = "Client";
    public static final String Client_id = "id";
    public static final String Cilent_fullName = "fullName";
    public static final String Client_phoneNumber = "phoneNumber";
    public static final String Client_storeName = "storeName";

    public static final String Bill_table = "Bill";
    public static final String Bill_id = "id";
    public static final String Bill_ClientID = "ClientID";
    public static final String Bill_date = "billdate"; // date Auto set
    public static final String Bill_totalprice = "totalprice";

    public static final String Inventory_table = "Invetory";
    public static final String Inventory_id = "id";
    public static final String Inventory_quantity= "quantity";
    public static final String Inventory_ProductID = "ProductID";

    public static final String Product_table = "Product";
    public static final String Product_id = "id";
    public static final String Product_nameProduct = "nameProduct";
    public static final String Product_priceOfBuy = "priceOfBuy";
    public static final String Product_priceOfSale = "priceOfSell";
    public static final String Product_barcode = "barcode";

    public static final String Product_Bill_table  = "Product_Bill";
    public static final String Product_Bill_id = "id";
    public static final String Product_Bill_ProductID = "ProductID";
    public static final String Product_Bill_BillID = "BillID";
    public static final String Product_Bill_quantity = "quantity";
    public static final String Product_Bill_totalpriceForProduct = "totalpriceForProduct";




    public Mydatabase(Context context) {
        super(context, db_name, null, vers);
    }
}
