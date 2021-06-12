package com.example.notify;

import java.util.HashMap;

public class firebaseModel {

    private String title;
    private String content;
    private String date;
    private String color;

    public  firebaseModel(){

    }
    public firebaseModel(String title,String content,String date,String color){
        this.title = title;
        this.content = content;
        this.date = date;
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
