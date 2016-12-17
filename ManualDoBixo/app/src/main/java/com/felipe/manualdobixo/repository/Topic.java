package com.felipe.manualdobixo.repository;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by felipe on 29/02/16.
 */
public class Topic extends SugarRecord {

    private String title;
    private String image;

    public Topic() { }

    public Topic(String title, String image) {
        this.title = title;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Item> getItems() {
        return Item.find(Item.class, "topic = ?", new String[] { "" + getId() });
    }

}
