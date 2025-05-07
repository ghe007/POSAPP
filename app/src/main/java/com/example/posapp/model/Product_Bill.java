package com.example.posapp.model;

public class Product_Bill {
    private int id , productID,billID,quantityOfSold;
    private double totalpriceForproduct,priceOfSold , totalpriceForbill;
    private String productname , billdate;

    public Product_Bill() {
    }

    public Product_Bill(int id, int productID, int billID, int quantityOfSold, double totalpriceForproduct, double priceOfSold, String productname) {
        this.id = id;
        this.productID = productID;
        this.billID = billID;
        this.quantityOfSold = quantityOfSold;
        this.totalpriceForproduct = totalpriceForproduct;
        this.priceOfSold = priceOfSold;
        this.productname = productname;
    }

    public Product_Bill(Product product , Invoice bill) {

        this.productID = product.getId();
        this.billID = bill.getId();
        this.quantityOfSold = product.getQuantity_of_sell();
        this.totalpriceForproduct = product.getTotal_price();
        this.priceOfSold = product.getPrice_of_sell();
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getBillID() {
        return billID;
    }

    public void setBillID(int billID) {
        this.billID = billID;
    }

    public int getQuantityOfSold() {
        return quantityOfSold;
    }

    public void setQuantityOfSold(int quantityOfSold) {
        this.quantityOfSold = quantityOfSold;
    }

    public double getTotalpriceForproduct() {
        return totalpriceForproduct;
    }

    public void setTotalpriceForproduct(double totalpriceForproduct) {
        this.totalpriceForproduct = totalpriceForproduct;
    }

    public double getPriceOfSold() {
        return priceOfSold;
    }

    public void setPriceOfSold(double priceOfSold) {
        this.priceOfSold = priceOfSold;
    }

    public double getTotalpriceForbill() {
        return totalpriceForbill;
    }

    public void setTotalpriceForbill(double totalpriceForbill) {
        this.totalpriceForbill = totalpriceForbill;
    }
    public String getBilldate() {
        return billdate;
    }
    public void setBilldate(String billdate) {
        this.billdate = billdate;
    }
}
