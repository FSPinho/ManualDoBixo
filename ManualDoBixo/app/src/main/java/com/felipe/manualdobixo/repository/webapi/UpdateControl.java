package com.felipe.manualdobixo.repository.webapi;

import com.orm.SugarRecord;

/**
 * Created by felipe on 03/03/16.
 */
public class UpdateControl extends SugarRecord {
    private Long timestamp;

    public UpdateControl(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
