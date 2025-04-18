package com.example.posapp.model;

public class Inventory {
    private int id;
    private int ProductID;
    private int quantity;
    private String product_name;
    private String bar_code;
    private double  priceOfBuy;
    private double priceOfSell;


    public Inventory(){}

    public Inventory(int id,int ProductID,int quantity,String product_name,String bar_code, double priceOfBuy,double priceOfSell){
        this.id = id;
        this.ProductID = ProductID;
        this.quantity = quantity;
        this.product_name = product_name;
        this.bar_code = bar_code;
        this.priceOfBuy = priceOfBuy;
        this.priceOfSell = priceOfSell;



    }
    public Inventory(int productID,int product_quantity){
        this.ProductID = productID;
        this.quantity = product_quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductID() {
        return ProductID;
    }

    public void setProductID(int productID) {
        ProductID = productID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getBar_code() {
        return bar_code;
    }

    public void setBar_code(String bar_code) {
        this.bar_code = bar_code;
    }

    public double getPriceOfBuy() {
        return priceOfBuy;
    }

    public void setPriceOfBuy(double priceOfBuy) {
        this.priceOfBuy = priceOfBuy;
    }

    public double getPriceOfSell() {
        return priceOfSell;
    }

    public void setPriceOfSell(double priceOfSell) {
        this.priceOfSell = priceOfSell;
    }
}
