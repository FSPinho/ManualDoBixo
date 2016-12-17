package com.felipe.manualdobixo.repository.webapi;

/**
 * Created by felipe on 03/03/16.
 */
public class ManualContent {

    private long lastUpdate;
    private String text;

    public ManualContent(long lastUpdate, String text) {
        this.lastUpdate = lastUpdate;
        this.text = text;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
