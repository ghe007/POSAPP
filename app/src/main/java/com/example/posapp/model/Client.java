package com.example.posapp.model;

public class Client {

    private int id;
    private String fullname;
    private String store_name;
    private String phone_number;
    public Client(int id, String fullname, String store_name, String phone_number) {
        this.id = id;
        this.fullname = fullname;
        this.store_name = store_name;
        this.phone_number = phone_number;
    }
    public Client( String fullname, String store_name, String phone_number) {
        this.fullname = fullname;
        this.store_name = store_name;
        this.phone_number = phone_number;
    }
    public Client(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
}
