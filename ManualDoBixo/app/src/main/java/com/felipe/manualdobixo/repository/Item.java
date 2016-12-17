package com.felipe.manualdobixo.repository;

import com.orm.SugarRecord;

/**
 * Created by felipe on 29/02/16.
 */
public class Item extends SugarRecord {

    private String title;
    private String text;
    private String image;

    private Topic topic;

    public Item() { }

    public Item(String title, String text, String image, Topic topic) {
        this.title = title;
        this.text = text;
        this.image = image;
        this.topic = topic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
