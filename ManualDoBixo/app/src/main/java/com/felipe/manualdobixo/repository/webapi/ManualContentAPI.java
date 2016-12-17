package com.felipe.manualdobixo.repository.webapi;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by felipe on 03/03/16.
 */
public interface ManualContentAPI {

    @GET("out.json")
    Call<ManualContent> getManualContent();


}
