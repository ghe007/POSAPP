package com.example.posapp.model;

import androidx.annotation.Nullable;

import java.util.Objects;

public class Product {
    private int id;
    private String product_name;
    private String barcode;
    private double price_of_buy;
    private double price_of_sell;
   //
    private int quantity_of_sell = 1;
    private double total_price;
    private int quantity;

    public Product(){}

    public Product(String name , String barcode , double price_of_buy , double price_of_sell){
        this.product_name = name;
        this.barcode = barcode;
        this.price_of_buy = price_of_buy;
        this.price_of_sell = price_of_sell;
     //   this.quantity = quantity;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public double getPrice_of_buy() {
        return price_of_buy;
    }

    public void setPrice_of_buy(double price_of_buy) {
        this.price_of_buy = price_of_buy;
    }

    public double getPrice_of_sell() {
        return price_of_sell;
    }

    public void setPrice_of_sell(double price_of_sell) {
        this.price_of_sell = price_of_sell;
    }

    public int getQuantity_of_sell() {
        return quantity_of_sell;
    }
    public void setQuantity_of_sell(int quantity_of_sell) {
        this.quantity_of_sell= quantity_of_sell;
    }

    public double getTotal_price() {
    return quantity_of_sell*price_of_sell;
     //   return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj){
            return  true;
        }
        if (obj == null || getClass() != obj.getClass()){
            return false;
        }
        Product product = (Product) obj;
       return id == product.id;
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
//    public String getQuantity() {
//        return quantity;
//    }
//
//    public void setQuantity(String quantity) {
//        this.quantity = quantity;
//    }
}
