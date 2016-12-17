package com.felipe.manualdobixo.view.listview;

/**
 * Created by felipe on 01/03/16.
 */
public class Card <T> {

    private String title;
    private String subtitle;
    private String image;
    private T content;

    public Card(String title, String subtitle, String image) {
        this(title, subtitle, image, null);
    }

    public Card(String title, String subtitle, String image, T content) {
        this.title = title;
        this.subtitle = subtitle;
        this.image = image;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

}
