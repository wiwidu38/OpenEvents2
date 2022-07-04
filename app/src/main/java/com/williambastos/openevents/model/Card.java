package com.williambastos.openevents.model;

public class Card {
    public String title;
    public String date;
    public String geo;
    public String photoSrc;
    public int id;

    public Card(String title, String date, String geo, String photoSrc, int id) {
        this.title = title;
        this.date = date;
        this.geo = geo;
        this.photoSrc = photoSrc;
        this.id = id;
    }
}
