package com.felipe.manualdobixo.repository.webapi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by felipe on 03/03/16.
 */
public class ManualContentService {

    public void getManualContent(String baseURL, final OnReceiveListener<ManualContent> onReceiveListener) {
        Gson gson = new GsonBuilder().registerTypeAdapter(
                ManualContent.class,
                new ManualContentSerializer()
        ).create();

        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        final ManualContentAPI manualContentAPI = retrofit.create(ManualContentAPI.class);

        Call<ManualContent> call = manualContentAPI.getManualContent();
        call.enqueue(new Callback<ManualContent>() {

            @Override
            public void onResponse(Call<ManualContent> call, Response<ManualContent> response) {
                ManualContent manualContent = response.body();

                if(onReceiveListener != null) {

                    if(manualContent != null)
                        onReceiveListener.onReceived(manualContent);
                    else
                        onReceiveListener.onFail();
                }

            }

            @Override
            public void onFailure(Call<ManualContent> call, Throwable t) {
                if(onReceiveListener != null)
                    onReceiveListener.onFail();
            }

        });
    }

}
