package com.williambastos.openevents.model;

import java.io.Serializable;

public class User implements Serializable {
    private String name;
    private String last_name;
    private String email;
    private String password;
    private String image;
    private Integer id;

    public User(String name, String last_name, String email, String password, String image) {
        this.name = name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
        this.image = image;
    }

    public User(String name, String last_name, String email, String password, String image, int id){
        this.name = name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
        this.image = image;
        this.id = id;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }


    public String getName() {
        return name;
    }
    public void setName(String first_name) {
        this.name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }


    public String getImage() {
        return image;
    }

    public Integer getId() {
        return id;
    }

    public void setId(String image) {
        this.id = id;
    }
}
