package com.webandrios.financialmanager;

public class ListForRecyclerView {
    private String name;
    private String amount;
    private String imageurl;
    private String date;

    public ListForRecyclerView() {
    }

    public ListForRecyclerView(String name, String amount, String imageUrl, String date) {
        this.name = name;
        this.amount = amount;
        this.imageurl = imageUrl;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getImageUrl() {
        return imageurl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageurl = imageUrl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
