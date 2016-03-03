package com.felipe.manualdobixo.repository.webapi;

/**
 * Created by felipe on 03/03/16.
 */
public interface OnReceiveListener<T> {

    void onReceived(T response);
    void onFail();

}
